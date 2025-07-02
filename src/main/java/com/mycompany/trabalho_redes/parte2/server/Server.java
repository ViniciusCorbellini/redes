package com.mycompany.trabalho_redes.parte2.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author Vinicius Corbellini
 */
public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000); //Escuta na 0.0.0.0 por default
        LOG.info(String.format("Starting ServerSocket on ip:port - > %s:%s \n", serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort()));

        LOG.info(String.format("Creating dir 'server_files' if it doesn't exist\n"));
        File serverDir = new File("server_files");
        if (!serverDir.exists()) serverDir.mkdir();
        LOG.info(String.format("Filepath: %s\n", serverDir.getAbsolutePath()));
        
        while (true) {
            LOG.info(String.format("Waiting for clients\n"));
            Socket client = serverSocket.accept();
            
            LOG.info(String.format("Client Accepted, instantiating new Client Handler\n"));
            new Thread(new ClientHandler(client)).run();
        }
    }
}
