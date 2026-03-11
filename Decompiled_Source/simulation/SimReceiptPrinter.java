/*
 * Decompiled with CFR 0.152.
 */
package simulation;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SimReceiptPrinter
extends Panel {
    private TextArea printArea;
    private Button take;

    SimReceiptPrinter() {
        this.setLayout(new BorderLayout(5, 5));
        this.printArea = new TextArea("", 9, 30, 1);
        this.printArea.setBackground(Color.white);
        this.printArea.setForeground(Color.black);
        this.printArea.setFont(new Font("Monospaced", 0, 12));
        this.printArea.setEditable(false);
        this.add((Component)this.printArea, "South");
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 1));
        this.take = new Button("Take receipt");
        buttonPanel.add(this.take);
        this.add((Component)buttonPanel, "North");
        this.take.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                SimReceiptPrinter.this.printArea.setText("");
                SimReceiptPrinter.this.take.setVisible(false);
            }
        });
        this.take.setVisible(false);
    }

    void println(String text) {
        this.printArea.append(String.valueOf(text) + '\n');
        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        this.take.setVisible(true);
    }
}

