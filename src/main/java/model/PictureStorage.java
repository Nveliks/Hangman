package model;

/*
enum PictureStorage содержит все возможные состояния виселицы
 */
public enum PictureStorage {
    ZERO(""),
    FIRST("\n\n\n\n\n___"),
    SECOND("""
         |
         |
         |
         |
         |
        ___"""),
    THIRD("""
         |---------
         |        |
         |
         |
         |
        ___"""),
    FOURTH("""
         |---------
         |        |
         |        O
         |
         |
        ___"""),
    FIFTH("""
         |---------
         |        |
         |        O
         |       /|\\
         |
        ___"""),
    SIXTH("""
         |---------
         |        |
         |        O
         |       /|\\
         |       / \\
        ___""");
    private final String stage;



    PictureStorage(String stage) {
        this.stage = stage;
    }

    public String getStage() {
        return stage;
    }
}
