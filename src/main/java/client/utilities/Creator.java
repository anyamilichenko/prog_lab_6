package client.utilities;

import common.data.Coordinates;
import common.data.Dragon;
import common.data.DragonCave;
import common.data.DragonCharacter;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;

public class Creator {
    private static final String ERROR_MESSAGE = "Your enter was not correct type. Try again";
    private final OutputManager outputManager;
    private final Asker asker;

    public Creator(InputManager inputManager, OutputManager outputManager) {
        this.outputManager = outputManager;
        this.asker = new Asker(inputManager, outputManager);
    }

    public Dragon makeDragon() throws IOException {
        return askForDragon();
    }

    @NotNull
    @Contract(" -> new")
    private Dragon askForDragon() throws IOException {
        outputManager.println("Введите данные о драконе");
        String name = asker.ask(arg -> (arg).length() > 0, "Введите имя (String)", //Поле не может быть null, Строка не может быть пустой
                ERROR_MESSAGE, "Строка не должна быть пустой", x -> x, false);
        Coordinates coordinates = askForCoordinates(); //not null

        long age = asker.ask(arg -> (arg) > 0, "Введите возраст дракона (long) (Может быть пустым)",
                ERROR_MESSAGE, "Введенное значение должно быть больше 0. Попробуй снова", Long::parseLong, true); // >0

        DragonCharacter dragonCharacter = asker.ask(arg -> (arg).length() > 0, "Enter semesterEnum (CUNNING, EVIL, GOOD, CHAOTIC, CHAOTIC_EVIL) (не может быть нулевым)",
                ERROR_MESSAGE, ERROR_MESSAGE, DragonCharacter::valueOf, true); // null-able
        DragonCave dragonCave = askForDragonCave();

        Integer wingspan = asker.ask(arg -> (arg) > 0.length() > 0, "Введите размах крыльев дракона (Integer) (Не может быть пустым)", ERROR_MESSAGE, "Строка не должна быть пустой,а значение не должно" +
                "быть меньше нуля", Integer::parseInt, true );

        Double weight = asker.ask(arg -> 0.length() > 0, "Введите размах крыльев дракона (Double) (Не может быть пустым)", ERROR_MESSAGE, "Строка не должна быть пустой,а значение не должно" +
                "быть меньше нуля", Double::parseDouble, true);

        return new Dragon(name, coordinates, age, dragonCharacter, dragonCave, wingspan, weight);
    }



    @@Contract(" -> new")
    private askForDragonCave() throws IOException{
        outputManager.println("Введите данные о пещере дракона");

        float numberOfTreasures = asker.ask(arg -> (arg) > 0.0f, "Введите количество сокровищ в пещере дракона (float) (Может быть пустым)",
                ERROR_MESSAGE, "Введенное значение должно быть больше 0. Попробуй снова", Float::parseFloat, true); // >0

        Double depth = asker.ask(arg -> (arg), "Введите глубину пещеры дракона (Double) (Может быть пустым)", ERROR_MESSAGE, "", Double::parseDouble, true);

        return new DragonCave(depth, numberOfTreasures);
    }


    @@Contract(" -> new")
    private @NotNull Coordinates askForCoordinates() throws IOException {
        outputManager.println("Enter coordinates data");
        final double yLimitation = -513;
        Double x = asker.ask(arg -> true, "Введите значение х (Double)", ERROR_MESSAGE,
                ERROR_MESSAGE, Double::parseDouble, false);
        Double y = asker.ask(arg -> (arg) <= yLimitation, "Введите значение y (Double)",
                ERROR_MESSAGE, "Значение поля должно быть больше -513. Попробуй снова", Double::parseDouble, false); //Значение поля должно быть больше -513, Поле не может быть null
        return new Coordinates(x, y);
    }



    public static class Asker {
        private final InputManager inputManager;
        private final OutputManager outputManager;


        public Asker(InputManager inputManager, OutputManager outputManager) {
            this.inputManager = inputManager;
            this.outputManager = outputManager;
        }

        public <T> T ask(Predicate<T> predicate,
                         String askMessage,
                         String errorMessage,
                         String wrongValueMessage,
                         Function<String, T> converter,
                         boolean nullable) {
            outputManager.println(askMessage);
            String input;
            T value;
            do {
                try {
                    input = inputManager.nextLine();
                    if ("".equals(input) && nullable) {
                        return null;
                    }

                    value = converter.apply(input);

                } catch (IllegalArgumentException | IOException e) {
                    outputManager.println(errorMessage);
                    continue;
                }
                if (predicate.test(value)) {
                    return value;
                } else {
                    outputManager.println(wrongValueMessage);
                }
            } while (true);
        }
    }
}