package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Random;
import model.Game;
import model.IllegalInputFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameTest {
    private final Random SEED = new Random(12345678L);
    PrintStream out;

    private void deleteFile(String pathnameFileDelete) throws IOException {
        File fileOutput = new File(pathnameFileDelete);
        Files.deleteIfExists(fileOutput.toPath());
    }

    /*
    Введена при выборе категории сначала буква  r, была напечатана строчка "You print wrong symbol" 1 раз, далее введена категория 1
    уровень сложности был введен через whitespace, значит уровень easy и введены буквы слова avocado, с повторением буквы a
    уровень изи если игра предлагает выполнить 6 ошибок
     */
    @Test void ShouldChooseEasyLevelAndOverAfterFiveInputs() throws IOException, IllegalInputFormatException {
        deleteFile("outputwhitespaceLevelChose.txt");

        File fileInput = new File("src/test/resources/whitespaceLevelChose.txt");
        Game game = new Game(fileInput, new File("outputwhitespaceLevelChose.txt"), SEED);
        game.start();
        String line;
        boolean easyLevel = false;
        int counterEntering = 0;
        int counterWrongSymbol = 0;
        try (BufferedReader br = new BufferedReader(
            new FileReader("outputwhitespaceLevelChose.txt", StandardCharsets.UTF_8))) {

            while (!Objects.equals(line = br.readLine(), "   You win!!!")) {
                if (line.equals("Enter a letter")) {
                    counterEntering++;
                }
                if (line.equals("You print wrong symbol")) {
                    counterWrongSymbol++;
                }
                if (line.equals("You can make 6 mistakes")) {
                    easyLevel = true;
                }
            }
        }
        Assertions.assertEquals(6, counterEntering);
        Assertions.assertTrue(easyLevel);
        Assertions.assertEquals(1, counterWrongSymbol);
        Assertions.assertEquals("   You win!!!", line);
    }

    /*
    Уровень сложности hard и поэтому доступно только 2 ошибки в угадывании буквы
     */
    @Test void gameShouldOverAfter2Mistakes() throws IllegalInputFormatException, IOException {
        deleteFile("output2Mistakes.txt");

        File fileInput = new File("src/test/resources/twoMistakes.txt");
        Game game = new Game(fileInput, new File("output2Mistakes.txt"), SEED);
        game.start();
        String line;
        int counterEntering = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("output2Mistakes.txt", StandardCharsets.UTF_8))) {

            while (!Objects.equals(line = br.readLine(), "     You lose")) {
                if (line.equals("Enter a letter")) {
                    counterEntering++;
                }
            }
        }

        Assertions.assertEquals(2, counterEntering);
        Assertions.assertEquals("     You lose", line);
    }

    /*
    Уровень сложности medium и поэтому доступно только 3 ошибки в угадывании буквы
    6 раз запрашивали "Enter a letter" потому что в threeMistakes.txt файле есть 2 вайтспейса и 1 правильная буква,
     после 6 вводов игра заканчивается

     */
    @Test void gameShouldOverAfter3Mistakes() throws IllegalInputFormatException, IOException {
        deleteFile("output3Mistakes.txt");
        File fileInput = new File("src/test/resources/threeMistakes.txt");
        Game game = new Game(fileInput, new File("output3Mistakes.txt"), SEED);
        System.setOut(out);
        game.start();
        String line;
        int CounterEntering = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("output3Mistakes.txt", StandardCharsets.UTF_8))) {

            while (!Objects.equals(line = br.readLine(), "     You lose")) {
                if (line.equals("Enter a letter")) {
                    CounterEntering++;
                }
            }
        }

        Assertions.assertEquals(6, CounterEntering);
        Assertions.assertEquals("     You lose", line);
    }

    /*
    Выбирается категория и уровень сложности изи, вводятся неверные буквы и игра заканчивается поражением после 6 вводов
     */

    @Test void gameShouldOverAfter6Mistakes() throws IllegalInputFormatException, IOException {
        deleteFile("output6Mistakes.txt");
        File fileInput = new File("src/test/resources/sixMistakes.txt");
        Game game = new Game(fileInput, new File("output6Mistakes.txt"), SEED);
        System.setOut(out);
        game.start();
        String line;
        int counterEntering = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("output6Mistakes.txt", StandardCharsets.UTF_8))) {

            while (!Objects.equals(line = br.readLine(), "     You lose")) {
                if (line.equals("Enter a letter")) {
                    counterEntering++;
                }
            }
        }

        Assertions.assertEquals(6, counterEntering);
        Assertions.assertEquals("     You lose", line);
    }

    /*
    Выбирается категория, уровень сложности и вводятся 5 правильных букв, после игра заканчивается победой
     */

    @Test void gameShouldOverAfterFiveInputLetter() throws IllegalInputFormatException, IOException {
        deleteFile("outputWinNoMistakes.txt");
        File fileInput = new File("src/test/resources/WinWithNoMistakes.txt");
        Game game = new Game(fileInput, new File("outputWinNoMistakes.txt"), SEED);
        System.setOut(out);
        game.start();
        String line;
        int counterEntering = 0;
        try (
            BufferedReader br = new BufferedReader(new FileReader("outputWinNoMistakes.txt", StandardCharsets.UTF_8))) {
            while (!Objects.equals(line = br.readLine(), "   You win!!!")) {
                if (line.equals("Enter a letter")) {
                    counterEntering++;
                }
            }
        }
        Assertions.assertEquals(5, counterEntering);
        Assertions.assertEquals("   You win!!!", line);
    }
}
