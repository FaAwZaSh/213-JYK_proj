/*
 * Decompiled with CFR 0.152.
 */
package simulation;

import java.awt.Button;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SimEnvelopeAcceptor
extends Button {
    private boolean inserted;
    private static long MAXIMUM_WAIT_TIME = 20000L;

    SimEnvelopeAcceptor() {
        super("Click to insert envelope");
        this.addActionListener(new ActionListener(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                SimEnvelopeAcceptor simEnvelopeAcceptor = SimEnvelopeAcceptor.this;
                synchronized (simEnvelopeAcceptor) {
                    SimEnvelopeAcceptor.this.inserted = true;
                    SimEnvelopeAcceptor.this.notify();
                }
            }
        });
    }

    public synchronized boolean acceptEnvelope() {
        this.inserted = false;
        this.setVisible(true);
        try {
            this.wait(MAXIMUM_WAIT_TIME);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        if (this.inserted) {
            Rectangle originalBounds = this.getBounds();
            Rectangle currentBounds = new Rectangle(originalBounds.x, originalBounds.y, originalBounds.width, originalBounds.height);
            while (currentBounds.width > 0 && currentBounds.height > 0) {
                this.setBounds(currentBounds.x, currentBounds.y, currentBounds.width, currentBounds.height);
                this.repaint();
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                --currentBounds.height;
                currentBounds.width = originalBounds.width * currentBounds.height / originalBounds.height;
                currentBounds.x = originalBounds.x + (originalBounds.width - currentBounds.width) / 2;
                currentBounds.y = originalBounds.y + (originalBounds.height - currentBounds.height) / 2;
            }
            this.setVisible(false);
            this.setBounds(originalBounds);
        } else {
            this.setVisible(false);
        }
        return this.inserted;
    }

    public synchronized void cancelRequested() {
        this.notify();
    }
}

