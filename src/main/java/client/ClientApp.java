package client;


import common.dto.CommandFromClientDto;
import common.dto.CommandResultDto;
import common.exceptions.DataCantBeSentException;
import common.exceptions.NoAnswerException;
import common.utilities.Pair;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.UnresolvedAddressException;

import static server.ServerApp.bytesToInt;
import static server.ServerApp.deserialize;

@SuppressWarnings("FieldCanBeLocal")
public class ClientApp {
    private final int clientPort;
    private final int serverPort;
    private final String clientIp;
    private final String serverIp;
    private final int waitingTime = 500;
    private final int countOfBytesForSize = 4;
    private final int timeoutToSend = 10;


    public ClientApp(int clientPort, int serverPort, String clientIp, String serverIp) {
        this.clientPort = clientPort;
        this.serverPort = serverPort;
        this.clientIp = clientIp;
        this.serverIp = serverIp;
    }


    public CommandResultDto sendCommand(CommandFromClientDto commandFromClientDto) throws DataCantBeSentException {
        try (DatagramChannel datagramChannel = DatagramChannel.open()) {
            datagramChannel.configureBlocking(false);
            send(datagramChannel, commandFromClientDto);
            return receiveCommandResult(datagramChannel);
        } catch (BindException e) {
            return new CommandResultDto("Не удалось отправить данные по адресу, исключение привязки. Пожалуйста, перезапустите клиент с другими аргументами");
        } catch (IOException e) {
            return new CommandResultDto("Что-то пошло не так при выполнении команды, сообщение: " + e.getMessage());
        }

    }



    private void send(DatagramChannel datagramChannel, CommandFromClientDto commandFromClientDto) throws IOException, DataCantBeSentException {

        datagramChannel.bind(new InetSocketAddress(clientIp, clientPort));

        SocketAddress serverSocketAddress = new InetSocketAddress(serverIp, serverPort);

        Pair<byte[], byte[]> pair = serialize(commandFromClientDto);

        byte[] sendDataBytes = pair.getFirst();
        byte[] sendDataAmountBytes = pair.getSecond();

        try {
            ByteBuffer sendDataAmountWrapper = ByteBuffer.wrap(sendDataAmountBytes);
            int limit = timeoutToSend;
            while (datagramChannel.send(sendDataAmountWrapper, serverSocketAddress) < sendDataAmountBytes.length) {
                limit -= 1;
                System.out.println("Не удалось отправить, повторите попытку");
                if (limit == 0) {
                    throw new DataCantBeSentException();
                }
            }
            ByteBuffer sendBuffer = ByteBuffer.wrap(sendDataBytes);
            while (datagramChannel.send(sendBuffer, serverSocketAddress) < sendDataBytes.length) {
                limit -= 1;
                System.out.println("Не удалось отправить, повторите попытку");
                if (limit == 0) {
                    throw new DataCantBeSentException();
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось разрешить указанный вами адрес. Пожалуйста, проверьте это и, возможно, перезапустите клиент");
        } catch (UnresolvedAddressException e) {
            System.out.println("Не удалось отправить данные, потому что они были слишком большими");
        }
    }


    private CommandResultDto receiveCommandResult(DatagramChannel datagramChannel) throws IOException {
        byte[] amountOfBytesHeader = new byte[countOfBytesForSize];
        ByteBuffer amountOfBytesHeaderWrapper = ByteBuffer.wrap(amountOfBytesHeader);
        try {
            receiveToBuffer(datagramChannel, amountOfBytesHeaderWrapper, waitingTime);
            byte[] dataBytes = new byte[bytesToInt(amountOfBytesHeader)];


            ByteBuffer dataBytesWrapper = ByteBuffer.wrap(dataBytes);

            receiveToBuffer(datagramChannel, dataBytesWrapper, 1);

            return (CommandResultDto) deserialize(dataBytes);

        } catch (NoAnswerException e) {
            return new CommandResultDto("Не удалось получить никакого ответа от сервера");
        } catch (ClassNotFoundException e) {
            return new CommandResultDto("Получен неверный ответ от сервера");
        }
    }


    private void receiveToBuffer(DatagramChannel datagramChannel, ByteBuffer receiverBuffer, int timeoutMills) throws NoAnswerException, IOException {
        int timeout = timeoutMills;
        SocketAddress checkingAddress = null;

        while (checkingAddress == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace(); // never thrown
            }
            checkingAddress = datagramChannel.receive(receiverBuffer);
            if (timeout == 0) {
                throw new NoAnswerException("Timeout exceeded. Could not receive any data");
            }
            timeout--;
        }
    }

    /**
     * @return first - data itself, second - amount of bytes in data
     */
    private Pair<byte[], byte[]> serialize(Object obj) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(obj);
        byte[] sizeBytes = ByteBuffer.allocate(countOfBytesForSize).putInt(byteArrayOutputStream.size()).array();

        return new Pair<>(byteArrayOutputStream.toByteArray(), sizeBytes);
    }

    public static int bytesToInt(byte[] bytes) {
        final int vosem = 8;
        final int ff = 0xFF;

        int value = 0;
        for (byte b : bytes) {
            value = (value << vosem) + (b & ff);
        }
        return value;
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

}
