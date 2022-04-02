package settings;

import java.io.File;

public class Settings {

    public static final boolean DEBUG = false;

    public static final File BOARD_IMAGE = new File("./resources/image/board.png");
    public static final int SUDOKU_BOARD_SIZE = 9;

    public static final File TESSERACT_TRAINED_DATA = new File("./resources/tesseract_data/");
    public static final int TESSERACT_DPI = 300;
    public static final int TESSERACT_PSM = 8;

    public static final File SUDOKU_TOP_IMAGE = new File("./resources/image/match/sudoku_top.png");
    public static final File SUDOKU_SCREENSHOT = new File("./resources/image/match/screenshot.png");
    public static final int[] SUDOKU_TOP_COORDINATE = new int[] { 7, 15 };
    public static final int SCREENSHOT_DELAY = 7500;

    public static final int ROBOT_DELAY = 10;
    public static final int ROBOT_START_DELAY = 5000;
    public static final int ROBOT_LEFT = 37;
    public static final int ROBOT_RIGHT = 39;
    public static final int ROBOT_DOWN = 40;

}
