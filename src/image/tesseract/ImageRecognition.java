package image.tesseract;

import image.RGB;
import image.trayicon.Notification;
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
import java.util.ArrayList;

public class ImageRecognition {

    private BufferedImage image;

    private final File imgFile;

    private final Tesseract tesseract;

    private final ArrayList<int[]> coordinate = new ArrayList<>();

    private final int[][] board = new int[9][9];

    public ImageRecognition(File file) throws IOException {

        System.out.println("Reading image: " + file.getPath() + "...");

        if (!file.exists())
           throw new FileNotFoundException(file.getPath() + " was not found.");

        this.imgFile = file;

        System.out.println("Image read: " + file.getPath());
        System.out.println("Initiating image.tesseract....");

        this.tesseract = new Tesseract();
        tesseract.setDatapath(Settings.TESSERACT_TRAINED_DATA.getPath());
        tesseract.setTessVariable("user_defined_dpi", String.valueOf(Settings.TESSERACT_DPI));
        tesseract.setPageSegMode(Settings.TESSERACT_PSM);
        tesseract.setOcrEngineMode(Settings.TESSERACT_OEM);

        System.out.println("image.tesseract initiated!");
        System.out.println("user_defined_dpi set to: " + Settings.TESSERACT_DPI + "dpi");
        System.out.println("page_seg_mode set to: " + Settings.TESSERACT_PSM);
        System.out.println("ocr_engine_mode set to: " + Settings.TESSERACT_OEM);

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

    public void cropBoard() {
        int x = coordinate.get(0)[0];
        int y = coordinate.get(0)[1];
        int width = coordinate.get(coordinate.size() - 1)[0] - x;
        int height = coordinate.get(coordinate.size() - 1)[1] - y;
        System.out.println("Cropping board...");

        try {
            crop(ImageIO.read(Settings.SUDOKU_SCREENSHOT), "board", x, y, width, height);
            image = ImageIO.read(imgFile);
        } catch (IOException e) {
            System.err.println("Fault while cropping board");
            e.printStackTrace();
        }

    }

    public void cropColumns() {
        int x = 2;
        int y = 2;
        int width = 55 - x;
        int height = 55 - y;
        for (int i = 1; i <= 81; i++) {
            crop(getImage(), "board_" + i, x, y, width,  height);
            x += 55;
            if (i % 9 == 0) {
                x = 0;
                y += 55;
            }
        }
    }

    private void crop(BufferedImage source, String imgName, int x, int y, int width, int height) {
        BufferedImage img = source.getSubimage(x, y, width, height);

        Graphics g = img.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        try {
            String name = "./resources/image/" + imgName + ".png";
            if (Settings.DEBUG)
                System.out.println("Cropping (" + imgName + ", " + x + ", " + y + ", " + width + ", " + height + "): " + name);
            ImageIO.write(img, "png", new File(name));
        } catch (IOException e) {
            System.err.println("Image could not be written.");
            e.printStackTrace(); {

            }
        }

    }

    public void takeScreenshot() throws IOException {

        System.out.println("Attempting to take a screenshot in " + Settings.SCREENSHOT_DELAY + " ms...");
        Notification.send(new Notification("Attempting to take a screenshot in " + Settings.SCREENSHOT_DELAY + " ms..."));

        try {

            Thread.sleep(Settings.SCREENSHOT_DELAY);

            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            ImageIO.write(new Robot().createScreenCapture(screenRect), "png", Settings.SUDOKU_SCREENSHOT);

            System.out.println("Screenshot successfully snapped");
            Notification.send(new Notification("Screenshot successfully snapped"));

        } catch (InterruptedException | AWTException | IOException e) {
            System.err.println("Screenshot has failed to be captured.");
            e.printStackTrace();
        }

    }

    public void match(BufferedImage mainImage, BufferedImage subImage) {
        RGB sub_rgb = new RGB(subImage.getRGB(Settings.SUDOKU_TOP_COORDINATE[0], Settings.SUDOKU_TOP_COORDINATE[1]));
        for (int x = 0; x < mainImage.getWidth(); x++) {
            for (int y = 0; y < mainImage.getHeight(); y++) {
                RGB main_rgb = new RGB(mainImage.getRGB(x, y));
                if (hasMatch(main_rgb, sub_rgb))
                    coordinate.add(new int[] { x, y } );
            }
        }
    }


    private boolean hasMatch(RGB main_rgb, RGB sub_rgb) {
        return main_rgb.getRed() == sub_rgb.getRed() && main_rgb.getGreen() == sub_rgb.getGreen() && main_rgb.getBlue() == sub_rgb.getBlue();
    }

    public boolean noMatch() {
        return coordinate.isEmpty();
    }

    public ArrayList<int[]> getCoordinate() {
        return coordinate;
    }

    public void openBrowser(String url) throws URISyntaxException, IOException {
        String os_name = System.getProperty("os.name").toLowerCase();
        if (os_name.startsWith("windows")) {
            Desktop.getDesktop().browse(new URI(url));
            return;
        }
        if (os_name.startsWith("mac")) {
            Runtime.getRuntime().exec("open " + url);
            return;
        }
        System.err.println("Fault at detecting or not supported os... " + "(" + os_name + ")");
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
