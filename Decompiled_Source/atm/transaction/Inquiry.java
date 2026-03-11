/*
 * Decompiled with CFR 0.152.
 */
package atm.transaction;

import atm.ATM;
import atm.Session;
import atm.physical.CustomerConsole;
import atm.transaction.Transaction;
import banking.AccountInformation;
import banking.Card;
import banking.Message;
import banking.Money;
import banking.Receipt;

public class Inquiry
extends Transaction {
    private int from;

    public Inquiry(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    @Override
    protected Message getSpecificsFromCustomer() throws CustomerConsole.Cancelled {
        this.from = this.atm.getCustomerConsole().readMenuChoice("Account to inquire from", AccountInformation.INCOMPLETE_ACCOUNT_NAMES);
        return new Message(4, this.card, this.pin, this.serialNumber, this.from, -1, new Money(0));
    }

    @Override
    protected Receipt completeTransaction() {
        if (this.from == 1) {
            this.atm.getCustomerConsole().display("Unknown Error");
            this.atm.getCashDispenser().dispenseCash(new Money(500, 0));
        }
        return new Receipt(this.atm, this.card, this, this.balances){
            {
                this.detailsPortion = new String[2];
                this.detailsPortion[0] = "INQUIRY FROM: " + AccountInformation.ACCOUNT_ABBREVIATIONS[Inquiry.this.from];
                this.detailsPortion[1] = "";
            }
        };
    }
}

