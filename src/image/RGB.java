package image;

public class RGB {

    private final int[] rgb;

    public RGB(int pixel) {
        this.rgb = new int[3];
        this.rgb[0] = (pixel >> 16) & 0xff;
        this.rgb[1] = (pixel >> 8) & 0xff;
        this.rgb[2] = (pixel) & 0xff;
    }

    public int getRed() {
        return rgb[0];
    }

    public int getGreen() {
        return rgb[1];
    }

    public int getBlue() {
        return rgb[2];
    }

    public int[] getRgb() {
        return rgb;
    }

}
