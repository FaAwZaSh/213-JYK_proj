/*
 * Decompiled with CFR 0.152.
 */
package simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.util.StringTokenizer;

class SimDisplay
extends Panel {
    private Label[] displayLine;
    private int currentDisplayLine;

    SimDisplay() {
        this.setLayout(new GridLayout(9, 1));
        this.setBackground(new Color(0, 96, 0));
        this.setForeground(Color.white);
        Font lineFont = new Font("Monospaced", 0, 14);
        this.displayLine = new Label[9];
        int i = 0;
        while (i < 9) {
            this.displayLine[i] = new Label("                                             ");
            this.displayLine[i].setFont(lineFont);
            this.add(this.displayLine[i]);
            ++i;
        }
        this.currentDisplayLine = 0;
    }

    void clearDisplay() {
        int i = 0;
        while (i < this.displayLine.length) {
            this.displayLine[i].setText("");
            ++i;
        }
        this.currentDisplayLine = 0;
    }

    void display(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, "\n", false);
        while (tokenizer.hasMoreTokens()) {
            try {
                this.displayLine[this.currentDisplayLine++].setText(tokenizer.nextToken());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    void setEcho(String echo) {
        this.displayLine[this.currentDisplayLine].setText(String.valueOf("                                             ".substring(0, "                                             ".length() / 2 - echo.length())) + echo);
    }

    @Override
    public Insets getInsets() {
        Insets insets = super.getInsets();
        insets.top += 5;
        insets.bottom += 5;
        insets.left += 10;
        insets.right += 10;
        return insets;
    }
}

