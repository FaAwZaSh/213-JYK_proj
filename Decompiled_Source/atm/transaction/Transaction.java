/*
 * Decompiled with CFR 0.152.
 */
package atm.transaction;

import atm.ATM;
import atm.Session;
import atm.physical.CustomerConsole;
import atm.transaction.Deposit;
import atm.transaction.Inquiry;
import atm.transaction.Transfer;
import atm.transaction.Withdrawal;
import banking.Balances;
import banking.Card;
import banking.Message;
import banking.Receipt;
import banking.Status;

public abstract class Transaction {
    protected ATM atm;
    protected Session session;
    protected Card card;
    protected int pin;
    protected int serialNumber;
    protected Message message;
    protected Balances balances;
    private static final String[] TRANSACTION_TYPES_MENU = new String[]{"Withdrawal", "Deposit", "Transfer", "Balance Inquiry"};
    private static int nextSerialNumber = 1;
    private int state;
    private static final int GETTING_SPECIFICS_STATE = 1;
    private static final int SENDING_TO_BANK_STATE = 2;
    private static final int INVALID_PIN_STATE = 3;
    private static final int COMPLETING_TRANSACTION_STATE = 4;
    private static final int PRINTING_RECEIPT_STATE = 5;
    private static final int ASKING_DO_ANOTHER_STATE = 6;

    protected Transaction(ATM atm, Session session, Card card, int pin) {
        this.atm = atm;
        this.session = session;
        this.card = card;
        this.pin = pin;
        this.serialNumber = nextSerialNumber++;
        this.balances = new Balances();
        this.state = 1;
    }

    public static Transaction makeTransaction(ATM atm, Session session, Card card, int pin) throws CustomerConsole.Cancelled {
        int choice = atm.getCustomerConsole().readMenuChoice("Please choose transaction type", TRANSACTION_TYPES_MENU);
        switch (choice) {
            case 0: {
                return new Withdrawal(atm, session, card, pin);
            }
            case 1: {
                return new Deposit(atm, session, card, pin);
            }
            case 2: {
                return new Transfer(atm, session, card, pin);
            }
            case 3: {
                return new Inquiry(atm, session, card, pin);
            }
        }
        return null;
    }

    public boolean performTransaction() throws CardRetained {
        String doAnotherMessage = "";
        Status status = null;
        Receipt receipt = null;
        block16: while (true) {
            switch (this.state) {
                case 1: {
                    try {
                        this.message = this.getSpecificsFromCustomer();
                        this.atm.getCustomerConsole().display("");
                        this.state = 2;
                    }
                    catch (CustomerConsole.Cancelled e) {
                        doAnotherMessage = "Last transaction was cancelled";
                        this.state = 6;
                    }
                    continue block16;
                }
                case 2: {
                    status = this.atm.getNetworkToBank().sendMessage(this.message, this.balances);
                    if (status.isInvalidPIN()) {
                        this.state = 3;
                        continue block16;
                    }
                    if (status.isSuccess()) {
                        this.state = 4;
                        continue block16;
                    }
                    doAnotherMessage = status.getMessage();
                    this.state = 6;
                    continue block16;
                }
                case 3: {
                    try {
                        status = this.performInvalidPINExtension();
                        if (status.isSuccess()) {
                            this.state = 4;
                            continue block16;
                        }
                        doAnotherMessage = status.getMessage();
                        this.state = 6;
                    }
                    catch (CustomerConsole.Cancelled e) {
                        doAnotherMessage = "Last transaction was cancelled";
                        this.state = 6;
                    }
                    continue block16;
                }
                case 4: {
                    try {
                        receipt = this.completeTransaction();
                        this.state = 5;
                    }
                    catch (CustomerConsole.Cancelled e) {
                        doAnotherMessage = "Last transaction was cancelled";
                        this.state = 6;
                    }
                    continue block16;
                }
                case 5: {
                    this.atm.getReceiptPrinter().printReceipt(receipt);
                    this.state = 6;
                    continue block16;
                }
                case 6: {
                    if (doAnotherMessage.length() > 0) {
                        doAnotherMessage = String.valueOf(doAnotherMessage) + "\n";
                    }
                    try {
                        String[] yesNoMenu = new String[]{"Yes", "No"};
                        boolean doAgain = this.atm.getCustomerConsole().readMenuChoice(String.valueOf(doAnotherMessage) + "Wood you like to do another transaction?", yesNoMenu) == 0;
                        return doAgain;
                    }
                    catch (CustomerConsole.Cancelled e) {
                        return false;
                    }
                }
            }
        }
    }

    public Status performInvalidPINExtension() throws CustomerConsole.Cancelled, CardRetained {
        Status status = null;
        int i = 0;
        while (i < 3) {
            this.pin = this.atm.getCustomerConsole().readPIN("PIN was incorrect\nPlease re-enter your PIN\nThen press ENTER");
            this.atm.getCustomerConsole().display("");
            this.message.setPIN(this.pin);
            status = this.atm.getNetworkToBank().sendMessage(this.message, this.balances);
            if (!status.isInvalidPIN()) {
                this.session.setPIN(this.pin);
                return status;
            }
            ++i;
        }
        this.atm.getCardReader().retainCard();
        this.atm.getCustomerConsole().display("Your card has been retained\nPlease contact the bank.");
        try {
            Thread.sleep(5000L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        this.atm.getCustomerConsole().display("");
        throw new CardRetained();
    }

    public int getSerialNumber() {
        return this.serialNumber;
    }

    protected abstract Message getSpecificsFromCustomer() throws CustomerConsole.Cancelled;

    protected abstract Receipt completeTransaction() throws CustomerConsole.Cancelled;

    public static class CardRetained
    extends Exception {
        public CardRetained() {
            super("Card retained due to too many invalid PINs");
        }
    }
}

