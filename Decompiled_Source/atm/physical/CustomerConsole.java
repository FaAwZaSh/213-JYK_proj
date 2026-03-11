/*
 * Decompiled with CFR 0.152.
 */
package atm.physical;

import banking.Money;
import simulation.Simulation;

public class CustomerConsole {
    public void display(String message) {
        Simulation.getInstance().clearDisplay();
        Simulation.getInstance().display(message);
    }

    public int readPIN(String prompt) throws Cancelled {
        Simulation.getInstance().clearDisplay();
        Simulation.getInstance().display(prompt);
        Simulation.getInstance().display("");
        String input = Simulation.getInstance().readInput(1, 0);
        Simulation.getInstance().clearDisplay();
        if (input == null) {
            throw new Cancelled();
        }
        return Integer.parseInt(input);
    }

    public synchronized int readMenuChoice(String prompt, String[] menu) throws Cancelled {
        Simulation.getInstance().clearDisplay();
        Simulation.getInstance().display(prompt);
        int i = 0;
        while (i < menu.length) {
            Simulation.getInstance().display(String.valueOf(i + 1) + ") " + menu[i]);
            ++i;
        }
        String input = Simulation.getInstance().readInput(3, menu.length);
        Simulation.getInstance().clearDisplay();
        if (input == null) {
            throw new Cancelled();
        }
        return Integer.parseInt(input) - 1;
    }

    public synchronized Money readAmount(String prompt) throws Cancelled {
        Simulation.getInstance().clearDisplay();
        Simulation.getInstance().display(prompt);
        Simulation.getInstance().display("");
        String input = Simulation.getInstance().readInput(2, 0);
        Simulation.getInstance().clearDisplay();
        if (input == null) {
            throw new Cancelled();
        }
        int dollars = Integer.parseInt(input) / 100;
        int cents = Integer.parseInt(input) % 100;
        return new Money(dollars, cents);
    }

    public static class Cancelled
    extends Exception {
        public Cancelled() {
            super("Cancelled by customer");
        }
    }
}

