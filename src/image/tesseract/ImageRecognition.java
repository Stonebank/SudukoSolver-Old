package image.tesseract;

import image.RGB;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import settings.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class ImageRecognition {

    private final BufferedImage image;

    private final Tesseract tesseract;

    private final int[][] board = new int[9][9];

    public ImageRecognition(File file) throws IOException {

        System.out.println("Reading image: " + file.getPath() + "...");

        if (!file.exists())
           throw new FileNotFoundException(file.getPath() + " was not found.");

        image = ImageIO.read(file);

        System.out.println("Image read: " + file.getPath());
        System.out.println("Initiating image.tesseract....");

        this.tesseract = new Tesseract();
        tesseract.setDatapath(Settings.TESSERACT_TRAINED_DATA.getPath());
        tesseract.setTessVariable("user_defined_dpi", String.valueOf(Settings.TESSERACT_DPI));
        tesseract.setPageSegMode(Settings.TESSERACT_PSM);

        System.out.println("image.tesseract initiated!");
        System.out.println("user_defined_dpi set to: " + Settings.TESSERACT_DPI + "dpi");
        System.out.println("page_seg_mode set to: " + Settings.TESSERACT_PSM);

    }

    public void read() {

        try {

            int col = 0;
            int rowIndex = 0;

            for (int row = 1; row <= 81; row++) {
                File rowFile = new File("./resources/image/board_" + row + ".png");
                String rowOCR = tesseract.doOCR(rowFile);

                for (String c : rowOCR.replaceAll("[^\\d\\s]", "").split("")) {
                    if (c.isEmpty() || c.isBlank())
                        c = "0";
                    board[rowIndex][col] = Integer.parseInt(c);
                    col++;
                    if (col > 8)
                        col = 0;
                    break;
                }

                if (row % 9 == 0)
                    rowIndex++;

            }

        } catch (TesseractException e) {
            System.err.println("Tesseract could not perform OCR.");
            e.printStackTrace();
        }
    }

    public void cropColumns() {
        int x1 = 4;
        int y1 = 2;
        for (int i = 1; i <= 81; i++) {
            crop(getImage(), i, x1, y1,50, 50);
            x1 += 55;
            if (i % 9 == 0) {
                x1 = 0;
                y1 += 55;
            }
        }
    }

    private void crop(BufferedImage source, int row, int x, int y, int width, int height) {
        BufferedImage img = source.getSubimage(x, y, width, height);

        Graphics g = img.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        try {
            String name = "./resources/image/board_" + row + ".png";
            if (Settings.DEBUG)
                System.out.println("Cropping (" + row + ", " + x + ", " + y + ", " + width + ", " + height + "): " + name);
            ImageIO.write(img, "png", new File(name));
        } catch (IOException e) {
            System.err.println("Image could not be written.");
            e.printStackTrace(); {

            }
        }

    }

    public void takeScreenshot() {

        System.out.println("Attempting to take a screenshot...");

        try {

            Thread.sleep(Settings.SCREENSHOT_DELAY);

            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            ImageIO.write(new Robot().createScreenCapture(screenRect), "png", Settings.SUDOKU_SCREENSHOT);

            System.out.println("Screenshot taken");

        } catch (InterruptedException | AWTException | IOException e) {
            System.err.println("Screenshot has failed to be captured.");
            e.printStackTrace();
        }

    }

    public int[] match(BufferedImage mainImage, BufferedImage subImage) {
        RGB sub_rgb = new RGB(subImage.getRGB(Settings.SUDOKU_TOP_COORDINATE[0], Settings.SUDOKU_TOP_COORDINATE[1]));
        for (int x = 0; x < mainImage.getWidth(); x++) {
            for (int y = 0; y < mainImage.getHeight(); y++) {
                RGB main_rgb = new RGB(mainImage.getRGB(x, y));
                if (hasMatch(main_rgb, sub_rgb))
                    return new int[] { x, y };
            }
        }
        return null;
    }

    private boolean hasMatch(RGB main_rgb, RGB sub_rgb) {
        return main_rgb.getRed() == sub_rgb.getRed() && main_rgb.getGreen() == sub_rgb.getGreen() && main_rgb.getBlue() == sub_rgb.getBlue();
    }

    public void openBrowser() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("www.sudoku.com"));
    }

    public boolean canOpenBrowser() {
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
    }

    public BufferedImage getImage() {
        return image;
    }

    public int[][] getBoard() {
        return board;
    }

}
