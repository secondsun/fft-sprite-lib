package dev.secondsun.fftspriteanimator;

public class Util {

    public static Integer getInt(int[] copyOfRange) {
        assert copyOfRange.length == 4;
        return (copyOfRange[0] & 0x00FF) + ((copyOfRange[1] & 0x00FF) << 8) + ((copyOfRange[2] & 0x00FF) << 16) + ((copyOfRange[3] & 0x00FF) << 24);
    }
}
