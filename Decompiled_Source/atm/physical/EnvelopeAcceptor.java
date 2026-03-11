/*
 * Decompiled with CFR 0.152.
 */
package atm.physical;

import atm.physical.CustomerConsole;
import atm.physical.Log;
import simulation.Simulation;

public class EnvelopeAcceptor {
    private Log log;

    public EnvelopeAcceptor(Log log) {
        this.log = log;
    }

    public void acceptEnvelope() throws CustomerConsole.Cancelled {
        boolean inserted = Simulation.getInstance().acceptEnvelope();
        if (!inserted) {
            throw new CustomerConsole.Cancelled();
        }
        this.log.logEnvelopeAccepted();
    }
}

