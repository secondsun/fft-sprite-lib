package dev.secondsun.fftspriteanimator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    static int sequenceIndex = 0;
    static int frameIndex = 0;

    private static JFrame frame;

    public static void main(String... args) throws IOException {
        Main.frame = new JFrame("3D Engine");
        frame.setSize(800,600);
        frame.add(new Main.Screen());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);



        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_Q -> {frameIndex=0;
                        sequenceIndex =0;}
                    case KeyEvent.VK_RIGHT -> frameIndex++;
                    case KeyEvent.VK_LEFT -> frameIndex--;
                    case KeyEvent.VK_UP -> {frameIndex = 0;
                        sequenceIndex++;}
                    case KeyEvent.VK_DOWN -> {frameIndex = 0;
                        sequenceIndex--;}
                    default -> frameIndex++;
                }
                frame.repaint();
            }
        });


    }


    private static class Screen extends Component {

        private static final BufferedImage CHRONO;

        static {
            try {
                CHRONO = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("bmg.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private final Shape shapes;
        private final Sequences seq;

        public Screen() throws IOException {

            this.shapes = ShapeLoader.loadSHP(Main.class.getClassLoader().getResourceAsStream("TYPE1.SHP"), new Rectangle( 106, 85, 48, 56 ));
            var stream = Main.class.getClassLoader().getResourceAsStream("TYPE1.SEQ");
            this.seq = SequenceLoader.loadSEQ(stream);

        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.BLACK);
            g.fillRect(0,0,getWidth(),getHeight());
            if (sequenceIndex >= seq.sequences.size()) {
                sequenceIndex = 0;
            }
            if (sequenceIndex <0) {
                sequenceIndex = seq.sequences.size()-1;
            }
            var sequence = seq.sequences.get(sequenceIndex);

            if (frameIndex < 0) {
                frameIndex = sequence.frames.size()-1;
            }

            if (frameIndex >= sequence.frames.size()) {
                frameIndex = 0;
            }
            System.out.println(sequence.frames.get(frameIndex).index());
            if (sequence.frames.get(frameIndex).index() != -1) {
                var frame = shapes.frames.get(sequence.frames.get(frameIndex).index());
                g.drawImage(frame.getFrame(CHRONO), 0, 0, getWidth(), getHeight(), null);
            }
        }
    }
}
