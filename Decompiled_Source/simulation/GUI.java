/*
 * Decompiled with CFR 0.152.
 */
package simulation;

import banking.Card;
import banking.Money;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Panel;
import simulation.ATMPanel;
import simulation.BillsPanel;
import simulation.CardPanel;
import simulation.LogPanel;
import simulation.SimCardReader;
import simulation.SimCashDispenser;
import simulation.SimDisplay;
import simulation.SimEnvelopeAcceptor;
import simulation.SimKeyboard;
import simulation.SimOperatorPanel;
import simulation.SimReceiptPrinter;

class GUI
extends Panel {
    private CardLayout mainLayout;
    private ATMPanel atmPanel;
    private BillsPanel billsPanel;
    private CardPanel cardPanel;
    private LogPanel logPanel;

    GUI(SimOperatorPanel operatorPanel, SimCardReader cardReader, SimDisplay display, SimKeyboard keyboard, SimCashDispenser cashDispenser, SimEnvelopeAcceptor envelopeAcceptor, SimReceiptPrinter receiptPrinter) {
        this.setBackground(Color.lightGray);
        this.mainLayout = new CardLayout(5, 5);
        this.setLayout(this.mainLayout);
        this.atmPanel = new ATMPanel(this, operatorPanel, cardReader, display, keyboard, cashDispenser, envelopeAcceptor, receiptPrinter);
        this.add((Component)this.atmPanel, "ATM");
        this.billsPanel = new BillsPanel();
        this.add((Component)this.billsPanel, "BILLS");
        this.cardPanel = new CardPanel();
        this.add((Component)this.cardPanel, "CARD");
        this.logPanel = new LogPanel(this);
        this.add((Component)this.logPanel, "LOG");
        this.mainLayout.show(this, "ATM");
    }

    public Money getInitialCash() {
        this.mainLayout.show(this, "BILLS");
        int numberOfBills = this.billsPanel.readBills();
        this.mainLayout.show(this, "ATM");
        return new Money(20 * numberOfBills);
    }

    public Card readCard() {
        this.mainLayout.show(this, "CARD");
        int cardNumber = this.cardPanel.readCardNumber();
        this.mainLayout.show(this, "ATM");
        if (cardNumber > 0) {
            return new Card(cardNumber);
        }
        return null;
    }

    public void printLogLine(String text) {
        this.logPanel.println(text);
    }

    void showCard(String cardName) {
        this.mainLayout.show(this, cardName);
    }

    static GridBagConstraints makeConstraints(int row, int col, int width, int height, int fill) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = row;
        g.gridx = col;
        g.gridheight = height;
        g.gridwidth = width;
        g.fill = fill;
        g.insets = new Insets(2, 2, 2, 2);
        g.weightx = 1.0;
        g.weighty = 1.0;
        g.anchor = 10;
        return g;
    }
}

