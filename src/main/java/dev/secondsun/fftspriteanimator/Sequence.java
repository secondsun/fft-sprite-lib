package dev.secondsun.fftspriteanimator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sequence {
    public final List<AnimationFrame> frames;
    private Set<Integer> uniqueFrames ;
    public Sequence(List<AnimationFrame> frames) {
        this.frames = frames;
        uniqueFrames = new HashSet<>();

        frames.forEach(frame->
        {
            uniqueFrames.add( frame.index() );
        });


    }
}
