package com.mycompany.trabalho_redes.parte2.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.trabalho_redes.parte2.model.JsonMessage;
import com.mycompany.trabalho_redes.parte2.util.FileUtils;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinicius Corbellini
 */
public class Client {

    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    public static void createDIR() {
        LOG.info("Creating dir 'client_files' if it doens't already exist\n");
        File clientDir = new File("client_files");
        if (!clientDir.exists()) {
            LOG.info(String.format("Creating dir -> filepath: %s\n", clientDir.getPath()));
            clientDir.mkdir();
        }
    }

    public static Socket getIpAndPort() throws IOException {
        LOG.info("Asking user for server's ip:port\n");
        String ip = JOptionPane.showInputDialog("Insira o ip do servidor: ", "###.###.###.###");
        Integer port = Integer.parseInt(JOptionPane.showInputDialog("Insira a port do servidor: ", "0-65535"));

        return new Socket(ip, port);
    }

    public static String getInputFromUser() {
        LOG.info("Waiting for user cmd\n");
        String message = JOptionPane.showInputDialog("> Comandos: list, put <nome_arquivo.extensao>, get <nome_arquivo.extensao>", "separe os args por ' '");
        return message;
    }

    public static void putRequest(JsonMessage msg, String[] parts) throws IOException, Exception {
        LOG.info("Handling put request\n");
        File file = new File("client_files/" + parts[1]);
        msg.setCmd("put_req");
        msg.setFile(parts[1]);

        LOG.info("Calculating base64\n");
        msg.setValue(FileUtils.fileToBase64(file));
        
        LOG.info("Calculating SHA-256\n");
        msg.setHash(FileUtils.calculateSHA256(file));
    }

    public static void main(String[] args) throws Exception {
        try {
            createDIR();
            Socket socket = getIpAndPort();

            ObjectMapper mapper = new ObjectMapper();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            JOptionPane.showMessageDialog(null, "Conectado ao servidor. Comandos: list, put <arquivo>, get <arquivo>");
            LOG.info("Starting loop\n");
            while (true) {
                String input = getInputFromUser();
                String[] parts = input.split(" ");
                JsonMessage msg = new JsonMessage();

                switch (parts[0]) {
                    case "list" -> {
                        LOG.info("List request\n");
                        msg.setCmd("list_req");
                    }
                    case "put" -> {
                        LOG.info(String.format("Put request for file: %s\n", parts[1]));
                        putRequest(msg, parts);
                    }

                    case "get" -> {
                        LOG.info(String.format("Get request for file: %s\n", parts[1]));
                        msg.setCmd("get_req");
                        msg.setFile(parts[1]);
                    }
                    
                    case "exit" -> {
                        LOG.info("Exiting\n");
                        System.exit(0);
                    }

                    default -> {
                        LOG.info(String.format("Invalid input: %s\n", input));
                        JOptionPane.showMessageDialog(null, "Comando inválido.");
                        continue;
                    }
                }
                LOG.info(String.format("Sending request\n"));
                out.println(mapper.writeValueAsString(msg));

                LOG.info(String.format("Receiving response\n"));
                String response = in.readLine();
                JsonMessage resp = mapper.readValue(response, JsonMessage.class);

                switch (resp.getCmd()) {
                    case "list_resp" -> {
                        LOG.info(String.format("Listing files: %s\n", resp.getFiles()));
                        JOptionPane.showMessageDialog(null, "Arquivos disponíveis: \n" + resp.getFiles());
                    }
                    case "put_resp" -> {
                        LOG.info(String.format("Upload -> status: %s // file: %s\n", resp.getStatus(), resp.getFile()));
                        JOptionPane.showMessageDialog(null, "Upload " + resp.getStatus() + " para " + resp.getFile());
                    }
                    case "get_resp" -> {
                        LOG.info(String.format("Get response -> status: %s\n", resp.getStatus()));
                        if ("fail".equals(resp.getStatus())) {
                            JOptionPane.showMessageDialog(null, "Arquivo não encontrado.");
                        } else {
                            FileUtils.saveBase64ToFile(resp.getValue(), new File("client_files/" + resp.getFile()));
                            JOptionPane.showMessageDialog(null, "Download de " + resp.getFile() + " concluído.");
                            LOG.info(String.format("Succesfully downloaded file: %s\n", resp.getFile()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
