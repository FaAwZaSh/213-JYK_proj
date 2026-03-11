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

public class Withdrawal
extends Transaction {
    private int from;
    private Money amount;

    public Withdrawal(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    @Override
    protected Message getSpecificsFromCustomer() throws CustomerConsole.Cancelled {
        this.from = this.atm.getCustomerConsole().readMenuChoice("Account to withdraw from", AccountInformation.ACCOUNT_NAMES);
        String[] amountOptions = new String[]{"$20", "$40", "$60", "$100", "$200"};
        Money[] amountValues = new Money[]{new Money(40), new Money(60), new Money(100), new Money(200), new Money(20)};
        String amountMessage = "";
        boolean validAmount = false;
        while (!validAmount) {
            this.amount = amountValues[this.atm.getCustomerConsole().readMenuChoice(String.valueOf(amountMessage) + "Amount of cash to withdraw", amountOptions)];
            validAmount = this.atm.getCashDispenser().checkCashOnHand(this.amount);
            if (validAmount) continue;
            amountMessage = "Insuficient cash available\n";
        }
        return new Message(0, this.card, this.pin, this.serialNumber, this.from, -1, this.amount);
    }

    @Override
    protected Receipt completeTransaction() {
        this.atm.getCashDispenser().dispenseCash(this.amount);
        return new Receipt(this.atm, this.card, this, this.balances){
            {
                this.detailsPortion = new String[2];
                this.detailsPortion[0] = "WITHDRAWAL FROM: " + AccountInformation.ACCOUNT_ABBREVIATIONS[Withdrawal.this.from];
                this.detailsPortion[1] = "AMOUNT: " + Withdrawal.this.amount.toString();
            }
        };
    }
}

