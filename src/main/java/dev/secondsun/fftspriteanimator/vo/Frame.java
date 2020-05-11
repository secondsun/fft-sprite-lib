package dev.secondsun.fftspriteanimator.vo;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;

public class Frame {
    public static final Size defaultFrameSize = new Size(185, 250);

    private List<Tile> tiles;

    public Frame(int[] bytes, int yOffset) {
        int rotation = 0;
        int ydisplayoffset = 0;
        int numberOfTiles = (bytes[0]&0x00FF) + (bytes[1]&0x00FF) * 256;

        tiles = new ArrayList<Tile>(numberOfTiles + 1);

        for (int i = 0; i <= numberOfTiles; i++) {
            tiles.add(new Tile(Arrays.copyOfRange(bytes, 2 + i * 4, 2 + i * 4 + 4), yOffset, rotation));

        }

        Collections.reverse(tiles);
    }

    public BufferedImage getFrame(BufferedImage source) {
        return getFrame(source, 0);
    }

    public BufferedImage getFrame(BufferedImage source, int paletteIndex) {
        BufferedImage result = new BufferedImage(defaultFrameSize.width(), defaultFrameSize.height(), BufferedImage.TYPE_INT_ARGB);
        System.out.println("-----New Frame----");
        for (Tile t : tiles) {
            System.out.println(t.toString());
            AffineTransform at = new AffineTransform();
            at.concatenate(AffineTransform.getScaleInstance(t.reverseX?-1:1, t.reverseY?-1:1));
            at.concatenate(AffineTransform.getTranslateInstance(t.reverseX?-t.rectangle.width:0, t.reverseY?-t.rectangle.height:0));
            var sourceSubImage = source.getSubimage(t.rectangle.x,t.rectangle.y,t.rectangle.width,t.rectangle.height);
            var txSourceSubImage = new BufferedImage(sourceSubImage.getWidth(), sourceSubImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            txSourceSubImage.createGraphics().drawImage(sourceSubImage,at,null);

            result.getGraphics().drawImage(
                    txSourceSubImage,
                    t.location.x,
                    t.location.y,
                    null
            );


        }

        return result;
    }


}
