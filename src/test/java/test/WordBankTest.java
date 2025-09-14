package test;

import model.WordBank;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class WordBankTest {

    /*
    Выбирается категория слова, создается WordBank с Random и seed для него
     */
    @Test
    public void shouldGiveAWord() {
        Long SEED = 12345678L;
        WordBank wordBank = new WordBank(new Random(SEED));
        String word = wordBank.getWord(3);

        Assertions.assertEquals("europe", word);
    }

}
