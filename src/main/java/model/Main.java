package model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {

    public static void main(String[] args) throws IllegalInputFormatException {

        Game game = new Game();
        game.start();

    }
}
