package com.mycompany.trabalho_redes.parte1;

import com.mycompany.trabalho_redes.parte1.view.GUI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinicius Corbellini
 */
public class Main {
    public static void main(String[] args) {
        GUI gui = null;
        try {
            gui = new GUI();
            gui.setVisible(true);
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(gui, ex);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
