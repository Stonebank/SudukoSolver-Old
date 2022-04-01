import board.SudukoBoard;
import robot.Typer;
import settings.Settings;
import tesseract.ImageRecognition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Launch {

    public static void main(String[] args) throws AWTException, IOException {

        // initiate new instance of ImageRecognition class
        ImageRecognition imageRecognition = new ImageRecognition(Settings.BOARD_IMAGE);

        // see if the table can be detected on screenshot
        Point match = imageRecognition.match(ImageIO.read(Settings.SUDOKU_SCREENSHOT), ImageIO.read(Settings.SUDOKU_TOP_IMAGE));
        if (match != null)
            System.out.println("Match found at " + match.getLocation());

        // crop each of the individual cells on the sudoku board
        imageRecognition.cropColumns();

        // read each of the individual cell with tesseract
        imageRecognition.read();

        // initiate new instance of the sudoku board algorithm
        SudukoBoard board = new SudukoBoard(imageRecognition.getBoard(), Settings.SUDOKU_BOARD_SIZE);

        // initiate new instance of Typer class
        Typer typer = new Typer(board);

        // initiate the class
        typer.initiate();

    }

}
