import java.awt.*;

import javax.swing.*;

public class SCFrame extends JFrame {

    private JTextArea main = new JTextArea();
    private JTable out = new JTable();
    SCFrame(){
        this.setLayout(new GridLayout(2,1,0,0));
        this.add(main);
        this.add(out);
    }
}
