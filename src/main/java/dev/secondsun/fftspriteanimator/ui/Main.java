package dev.secondsun.fftspriteanimator.ui;

import dev.secondsun.fftspriteanimator.Animations;
import dev.secondsun.fftspriteanimator.loader.SequenceLoader;
import dev.secondsun.fftspriteanimator.vo.Sequences;
import dev.secondsun.fftspriteanimator.vo.Shape;
import dev.secondsun.fftspriteanimator.loader.ShapeLoader;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
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
    private static UIBar uiBar;
    private static Screen screen;

    public static void main(String... args) throws IOException {

        Main.frame = new JFrame("FFT Sprites");
        LayoutManager layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.setLayout(layout);
        frame.setSize(800,600);

        screen = new Screen();
        uiBar = new UIBar();
        uiBar.subscribe(screen);
        frame.add(uiBar);
        frame.add(screen);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.requestFocusInWindow();

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


    private static class Screen extends Component implements Subscriber<Integer> {

        private static final BufferedImage CHRONO;

        static {
            try {
                CHRONO = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("crono1.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private final Shape shapes;
        private final Sequences seq;
        private Subscription subscription;

        public Screen() throws IOException {

            this.shapes = ShapeLoader
                .loadSHP(Main.class.getClassLoader().getResourceAsStream("TYPE1.SHP"), new Rectangle( 106, 85, 48, 56 ));
            var stream = Main.class.getClassLoader().getResourceAsStream("TYPE1.SEQ");
            this.seq = SequenceLoader.loadSEQ(stream);
            this.setMinimumSize(new Dimension(800,600));
            this.setPreferredSize(new Dimension(800,600));
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
            uiBar.totalFrames.setText("" + seq.sequences.get(sequenceIndex).frames.size());
            uiBar.animationSpinner.setSelectedItem(Animations.fromIndex(sequenceIndex));
            uiBar.currentFrame.setText("" +frameIndex);
            uiBar.repaint();
            if (sequence.frames.get(frameIndex).index() != -1) {
                var frame = shapes.frames.get(sequence.frames.get(frameIndex).index());
                g.drawImage(frame.getFrame(CHRONO), 0, 0, getWidth(), getHeight(), null);
            }
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;

        }

        @Override
        public void onNext(Integer item) {
            sequenceIndex = item;
            frameIndex = 0;
            uiBar.currentFrame.setText("0");
            uiBar.totalFrames.setText("" + seq.sequences.get(sequenceIndex).frames.size());
            repaint();
            frame.requestFocusInWindow();

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }
}

