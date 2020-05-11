package dev.secondsun.fftspriteanimator.loader;


import dev.secondsun.fftspriteanimator.vo.AnimationFrame;
import dev.secondsun.fftspriteanimator.vo.Sequence;
import dev.secondsun.fftspriteanimator.vo.Sequences;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static dev.secondsun.fftspriteanimator.Util.getInt;

/**
 * Loads a sequence .seq file
 * See https://ffhacktics.com/smf/index.php?topic=8834.msg174178#msg174178
 */
public final class SequenceLoader {

    private static final Map<Integer, Integer> jumps;

    static {
        jumps = new HashMap<>();
        jumps.put(192, 1);
        jumps.put(198, 1);
        jumps.put(211, 1);
        jumps.put(212, 1);
        jumps.put(214, 1);
        jumps.put(215, 1);
        jumps.put(216, 1);
        jumps.put(226, 1);
        jumps.put(238, 1);
        jumps.put(239, 1);
        jumps.put(240, 1);
        jumps.put(246, 1);
        jumps.put(193, 2);
        jumps.put(217, 2);
        jumps.put(242, 2);
        jumps.put(252, 2);
        jumps.put(250, 3);
    }

    private SequenceLoader() {
    }

    public static Sequences loadSEQ(InputStream seqStream) throws IOException {
        byte[] bytes2 = seqStream.readAllBytes();

        int[] bytes = new int[bytes2.length];
        for (int i = 0; i < bytes.length;i++) {
            bytes[i] = bytes2[i]&0x00ff;
        }

        var seqs = new Sequences();

        for (int i = 0; i < 0x100; i++) {
            Integer currentOffset = getInt(Arrays.copyOfRange(bytes, i * 4 + 4, (i + 1) * 4 + 4));
            if (currentOffset == 0xFFFFFFFF) {
                break;
            } else {
                seqs.addOffset(currentOffset);
            }
        }

        final int animationStart = 0x0406;
        final List<Integer> offsets = seqs.getOffsets();

        var result = new ArrayList<>(seqs.getOffsets().size());

        for (int i = 0; i < offsets.size() - 1; i++) {
            if (offsets.get(i) == offsets.get(i + 1))
                continue;
            var seq = buildSequence(Arrays.copyOfRange(bytes, animationStart + offsets.get(i), animationStart + offsets.get(i + 1)), i);
            if (seq != null)
                seqs.sequences.add(seq);
        }

        var seq2 = buildSequence(Arrays.copyOfRange(bytes, animationStart + offsets.get(offsets.size()-1),bytes.length), offsets.size());
        if (seq2 != null)
            seqs.sequences.add(seq2);



        return seqs;
    }

    private static Sequence buildSequence(int[] sequenceBytes, int offsetIndex) {
        var frames =
                processSequence(sequenceBytes, offsetIndex);
        if (frames == null)
            return null;
        else {
            return new Sequence(frames);
        }
    }

    private static List<AnimationFrame> processSequence(int[] bytes, int number) {
        int i = 0;
        List<int[]> sequence = new ArrayList<>();
        while (i < bytes.length - 1) {
            if (bytes[i] != 0xFF) {
                sequence.add(new int[]{bytes[i], bytes[i + 1]});
            } else if (jumps.containsKey(bytes[i + 1])) {
                i += jumps.get(bytes[i + 1]);
            }

            i += 2;
        }
        if (sequence.size() == 0) return null;

        List<AnimationFrame> result = new ArrayList<AnimationFrame>(sequence.size());
        for( int[] frame : sequence )
        {
            result.add(new AnimationFrame(frame[1], frame[0]));
        }

        return result;
    }


}
