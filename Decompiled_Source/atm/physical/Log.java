/*
 * Decompiled with CFR 0.152.
 */
package atm.physical;

import banking.Message;
import banking.Money;
import banking.Status;
import simulation.Simulation;

public class Log {
    public void logSend(Message message) {
        Simulation.getInstance().printLogLine("Message:   " + message.toString());
    }

    public void logResponse(Status response) {
        Simulation.getInstance().printLogLine("Response:  " + response.toString());
    }

    public void logCashDispensed(Money amount) {
        Simulation.getInstance().printLogLine("Dispensed: " + amount.toString());
    }

    public void logEnvelopeAccepted() {
        Simulation.getInstance().printLogLine("Envelope:  received");
    }
}

