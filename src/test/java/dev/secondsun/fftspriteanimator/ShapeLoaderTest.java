package dev.secondsun.fftspriteanimator;

import dev.secondsun.fftspriteanimator.loader.ShapeLoader;
import dev.secondsun.fftspriteanimator.vo.Shape;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShapeLoaderTest {
    @Test
    public void loadShape() throws Exception {
        //type1 = new Shape( Properties.Resources.TYPE1_SHP, "TYPE1", new Rectangle( 106, 85, 48, 56 ) );
        Shape shapes = ShapeLoader
            .loadSHP(getClass().getClassLoader().getResourceAsStream("TYPE1.SHP"), new Rectangle( 106, 85, 48, 56 ));
        assertNotNull(shapes);




    }
}
