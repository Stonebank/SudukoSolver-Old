import board.SudukoBoard;
import robot.Typer;
import settings.Settings;
import image.tesseract.ImageRecognition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Launch {

    public static void main(String[] args) throws AWTException, IOException, URISyntaxException {

        // initiate new instance of ImageRecognition class
        ImageRecognition imageRecognition = new ImageRecognition(Settings.BOARD_IMAGE);

        // check if the application can open www.sudoku.com in the default browser
        if (imageRecognition.canOpenBrowser()) {

            // open www.sudoku.com if supported
            imageRecognition.openBrowser();
            // take screenshot
            imageRecognition.takeScreenshot();

            int[] match = imageRecognition.match(ImageIO.read(Settings.SUDOKU_SCREENSHOT), ImageIO.read(Settings.SUDOKU_TOP_IMAGE));
            if (match != null)
                System.out.println("Match found at " + match[0] + ", " + match[1]);

        }

        // crop each of the individual cells on the sudoku board
        imageRecognition.cropColumns();

        // read each of the individual cell with image.tesseract
        imageRecognition.read();

        // initiate new instance of the sudoku board algorithm
        SudukoBoard board = new SudukoBoard(imageRecognition.getBoard(), Settings.SUDOKU_BOARD_SIZE);

        // initiate new instance of Typer class
        Typer typer = new Typer(board);

        // initiate the class
        typer.initiate();

    }

}
