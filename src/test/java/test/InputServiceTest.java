package test;

import java.util.Random;
import model.Game;
import model.IllegalInputFormatException;
import model.InputService;
import model.PictureStorage;
import model.WordBank;
import model.WriterService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
class InputServiceTest {

    @Before public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private final Long SEED = 12345678L;
    @Test void shouldWorkWithANyRegistr() {
        Random random = mock(Random.class);
        when(random.nextInt(anyInt())).thenReturn(1);
        WordBank wordBank = new WordBank(random);
        String word = wordBank.getWord(2);
        WriterService writerService =
            new WriterService(word, Game.NumberOfMistakes.SIX.availableMistakes(), System.out);
        writerService.updateStage('E');
        writerService.updateStage('r');

        Assertions.assertEquals("____er", writerService.userWord.toString());
    }

    /*
При введении нескольких символов при вводе слова должен выпасть IllegalInputFormatException,
после ввода буквы д слово становится _____d_ gри вводе символов ro выпадает ошибка
     */

    @Test void shouldnAcceptFewSymbols() throws IllegalInputFormatException {
        WordBank wordBank = new WordBank(new Random(SEED));
        String word = wordBank.getWord(1);
        InputService inputService = new InputService(System.out);
        WriterService writerService =
            new WriterService(word, Game.NumberOfMistakes.SIX.availableMistakes(), System.out);
        writerService.updateStage(inputService.readUserGuess("d"));
        Assertions.assertEquals("_____d_", writerService.userWord.toString());
        Assertions.assertThrowsExactly(IllegalInputFormatException.class,
            () -> writerService.updateStage(inputService.readUserGuess("ro")));
    }

    /*
    За корректность отвечает userWord, отражающий текущее состояние отгаданного слова, а также состояние виселицы
    переменной gallows, после ввода e и r они появились в userWord, но f отсутствует в слове europe,
    поэтому gallows стал PictureStorage.SECOND, потому что уровень сложности MEDIUM (NumberOfMistakes.THREE.availableMistakes)
     */
    @Test void shouldCorrectStatus() throws IllegalInputFormatException {
        WordBank wordBank = new WordBank(new Random(SEED));
        String word = wordBank.getWord(3);
        InputService inputService = new InputService(System.out);
        WriterService writerService =
            new WriterService(word, Game.NumberOfMistakes.THREE.availableMistakes(), System.out);
        writerService.updateStage(inputService.readUserGuess("e"));
        writerService.updateStage(inputService.readUserGuess("r"));
        writerService.updateStage(inputService.readUserGuess("f"));
        writerService.updateGallows(writerService.hangLevel);

        Assertions.assertEquals("e_r__e", writerService.userWord.toString());
        Assertions.assertEquals(PictureStorage.SECOND, writerService.gallows);

    }

}
