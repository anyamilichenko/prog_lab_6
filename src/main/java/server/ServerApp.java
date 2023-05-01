package server;

import common.commands.AbstractCommand;
import common.commands.HelpCommand;
import common.data.Dragon;
import common.dto.CommandFromClientDto;
import common.dto.CommandResultDto;
import common.exceptions.DataCantBeSentException;
import common.utilities.CollectionManager;
import common.utilities.HistoryManager;
import common.utilities.Pair;
import server.commands.SaveCommand;
import server.utilities.FileManager;
import server.utilities.JsonParser;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;




@SuppressWarnings("FieldCanBeLocal")
public class ServerApp {
    private final HistoryManager historyManager;
    private final CollectionManager collectionManager;
    private final FileManager fileManager;
    private final Logger logger;
    private final int countOfBytesForSize = 4;
    private final int serverWaitingPeriod = 50;
    private String stringData;
    private final int timeoutToSend = 10;

    public ServerApp(HistoryManager historyManager, CollectionManager collectionManager, FileManager fileManager, Logger logger) {
        this.logger = logger;
        this.collectionManager = collectionManager;
        this.historyManager = historyManager;
        this.fileManager = fileManager;
    }

    public void start(int serverPort, String serverIp) throws IOException {
        try (DatagramChannel datagramChannel = DatagramChannel.open()) {
            initialise(datagramChannel, serverIp, serverPort);
            boolean isWorkingState = true;
            datagramChannel.configureBlocking(false);
            Scanner scanner = new Scanner(System.in);
            while (isWorkingState) {
                if (System.in.available() > 0) {
                    final String inp = scanner.nextLine();
                    if ("exit".equals(inp)) {
                        isWorkingState = false;
                    }
                    if ("save".equals(inp)) {
                        System.out.println(new SaveCommand(fileManager).execute(collectionManager, historyManager));
                    }
                }
                byte[] amountOfBytesHeader = new byte[countOfBytesForSize];
                ByteBuffer amountOfBytesHeaderWrapper = ByteBuffer.wrap(amountOfBytesHeader);
                SocketAddress clientSocketAddress = datagramChannel.receive(amountOfBytesHeaderWrapper);
                if (Objects.nonNull(clientSocketAddress)) {
                    AbstractCommand command = receive(amountOfBytesHeader, datagramChannel);
                    CommandResultDto commandResultDto = command.execute(collectionManager, historyManager);
                    logger.info("выполнена команда с результатом: " + commandResultDto.toString());
                    send(commandResultDto, datagramChannel, clientSocketAddress);
                }
            }
            System.out.println(new SaveCommand(fileManager).execute(collectionManager, historyManager));
        } catch (DataCantBeSentException | InterruptedException e) {
            logger.info("Could not send data to client");
        } catch (BindException e) {
            logger.error("Не удалось использовать эти порты и ip, BindException. Пожалуйста, перезапустите сервер с другими аргументами");
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            logger.error("Возникла проблема с файлом данных. Пожалуйста, проверьте, доступно ли он.");
        }
    }

    private void send(CommandResultDto commandResultDto, DatagramChannel datagramChannel, SocketAddress clientSocketAddress) throws IOException, DataCantBeSentException {
        // Send
        Pair<byte[], byte[]> pair = serialize(commandResultDto);

        byte[] sendDataBytes = pair.getFirst();
        byte[] sendDataAmountBytes = pair.getSecond();


        try {
            ByteBuffer sendDataAmountWrapper = ByteBuffer.wrap(sendDataAmountBytes);
            int limit = timeoutToSend;
            while (datagramChannel.send(sendDataAmountWrapper, clientSocketAddress) <= 0) {
                limit -= 1;
                logger.info("не удалось отправить, повторите попытку");
                if (limit == 0) {
                    throw new DataCantBeSentException();
                }
            }
            ByteBuffer sendBuffer = ByteBuffer.wrap(sendDataBytes);
            while (datagramChannel.send(sendBuffer, clientSocketAddress) <= 0) {
                limit -= 1;
                logger.info("не удалось отправить, повторите попытку");
                if (limit == 0) {
                    throw new DataCantBeSentException();
                }
            }
            logger.info("отправил результат команды клиенту");
        } catch (IOException e) {
            logger.error("не удалось отправить данные клиенту, потому что сообщение слишком большое");
        }
    }

    private AbstractCommand receive(byte[] amountOfBytesHeader, DatagramChannel datagramChannel) throws IOException, InterruptedException {
        // Получение
        byte[] dataBytes = new byte[bytesToInt(amountOfBytesHeader)];

        ByteBuffer dataBytesWrapper = ByteBuffer.wrap(dataBytes);

        Thread.sleep(serverWaitingPeriod);

        SocketAddress checkAddress = datagramChannel.receive(dataBytesWrapper);
        while (checkAddress == null) {
            checkAddress = datagramChannel.receive(dataBytesWrapper);
        }

        CommandFromClientDto commandFromClientDto;
        try {
            commandFromClientDto = (CommandFromClientDto) deserialize(dataBytes);
        } catch (ClassNotFoundException e) {
            return new HelpCommand();
        }
        logger.info("Получен объект данных: " + commandFromClientDto.getCommand().toString());
        return (commandFromClientDto).getCommand();
    }


    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    /**
     * @return первое - сами данные, второе - количество байтов в данных
     */
    public Pair<byte[], byte[]> serialize(Object obj) throws IOException {

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
    private void initialise(DatagramChannel datagramChannel, String serverIp, int serverPort) throws IOException {
        datagramChannel.bind(new InetSocketAddress(serverIp, serverPort));
        logger.info("Создаем канал датаграм с ip: " + serverIp);
        stringData = fileManager.read();
        LinkedList<Dragon> dragons = new JsonParser().deSerialize(stringData);
        collectionManager.initialiseData(dragons);
        logger.info("Коллекция инициализирована. Готова к приему данных.");
    }
}