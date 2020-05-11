package dev.secondsun.fftspriteanimator;

import dev.secondsun.fftspriteanimator.loader.SequenceLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceLoaderTest {

    @Test
    public void testOffsetLoad() throws IOException {
        var stream = getClass().getClassLoader().getResourceAsStream("TYPE1.SEQ");
        assertNotNull(stream);

        var seq = SequenceLoader.loadSEQ(stream);
        assertNotNull(seq);

        assertTrue(0x100 > seq.getOffsets().size());
        assertTrue(0x00 < seq.getOffsets().size());


    }

}
