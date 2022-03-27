package settings;

import java.io.File;

public class Settings {

    public static final File TESSERACT_TRAINED_DATA = new File("./resources/tesseract_data/");
    public static final int TESSERACT_DPI = 300;

    public static final int ROBOT_DELAY = 10;
    public static final int ROBOT_START_DELAY = 5000;
    public static final int ROBOT_LEFT = 37;
    public static final int ROBOT_RIGHT = 39;
    public static final int ROBOT_DOWN = 40;

    public static final File BOARD_IMAGE = new File("./resources/image/board.png");

}
