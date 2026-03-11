/*
 * Decompiled with CFR 0.152.
 */
import atm.ATM;
import java.applet.Applet;
import simulation.GUI;
import simulation.Simulation;

public class ATMApplet
extends Applet {
    @Override
    public void init() {
        ATM theATM = new ATM(42, "Gordon College", "First National Bank of Podunk", null);
        Simulation theSimulation = new Simulation(theATM);
        new Thread(theATM).start();
        GUI gui = theSimulation.getGUI();
        this.setBackground(gui.getBackground());
        this.add(gui);
    }
}

