/*
 * Decompiled with CFR 0.152.
 */
package atm.physical;

import atm.ATM;
import banking.Card;
import simulation.Simulation;

public class CardReader {
    private ATM atm;

    public CardReader(ATM atm) {
        this.atm = atm;
    }

    public Card readCard() {
        return Simulation.getInstance().readCard();
    }

    public void ejectCard() {
        Simulation.getInstance().ejectCard();
    }

    public void retainCard() {
        Simulation.getInstance().retainCard();
    }
}

