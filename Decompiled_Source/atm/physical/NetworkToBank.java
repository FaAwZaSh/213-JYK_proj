/*
 * Decompiled with CFR 0.152.
 */
package atm.physical;

import atm.physical.Log;
import banking.Balances;
import banking.Message;
import banking.Status;
import java.net.InetAddress;
import simulation.Simulation;

public class NetworkToBank {
    private Log log;
    private InetAddress bankAddress;

    public NetworkToBank(Log log, InetAddress bankAddress) {
        this.log = log;
        this.bankAddress = bankAddress;
    }

    public void openConnection() {
    }

    public void closeConnection() {
    }

    public Status sendMessage(Message message, Balances balances) {
        this.log.logSend(message);
        Status result = Simulation.getInstance().sendMessage(message, balances);
        this.log.logResponse(result);
        return result;
    }
}

