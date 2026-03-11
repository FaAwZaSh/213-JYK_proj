/*
 * Decompiled with CFR 0.152.
 */
package atm.physical;

import atm.ATM;
import banking.Money;
import simulation.Simulation;

public class OperatorPanel {
    private ATM atm;

    public OperatorPanel(ATM atm) {
        this.atm = atm;
    }

    public Money getInitialCash() {
        return Simulation.getInstance().getInitialCash();
    }
}

