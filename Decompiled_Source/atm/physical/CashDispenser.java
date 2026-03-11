/*
 * Decompiled with CFR 0.152.
 */
package atm.physical;

import atm.physical.Log;
import banking.Money;
import simulation.Simulation;

public class CashDispenser {
    private Log log;
    private Money cashOnHand;

    public CashDispenser(Log log) {
        this.log = log;
        this.cashOnHand = new Money(0);
    }

    public void setInitialCash(Money initialCash) {
        this.cashOnHand = initialCash;
    }

    public boolean checkCashOnHand(Money amount) {
        return amount.lessEqual(this.cashOnHand);
    }

    public void dispenseCash(Money amount) {
        this.cashOnHand.subtract(amount);
        Simulation.getInstance().dispenseCash(amount);
        this.log.logCashDispensed(amount);
    }
}

