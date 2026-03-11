/*
 * Decompiled with CFR 0.152.
 */
package banking;

import atm.ATM;
import atm.transaction.Transaction;
import banking.Balances;
import banking.Card;
import java.util.Date;
import java.util.Enumeration;

public abstract class Receipt {
    private String[] headingPortion = new String[4];
    protected String[] detailsPortion;
    private String[] balancesPortion;

    protected Receipt(ATM atm, Card card, Transaction transaction, Balances balances) {
        this.headingPortion[0] = new Date().toString();
        this.headingPortion[1] = atm.getBankName();
        this.headingPortion[2] = "ATM #" + atm.getID() + " " + atm.getPlace();
        this.headingPortion[3] = "CARD " + (card.getNumber() + 1) + " TRANS #" + transaction.getSerialNumber();
        this.balancesPortion = new String[2];
        this.balancesPortion[0] = "TOTAL BAL: " + balances.getTotal();
        this.balancesPortion[1] = "AVAILABLE: " + balances.getAvailable();
    }

    public Enumeration getLines() {
        return new Enumeration(){
            private int portion = 0;
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return this.portion < 2 || this.index < Receipt.this.balancesPortion.length;
            }

            public Object nextElement() {
                String line = null;
                switch (this.portion) {
                    case 0: {
                        line = Receipt.this.headingPortion[this.index++];
                        if (this.index < Receipt.this.headingPortion.length) break;
                        ++this.portion;
                        this.index = 0;
                        break;
                    }
                    case 1: {
                        line = Receipt.this.detailsPortion[this.index++];
                        if (this.index < Receipt.this.detailsPortion.length) break;
                        ++this.portion;
                        this.index = 0;
                        break;
                    }
                    case 2: {
                        line = Receipt.this.balancesPortion[this.index++];
                    }
                }
                return line;
            }
        };
    }
}

