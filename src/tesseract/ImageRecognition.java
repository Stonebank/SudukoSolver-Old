package tesseract;

import board.SudukoBoard;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import settings.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageRecognition {

    public static void main(String[] args) throws IOException {
        ImageRecognition imageRecognition = new ImageRecognition(new File("./resources/image/board.png"));

        int y2 = imageRecognition.getImage().getHeight() / 9;
        int y1 = 1;

        for (int i = 1; i <= 9; i++) {
            imageRecognition.crop(imageRecognition.getImage(), i, y1, imageRecognition.getImage().getWidth() - 1, y2);
            y1 += imageRecognition.getImage().getHeight() / 9;
        }

        imageRecognition.read();

    }

    private final BufferedImage image;

    // temp
    private final int[][] board = new int[9][9];
    private final int[][] current_board = new int[][] {
            { 9, 1, 3, 4, 2, 7, 0, 8, 0 },
            { 6, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 2, 0, 0, 0, 0, 3, 0, 7, 0 },
            { 0, 0, 0, 1, 0, 2, 0, 0, 8 },
            { 0, 6, 2, 5, 0, 0, 0, 0, 3 },
            { 5, 3, 8, 7, 0, 0, 2, 9, 0 },
            { 3, 4, 0, 8, 7, 0, 0, 6, 0 },
            { 0, 0, 6, 0, 4, 9, 8, 1, 5 },
            { 8, 0, 1, 2, 0, 0, 0, 0, 0 }
    };

    private void correctBoard() {
        int mistake = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (current_board[i][j] != board[i][j]) {
                    board[i][j] = current_board[i][j];
                    mistake++;
                }
            }
        }
        System.out.println("The board has " + (board.length * board.length) + " elements in which " + ((double) mistake / (board.length * board.length) * 100) + "% were mistakes.");
        System.out.println();
    }

    private final Tesseract tesseract;

    public ImageRecognition(File file) throws IOException {

        System.out.println("Reading image: " + file.getPath() + "...");

        if (!file.exists())
           throw new FileNotFoundException(file.getPath() + " was not found.");

        image = ImageIO.read(file);

        System.out.println("Image read: " + file.getPath());
        System.out.println("Initiating tesseract....");

        this.tesseract = new Tesseract();
        tesseract.setDatapath("./resources/tesseract_data/");
        tesseract.setTessVariable("user_defined_dpi", "300");

        System.out.println("tesseract initiated!");
        System.out.println("user_defined_dpi set to: " + Settings.TESSERACT_DPI + "dpi");

    }

    public void read() {

        try {

            int col = 0;

            for (int row = 1; row <= 9; row++) {
                File rowFile = new File("./resources/image/board_" + row + ".png");
                String rowOCR = tesseract.doOCR(rowFile);

                for (char c : rowOCR.replaceAll("[^\\d]", "").toCharArray()) {
                    board[row - 1][col] = Character.getNumericValue(c);
                    col++;
                    if (col > 8)
                        col = 0;
                }

            }

            SudukoBoard sudukoBoard = new SudukoBoard(board, 9);
            sudukoBoard.displayBoard();

            correctBoard();

            sudukoBoard.displayBoard();

            System.out.println("solving....");
            if (sudukoBoard.canSolve())
                sudukoBoard.displayBoard();

        } catch (TesseractException e) {
            System.err.println("Tesseract could not perform OCR.");
            e.printStackTrace();
        }
    }

    public void crop(BufferedImage source, int row, int startY, int endX, int endY) {
        BufferedImage img = source.getSubimage(1, startY, endX, endY);

        Graphics g = img.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        try {
            String name = "./resources/image/board_" + row + ".png";
            System.out.println("Cropping (" + row + ", " + startY + ", " + endX + ", " + endY + "): " + name);
            ImageIO.write(img, "png", new File(name));
        } catch (IOException e) {
            System.err.println("Image could not be written.");
            e.printStackTrace();
        }

    }

    public BufferedImage getImage() {
        return image;
    }

}
