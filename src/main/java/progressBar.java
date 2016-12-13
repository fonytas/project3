import javax.swing.*;
import javax.swing.border.Border;

public class progressBar extends JPanel {

    JProgressBar proBar;

    static final int MY_MINIMUM = 0;

    static final int MY_MAXIMUM = 100;

    public progressBar() {
        proBar = new JProgressBar();
        proBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Seeding..");
        proBar.setBorder(border);
        proBar.setMinimum(MY_MINIMUM);
        proBar.setMaximum(MY_MAXIMUM);
        proBar.setSize(600,800);
        add(proBar);
    }

    public void updateBar(int newValue) {
        proBar.setValue(newValue);
    }



}
