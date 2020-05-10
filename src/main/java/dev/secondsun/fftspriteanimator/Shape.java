package dev.secondsun.fftspriteanimator;


import java.awt.Rectangle;
import java.util.List;

public class Shape {
    public final Rectangle relevantRectangle;
    public List<Frame> frames;

    public Shape(Rectangle relevantRectangle) {
        this.relevantRectangle = relevantRectangle;
    }
}
