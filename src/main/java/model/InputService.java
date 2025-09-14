package model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Scanner;

/*
Класс InputService осуществляет чтение входных данных на всех стадиях игры (выбора категории, уровня сложности
и угадывание букв)
 */
public class InputService {
    private static final int NUMBEROFCATEGORYS = 3;
    public Scanner charReader;
    SecureRandom randomGenerator = new SecureRandom();
    PrintStream writer;

    public InputService(InputStream inputStream, PrintStream printStream) {
        this.writer = printStream;
        charReader = new Scanner(inputStream, StandardCharsets.UTF_8);

    }

    public InputService(File file, PrintStream printStream) throws IOException {
        this.writer = printStream;
        charReader = new Scanner(file, StandardCharsets.UTF_8);
    }

    public InputService(PrintStream printStream) {
        this.writer = printStream;
        charReader = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public String userInput() {
        return charReader.nextLine();
    }

    /*
    readUserGuess обрабатывает введенную строку игроком во время угадывания букв, и возвращает правильно введенную букву
     */
    public char readUserGuess(String userLetters) throws IllegalInputFormatException {

        if (userLetters.length() == 1 && userLetters.matches("[a-zA-Z]+")) {
            return userLetters.charAt(0);
        } else {
            throw new IllegalInputFormatException();
        }
    }

    /*
    categoryInput  принимает количество категорий в банке со словами и считывает выбор категории игрока,
     возвращает номер категории слов
     */
    public int categoryInput(int size) {
        WriterService.MessengersForUsers.CATEGORY.getText(writer);
        String inputC = userInput();
        if (inputC.isEmpty()) {
            return randomGenerator.nextInt(size); //Random seed
        } else {
            try {
                int numOfCategory = Integer.parseInt(inputC);
                if (numOfCategory > 0 && numOfCategory <= size) {
                    return numOfCategory;
                } else {
                    WriterService.MessengersForUsers.WRONGCATEGORY.getText(writer);
                    return categoryInput(size);
                }
            } catch (NumberFormatException e) {
                WriterService.MessengersForUsers.WRONGCATEGORY.getText(writer);
                return categoryInput(size);
            }
        }
    }

    /*
    levelInput осуществляет считывание с консоли номер уровня сложности и при его корректности возвращает его
     */
    public int levelInput() {
        WriterService.MessengersForUsers.LEVEL.getText(writer);
        String inputL = userInput();
        if (inputL.isEmpty()) {
            return 1; //Random seed
        } else {
            try {
                int numOfLevel = Integer.parseInt(inputL);
                if (numOfLevel > 0 && numOfLevel <= NUMBEROFCATEGORYS) {
                    return numOfLevel;
                } else {
                    WriterService.MessengersForUsers.WRONGLEVEL.getText(writer);
                    return levelInput();
                }
            } catch (NumberFormatException e) {
                WriterService.MessengersForUsers.WRONGLEVEL.getText(writer);
                return levelInput();
            }
        }
    }
}






