package dev.secondsun.fftspriteanimator;

import java.awt.*;
import java.util.List;

public class Tile {
    private static List<Size> sizes;
    public Point location;
    public Rectangle rectangle;

    public boolean reverseX;
    public boolean reverseY;

    public float Rotation;

    static {
        sizes = List.of(
                new Size(8, 8),
                new Size(16, 8),
                new Size(16, 16),
                new Size(16, 24),
                new Size(24, 8),
                new Size(24, 16),
                new Size(24, 24),
                new Size(32, 8),
                new Size(32, 16),
                new Size(32, 24),
                new Size(32, 32),
                new Size(32, 40),
                new Size(48, 16),
                new Size(40, 32),
                new Size(48, 48),
                new Size(56, 56));
    }

    public Tile(int[] bytes, int yOffset, int rotation) {
        Rotation = (float) (rotation * 1.40625);
        int xByte = 0xFF&bytes[0];
        int yByte = 0xFF&bytes[1];
        byte x = (byte)(0xFF&xByte);
        byte y =  (byte)(0xFF&yByte);

        int flags = 0xFFFF& (bytes[2] + bytes[3] * 256);
        reverseX = (flags & 0x4000) == 0x4000;
        reverseY = (flags & 0x8000) == 0x8000;
        int f = (0xFF)& ((flags >> 10) & 0x0F);
        int tileX = (flags & 0x1F) * 8;
        int tileY = ((flags >> 5) & 0x1F) * 8 + yOffset;

        location = new Point(x + 53, y + 118);

        rectangle = new Rectangle(new Point(tileX, tileY), new Dimension(sizes.get(f).width(), sizes.get(f).height()));
    }

    @Override
    public String toString() {
        return "Tile{" +
                "location=" + location +
                ", rectangle=" + rectangle +
                ", reverseX=" + reverseX +
                ", reverseY=" + reverseY +
                ", Rotation=" + Rotation +
                '}';
    }
}

