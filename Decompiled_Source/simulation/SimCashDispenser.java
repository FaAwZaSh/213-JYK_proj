/*
 * Decompiled with CFR 0.152.
 */
package simulation;

import banking.Money;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

class SimCashDispenser
extends Panel {
    private Label label;

    SimCashDispenser() {
        this.setLayout(new GridLayout(1, 1));
        this.label = new Label("$XXX.XX", 1);
        this.label.setFont(new Font("SansSerif", 0, 24));
        this.label.setForeground(new Color(0, 64, 0));
        this.add(this.label);
        this.label.setVisible(false);
    }

    public void animateDispensingCash(Money amount) {
        this.label.setText(amount.toString());
        int size = 3;
        while (size <= 24) {
            this.label.setFont(new Font("SansSerif", 0, size));
            this.label.setVisible(true);
            try {
                Thread.sleep(250L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            this.label.setVisible(false);
            ++size;
        }
    }
}

