package model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import lombok.Getter;

/*
Класс WordBank содержит Hashmap WORDS слов и HINTS метод возвращающий size WORDS и HINTS рандомное слово из массива
выбранной категории и подсказку
 */

public class WordBank {
    private static final HashMap<String, List<String>> WORDS = new HashMap<>();
    private static final HashMap<String, String> HINTS = new HashMap<>();
    private static final int CATEGORYFIRST = 1;
    private static final int CATEGORYSECOND = 2;
    private static final int CATEGORYTHIRD = 3;
    private final Random randomGenerator;

    public WordBank(Random random) {

        randomGenerator = random;
        createBank();
    }

    public WordBank() {
        randomGenerator = new SecureRandom();
        createBank();
    }

    public static String getHint(String word) {
        return HINTS.get(word);
    }

    private void fillWordsAndHints(String category, String word, String hint) {
        WORDS.putIfAbsent(category, new ArrayList<>());
        WORDS.get(category).add(word);
        HINTS.putIfAbsent(word, hint);
    }

    private void createBank() {
        fillWordsAndHints(Categories.FRUITS.category(), "orange", "Like mandarin");
        fillWordsAndHints(Categories.FRUITS.category(), "mango", "Dota 2");
        fillWordsAndHints(Categories.FRUITS.category(), "pineapple", "Fanta");
        fillWordsAndHints(Categories.FRUITS.category(), "avocado", "Green and healthy");
        fillWordsAndHints(Categories.FRUITS.category(), "apple", "Iphone");
        fillWordsAndHints(Categories.SPORTS.category(), "football", "American");
        fillWordsAndHints(Categories.SPORTS.category(), "soccer", "Messi");
        fillWordsAndHints(Categories.GEOGRAPHY.category(), "russia", "Our country");
        fillWordsAndHints(Categories.GEOGRAPHY.category(), "asia", "Part of the east world");
        fillWordsAndHints(Categories.GEOGRAPHY.category(), "europe", "Part of the west world");

    }

    public String getWord(int categoryInput) {

        String category;
        switch (categoryInput) {
            case (CATEGORYSECOND):
                category = Categories.SPORTS.category();
                break;
            case (CATEGORYTHIRD):
                category = Categories.GEOGRAPHY.category();
                break;
            case (CATEGORYFIRST):
                category = Categories.FRUITS.category();
                break;
            default:
                category = Categories.FRUITS.category();

        }
        int num = randomGenerator.nextInt(WORDS.get(category).size());
        return WORDS.get(category).get(num);
    }

    public int getSizeWords() {
        return WORDS.size();
    }

    @Getter public enum Categories {
        FRUITS("fruits"), GEOGRAPHY("geography"), SPORTS("sports");

        private final String category;

        Categories(String category) {
            this.category = category;
        }
    }

}
