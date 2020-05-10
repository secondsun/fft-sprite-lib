package dev.secondsun.fftspriteanimator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sequences define animations. They are index by some constant, then explain how to combine the bitmap and shape into a frame
 */
public class Sequences {

    List<Integer> offsets = new ArrayList<>(0x100);
    List<Sequence> sequences = new ArrayList<>();
    /**
     * Offsets are the start of an animation in the seq file.
     * Animations begin at such that animation N begins at 0x0406 + offset[N]
     *
     * @return all of the offsets
     */
    public List<Integer> getOffsets() {
        return offsets;
    }

    public void addOffset(Integer currentOffset) {
        offsets.add(currentOffset);
    }
}
