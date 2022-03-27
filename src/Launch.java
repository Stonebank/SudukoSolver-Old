import board.SudukoBoard;
import robot.Typer;

import java.awt.*;

public class Launch {

    private static final int[][] BOARD = new int[][] {
            { 0, 0, 1, 7, 6, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 8, 0, 2, 5, 0 },
            { 8, 2, 0, 4, 0, 9, 0, 1, 0 },
            { 0, 9, 0, 5, 1, 0, 0, 3, 0 },
            { 2, 1, 0, 0, 3, 6, 7, 0, 0 },
            { 0, 5, 0, 8, 0, 4, 0, 9, 0 },
            { 9, 6, 0, 0, 0, 0, 0, 0, 8 },
            { 1, 0, 5, 6, 7, 0, 4, 0, 0 },
            { 0, 7, 4, 2, 9, 0, 5, 0, 1 }
    };

    private final static int SIZE = 9;

    public static void main(String[] args) throws AWTException {
        SudukoBoard board = new SudukoBoard(BOARD, SIZE);
        new Typer(board);
    }

}
