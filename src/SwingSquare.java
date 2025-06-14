package src;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SwingSquare extends JTextField {

    // Constructor
    public SwingSquare(int size) {
        // add some restrictions to how text can be inputted to the field
        addKeyListener(new KeyAdapter() {
            // override what happens when a key is typed
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e); // not necessary

                // make variable for inputted character
                char inp = e.getKeyChar();

                if (!Character.isDigit(inp)) {
                    e.consume();
                    return;
                }

                int num = Integer.parseInt(getText() + inp);
                if (num > size*size || num == 0) {
                    e.consume();
                    return; // doesn't do anything but it's for consistency yk
                }
            }
        });
    }


    // Getters
    public int getInt() {
        return Integer.parseInt(getText());
    }
}