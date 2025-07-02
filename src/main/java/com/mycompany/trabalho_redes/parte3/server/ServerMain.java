package com.mycompany.trabalho_redes.parte3.server;

import javax.swing.JOptionPane;

/**
 *
 * @author Vinicius Corbellini
 */
public class ServerMain {
    public static void main(String[] args) {
        int port = Integer.parseInt(JOptionPane.showInputDialog("Insira a port do server:"));
        UDPServer srv = new UDPServer(port);
        srv.start();
    }
}
