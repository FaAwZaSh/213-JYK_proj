/*
 * Decompiled with CFR 0.152.
 */
package atm.physical;

import banking.Receipt;
import java.util.Enumeration;
import simulation.Simulation;

public class ReceiptPrinter {
    public void printReceipt(Receipt receipt) {
        Enumeration receiptLines = receipt.getLines();
        while (receiptLines.hasMoreElements()) {
            Simulation.getInstance().printReceiptLine((String)receiptLines.nextElement());
        }
    }
}

