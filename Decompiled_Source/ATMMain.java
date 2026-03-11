/*
 * Decompiled with CFR 0.152.
 */
import atm.ATM;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import simulation.Simulation;

public class ATMMain {
    public static void main(String[] args) {
        ATM theATM = new ATM(42, "Gordon College", "First National Bank of Podunk", null);
        Simulation theSimulation = new Simulation(theATM);
        Frame mainFrame = new Frame("ATM Simulation");
        mainFrame.add(theSimulation.getGUI());
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem quitItem = new MenuItem("Quit", new MenuShortcut(81));
        quitItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);
        mainFrame.setMenuBar(menuBar);
        mainFrame.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        new Thread(theATM).start();
        mainFrame.setResizable(false);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}

