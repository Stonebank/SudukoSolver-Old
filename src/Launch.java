import board.SudukoBoard;
import board.mode.Mode;
import image.tesseract.ImageRecognition;
import image.trayicon.Notification;
import robot.Typer;
import settings.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Launch {

    public static void main(String[] args) throws AWTException, IOException, URISyntaxException {

        // initiate new instance of ImageRecognition class
        ImageRecognition imageRecognition = new ImageRecognition(Settings.BOARD_IMAGE);

        // initiate new instance of the sudoku board algorithm
        SudukoBoard board = new SudukoBoard(imageRecognition.getBoard(), Settings.SUDOKU_BOARD_SIZE);

        // initiate new instance of Typer class
        Typer typer = new Typer(board);

        // allows user to choose which mode the application should run
        typer.initiateMode();

        // if the selected mode is "TAKE_SCREENSHOT", the machine will attempt to open the default browser and take a screenshot.
        if (Settings.MODE == Mode.TAKE_SCREENSHOT) {

            // allows the user to pick difficulty
            typer.initiateDifficulty();

            // check if the application can open www.sudoku.com in the default browser
            if (!imageRecognition.canOpenBrowser()) {
                System.err.println(Mode.TAKE_SCREENSHOT + " is not supported on your machine.");
                Notification.send(new Notification(Mode.TAKE_SCREENSHOT + " is not supported on your machine."));
                return;
            }

            Notification.send(new Notification("You have selected " + Settings.MODE + "."));

            // open www.sudoku.com if supported
            imageRecognition.openBrowser("https://sudoku.com/" + typer.getGameMode());

            // take screenshot
            imageRecognition.takeScreenshot();

            // check if any matches occurs by comparing RGB pixel by pixel
            imageRecognition.match(ImageIO.read(Settings.SUDOKU_SCREENSHOT), ImageIO.read(Settings.SUDOKU_TOP_IMAGE));
            if (imageRecognition.noMatch()) {
                System.err.println("There were no RGB matches detected for the sudoku table. Try mode: " + Mode.HAS_SCREENSHOT + " after saving a screenshot in " + Settings.BOARD_IMAGE);
                Notification.send(new Notification("There were no RGB matches detected for the sudoku table. Try mode: " + Mode.HAS_SCREENSHOT + " after saving a screenshot in " + Settings.BOARD_IMAGE));
                System.exit(0);
                return;
            }

            System.out.println("A total of " + imageRecognition.getCoordinate().size() + " matches was found.");

            // crop the board now that we know top left x and y, we can calculate bottom right coordinates
            imageRecognition.cropBoard();

        }

        // crop each of the individual cells on the sudoku board
        imageRecognition.cropColumns();

        // read each of the individual cell with image.tesseract
        imageRecognition.read();

        // initiate the class
        typer.initiate();

    }

}
