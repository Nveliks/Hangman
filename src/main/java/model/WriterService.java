package model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

/*
Класс WriterService включает в себя enum с текстовыми сообщениями для игрока
 */

public class WriterService {
    private static final int AVAILIBLEMISTAKESEASY = 6;
    private static final int AVAILIBLEMISTAKESMEDIUM = 3;
    private static final int AVAILIBLEMISTAKESHARD = 2;
    private static final int TRESHHOLDHINT = 2;
    private static final int INCREASEGALLOWSHARD = 3;
    private static final int INCREASEGALLOWSMEDIUM = 2;
    private static final int INCREASEGALLOWSEASY = 1;
    private static final int TRESHHOLDHARDLEVEL = 4;
    private static final int TRESHHOLDMEDIUMLEVEL = 5;
    private static final int TRESHHOLDEASYLEVEL = 6;
    private static final int GALLOWSFIRST = 1;
    private static final int GALLOWSSECOND = 2;
    private static final int GALLOWSTHIRD = 3;
    private static final int GALLOWSFOURTH = 4;
    private static final int GALLOWSFIFTH = 5;
    private static final int GALLOWSSIXTH = 6;
    public final StringBuilder word;
    private final int numOfAvailibleMistakes;
    public int hangLevel;
    public StringBuilder userWord;
    public PictureStorage gallows = PictureStorage.ZERO;
    public PrintStream writer;
    HashMap<Character, List<Integer>> wordByChars;
    private final ArrayList<String> lettersUsed = new ArrayList<String>();
    @Getter private int currentMistakes = 0;

    public WriterService(String word, int numOfAvailibleMistakes, PrintStream printStream) {
        this.numOfAvailibleMistakes = numOfAvailibleMistakes;
        this.word = new StringBuilder().append(word);
        this.writer = printStream;
        maskWord();
        startUserWord(word.length());
    }

    /*
    endGamePic выводит окончание игры в зависимости от исхода
     */
    public void endGamePic() {
        updateGallows(hangLevel);
        writer.println(gallows.getStage());
        writer.println("The answer is " + word);
        if (hangLevel == Game.NumberOfMistakes.SIX.availableMistakes()) {
            MessengersForUsers.LOSE.getText(writer);
        } else {
            MessengersForUsers.WIN.getText(writer);
        }
    }

    /*
    maskWord для маскировки слова по буквам
    */
    private void maskWord() {
        wordByChars = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            wordByChars.putIfAbsent(letter, new ArrayList<>());
            wordByChars.get(letter).add(i);
        }
    }

    /*
    Cоздает видимость слова для пользователя в начале игры
     */
    private void startUserWord(int len) {
        userWord = new StringBuilder();
        userWord.append("_".repeat(len));
    }

    /*
    updateHangLevel производит апдейт состояния изображения виселицы в зависимости от сложности,
    также ведет подсчет произведенных ошибок
     */
    public void updateHangLevel(int numOfAvailibleMistales) {

        currentMistakes++;
        switch (numOfAvailibleMistales) {
            case (AVAILIBLEMISTAKESEASY):
                if (hangLevel < TRESHHOLDEASYLEVEL) {
                    hangLevel += INCREASEGALLOWSEASY;
                }
                break;
            case (AVAILIBLEMISTAKESMEDIUM):
                if (hangLevel < TRESHHOLDMEDIUMLEVEL) {
                    hangLevel += INCREASEGALLOWSMEDIUM;
                }
                break;
            case (AVAILIBLEMISTAKESHARD):
                if (hangLevel < TRESHHOLDHARDLEVEL) {
                    hangLevel += INCREASEGALLOWSHARD;
                }
                break;
            default:
        }
    }

    /*
    acceptLetter осуществляет вывод состояния игры на текущий момент перед вводом слова игрока
     */
    public void acceptLetter() {
        int currentAvailibleMistakes = numOfAvailibleMistakes - currentMistakes;
        writer.println("You can make " + currentAvailibleMistakes + " mistakes");

        writer.println(gallows.getStage());
        if (currentAvailibleMistakes < TRESHHOLDHINT) {
            writer.println("  The hint:\n" + WordBank.getHint(word.toString()));
        }
        writer.println("Guess a word:\n   " + userWord.toString());
        MessengersForUsers.USERGUESS.getText(writer);
    }

    /*
acceptLetter Выводит всю доступную информацию пользователю о его состоянии игры
     */

    public void updateStage(char character) {

        String letter = String.valueOf(character).toLowerCase();
        if (!lettersUsed.contains(letter)) {
            if (word.indexOf(letter) != -1 && userWord.indexOf(letter) == -1) {
                for (int i : wordByChars.get(Character.toLowerCase(character))) {
                    userWord.replace(i, i + 1, letter);
                }
            } else {
                updateHangLevel(numOfAvailibleMistakes);
                updateGallows(hangLevel);
            }
        } else {
            MessengersForUsers.LETTERUSED.getText(writer);
        }
        lettersUsed.add(letter);
    }


/*
updateStage производит апдейт userWord или виселицы  в зависимости от введной буквой игроком
 */

    /*
    updateGallows do update gallows
     */
    public void updateGallows(int hangLevel) {
        switch (hangLevel) {
            case (GALLOWSFIRST):
                gallows = PictureStorage.FIRST;
                break;

            case (GALLOWSSECOND):
                gallows = PictureStorage.SECOND;
                break;

            case (GALLOWSTHIRD):
                gallows = PictureStorage.THIRD;
                break;

            case (GALLOWSFOURTH):
                gallows = PictureStorage.FOURTH;
                break;

            case (GALLOWSFIFTH):
                gallows = PictureStorage.FIFTH;
                break;

            case (GALLOWSSIXTH):
                gallows = PictureStorage.SIXTH;
                break;

            default:
        }
    }

    public enum MessengersForUsers {
        START("""
                   Hello it's a hangman game
             to start the game choose category of word
            """),

        CATEGORY("""
             print number to choose category\s
             1 - fruits
             2 - sports
             3 - geography \
            """),

        WRONGCATEGORY("You print wrong symbol\n Try to choose category again"),

        LEVEL("print number to choose game level print\n 1 - easy\n 2 - medium\n 3 - hard "),

        WRONGLEVEL("You print wrong symbol\n Try to choose game level again"),

        USERGUESS("Enter a letter"),

        WRONGUSERGUESS("You print wrong symbol"),

        WIN("   You win!!!\nCongratilation!!!"),

        LOSE("     You lose"),

        LETTERUSED("This letter is used!\n   Try again");

        final String text;

        MessengersForUsers(String text) {
            this.text = text;
        }

        public void getText(PrintStream writer) {
            writer.println(this.text);
        }
    }
}
