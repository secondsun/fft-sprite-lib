package dev.secondsun.fftspriteanimator;

import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.secondsun.fftspriteanimator.Util.getInt;

public class ShapeLoader {
    public static Shape loadSHP(InputStream stream, Rectangle rectangle) throws IOException {
        byte[] bytes2 = stream.readAllBytes();
        int[] bytes = new int[bytes2.length];
        for (int i = 0; i < bytes.length;i++) {
            bytes[i] = bytes2[i]&0x00ff;
        }


        Shape shape = new Shape(rectangle);
        int jump = (int) getInt(Arrays.copyOfRange(bytes, 0, 4));
        int secondHalf = (bytes[4] & 0x00FF) + (bytes[5] & 0x00FF) * 256;

        List<Integer> offsets = new ArrayList<>();
        offsets.add(0);
        Integer addy = 0;
        int i = 0;

        do {
            addy = getInt(Arrays.copyOfRange(bytes, 0x0C + 4 * i, 0x0C + 4 * i + 4));
            i++;
            if (addy != 0) {
                offsets.add(addy);
            }
        } while (addy != 0);

        shape.frames = new ArrayList<Frame>(offsets.size());
        for (i = 0; i < offsets.size(); i++) {
            shape.frames.add(new Frame(Arrays.copyOfRange(bytes, (offsets.get(i) + 0x40A), bytes.length), i >= secondHalf ? 256 : 0));
        }

        if (jump > 8) {
            offsets = new ArrayList<>();
            offsets.add(0);
            addy = 0;
            i = 0;
            do {
                addy = getInt(Arrays.copyOfRange(bytes, jump + 4 * i + 4, jump + 4 * i + 4 + 4));
                i++;
                if (addy != 0) {
                    offsets.add(addy);
                }
            } while (addy != 0);
        }
        for (i = 0; i < offsets.size(); i++) {
            shape.frames.add(new Frame(Arrays.copyOfRange(bytes, (offsets.get(i) + jump + 0x402), bytes.length), i >= secondHalf ? 256 : 0));
        }


        return shape;
    }
}
