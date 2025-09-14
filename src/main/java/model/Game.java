package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import lombok.Getter;

/*
Класс Game отвечает за всю игру
За генерацию слова и выбор уровня сложности отвечает метод startGame
За игрувой процесс отвечает gameProcess параметром является количество доступное ошибок,
которое зависит от уровня ложности
Игра продолжается пока не отгадают слово или пока не используются все доступные ошибки
*/

public class Game {

    private static final int USERGAMELEVELHARD = 3;
    private static final int USERGAMELEVELMEDIUM = 2;
    private final WordBank wordBank;
    private final InputService inputService;
    NumberOfMistakes numberOfMistakes;
    WriterService writerService;
    PrintStream writer;
    private String word;

    public Game(File fileIn, File fileOut, Random random) throws IOException {
        this.writer = new PrintStream(fileOut, StandardCharsets.UTF_8);
        this.inputService = new InputService(fileIn, writer);
        this.wordBank = (random == null) ? new WordBank() : new WordBank(random);
    }

    public Game(Random random) {
        this.inputService = new InputService(System.in, System.out);
        this.writer = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        this.wordBank = (random == null) ? new WordBank() : new WordBank(random);
    }

    public Game() {

        this(null);
    }
/*
В метод updateNumOfMistakes в качестве аргумента передается инт, который считывается сканером в начале игры
 для определения сложности
Этот метод определяет количество ошибок, которое допустимо на определенном уровне сложности
enum здесь не получается использовать, тк switch происходит по параметру метода(int)
 */

    public NumberOfMistakes updateNumOfMistakes(int hardLevel) throws IllegalInputFormatException {
        return switch (hardLevel) {
            case USERGAMELEVELMEDIUM -> NumberOfMistakes.THREE;
            case  USERGAMELEVELHARD -> NumberOfMistakes.TWO;
            default -> NumberOfMistakes.SIX;
        };
    }

    /*
    Метод состоит из 2 циклов while
    Внутренний цикл вайл представляет собой ввод буквы, и будет, пока letter не будет корректным
    Далее осуществляется проверка буквы, есть ли она в загаданном слове или нет
    После окончания внешнего цикла выводится финальный output в зависимости от исхода(win or lose)
     */
    public void gameProcess(int numAvailiblemistakes) throws IllegalInputFormatException {
        writerService = new WriterService(word, numAvailiblemistakes, writer);
        while (writerService.currentMistakes() < numAvailiblemistakes && writerService.userWord.indexOf("_") != -1) {
            writerService.acceptLetter();
            char letter;
            while (true) {
                try {
                    letter = inputService.readUserGuess(inputService.userInput());
                    break;
                } catch (IllegalInputFormatException e) {
                    WriterService.MessengersForUsers.WRONGUSERGUESS.getText(writerService.writer);
                    WriterService.MessengersForUsers.USERGUESS.getText(writerService.writer);
                }
            }
            writerService.updateStage(letter);
        }
        writerService.endGamePic();
    }

    public void start() throws IllegalInputFormatException {
        WriterService.MessengersForUsers.START.getText(writer);
        word = wordBank.getWord(inputService.categoryInput(wordBank.getSizeWords()));
        numberOfMistakes = updateNumOfMistakes(inputService.levelInput());
        gameProcess(numberOfMistakes.availableMistakes());
    }

    public enum NumberOfMistakes {
        TWO("You can make 2 mistakes", 2), THREE("You can make 3 mistakes", 3), SIX("You can make 6 mistakes", 6);

        public final String numMistakesText;
        @Getter public final int availableMistakes;

        NumberOfMistakes(String numMistakesText, int availableMistakes) {
            this.availableMistakes = availableMistakes;
            this.numMistakesText = numMistakesText;
        }

    }

}

