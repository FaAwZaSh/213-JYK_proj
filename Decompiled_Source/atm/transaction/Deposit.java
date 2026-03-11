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
import banking.Status;

public class Deposit
extends Transaction {
    private int to;
    private Money amount;

    public Deposit(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    @Override
    protected Message getSpecificsFromCustomer() throws CustomerConsole.Cancelled {
        this.to = this.atm.getCustomerConsole().readMenuChoice("Account to deposit to", AccountInformation.ACCOUNT_NAMES);
        this.amount = this.atm.getCustomerConsole().readAmount("Amount to deposit");
        return new Message(1, this.card, this.pin, this.serialNumber, -1, this.to, this.amount);
    }

    @Override
    protected Receipt completeTransaction() throws CustomerConsole.Cancelled {
        this.atm.getEnvelopeAcceptor().acceptEnvelope();
        Status status = this.atm.getNetworkToBank().sendMessage(new Message(2, this.card, this.pin, this.serialNumber, -1, this.to, this.amount), this.balances);
        return new Receipt(this.atm, this.card, this, this.balances){
            {
                this.detailsPortion = new String[2];
                this.detailsPortion[0] = "DEPOSIT TO: " + AccountInformation.ACCOUNT_ABBREVIATIONS[Deposit.this.to];
                this.detailsPortion[1] = "AMOUNT: " + Deposit.this.amount.toString();
            }
        };
    }
}

