package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SwingSquare extends JTextField {
    private static final Font plainFont = new Font("Dialog", Font.PLAIN, 12);
    private static final Font boldFont = new Font("Dialog", Font.BOLD, 18);
    private boolean bold;

    // Constructor
    public SwingSquare(int size) {
        bold = false;
        setFont(plainFont);
        setHorizontalAlignment(SwingSquare.CENTER);

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

            // make inputted numbers look different from generated ones
            setBold();
            }
        });
    }


    // Getters
    public int getInt() {
        return Integer.parseInt(getText());
    }

    public boolean isBold() {
        return bold;
    }

    // Setters
    public void setBold() {
        setFont(boldFont);
        bold = true;
    }

    public void reset() {
        setText("");
        setFont(plainFont);
        bold = false;
    }
}