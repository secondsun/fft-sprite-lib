package dev.secondsun.fftspriteanimator.ui;

import dev.secondsun.fftspriteanimator.Animations;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Observer;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

public class UIBar extends JComponent  implements Publisher<Integer> {

  public JTextField totalFrames;
  public JTextField currentFrame;
  public JComboBox<Animations> animationSpinner;

  public UIBar() {
    BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);

    this.setLayout(layout);
    this.add(selectBox());
    this.add(Box.createHorizontalGlue());
    this.add(new JLabel("Total Frames"));
    this.add(Box.createRigidArea(new Dimension(4,0)));
    this.add(totalFrames());
    this.add(Box.createRigidArea(new Dimension(8,0)));
    this.add(new JLabel("Current Frame"));
    this.add(Box.createRigidArea(new Dimension(4,0)));
    this.add(currentFrame());
    this.add(Box.createRigidArea(new Dimension(8,0)));

  }

  @Override
  public Dimension getMaximumSize() {
    return super.getPreferredSize();
  }

  private Component totalFrames() {
    totalFrames = new JTextField("0");
    return totalFrames;
  }

  private Component currentFrame() {
    currentFrame = new JTextField("0");
    return currentFrame;
  }



  private JComboBox<Animations> selectBox() {

    this.animationSpinner = new JComboBox<>(Animations.values());
    this.animationSpinner.setRenderer(new ListCellRenderer<Animations>() {
      @Override
      public Component getListCellRendererComponent(JList<? extends Animations> list,
          Animations value, int index, boolean isSelected, boolean cellHasFocus) {
        return new JLabel(propercase(value.name()));
      }
    });
    return animationSpinner;
  }

  private String propercase(String name) {
    return Arrays.stream(name.split("_")).map(word -> word.isEmpty()
        ? word
        : Character.toTitleCase(word.charAt(0)) + word
            .substring(1)
            .toLowerCase())
        .collect(Collectors.joining(" "));
  }

  @Override
  public void subscribe(Subscriber<? super Integer> subscriber) {
    this.animationSpinner.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
          var animations = animationSpinner.getItemAt(animationSpinner.getSelectedIndex());
          subscriber.onNext(animations.index);
        }
      }
    });
  }
}
