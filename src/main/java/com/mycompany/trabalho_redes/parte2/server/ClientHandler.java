package com.mycompany.trabalho_redes.parte2.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.trabalho_redes.parte2.model.JsonMessage;
import com.mycompany.trabalho_redes.parte2.util.FileUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Vinicius Corbellini
 */
public class ClientHandler implements Runnable {

    private static final Logger LOG = Logger.getLogger(ClientHandler.class.getName());

    private Socket socket;
    private ObjectMapper mapper = new ObjectMapper();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(//Permite ler linhas 
                    new InputStreamReader( //converte os bytes recebidos do socket em caracteres
                            socket.getInputStream() //fluxo de entrada do socket
                    )
            );
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), //fluxo de saida do socket
                    true //Envia automaticamente os dados qnd println() for chamado
            );

            String line;
            //Itera ate que a comunicação acabe
            while ((line = in.readLine()) != null) {
                //OM converte o pacote 

                LOG.info("Waiting for request messages");
                JsonMessage msg = mapper.readValue(line, JsonMessage.class);

                LOG.info("Starting response");
                JsonMessage resp = new JsonMessage();

                switch (msg.getCmd()) {
                    case "list_req" -> {
                        listRequest(resp);
                    }

                    case "put_req" -> {
                        putRequest(msg, resp);
                    }

                    case "get_req" -> {
                        getRequest(msg, resp);
                    }
                }
                LOG.info("Sending response");
                out.println(mapper.writeValueAsString(resp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listRequest(JsonMessage resp) {
        LOG.info("Handling list request");
        File[] files = new File("server_files").listFiles();

        LOG.info("Getting server files and adding it to response");
        resp.setCmd("list_resp");

        //Filtra os arquivos 
        resp.setFiles(Arrays.stream(files)
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toList()));
    }

    private void putRequest(JsonMessage msg, JsonMessage resp) throws IOException, Exception {
        LOG.info("Handling put request");
        File newFile = new File("server_files/" + msg.getFile());

        FileUtils.saveBase64ToFile(msg.getValue(), newFile);
        LOG.info(String.format("Decoding and saving file '%s' to server_files dir", msg.getFile()));

        String hash = FileUtils.calculateSHA256(newFile);
        LOG.info("Calculating SHA-256 hash of the received file");

        resp.setCmd("put_resp");
        resp.setFile(msg.getFile());

        String status = hash.equals(msg.getHash()) ? "ok" : "fail";
        resp.setStatus(status);

        LOG.info(String.format("File upload status: %s", status));
    }

    private void getRequest(JsonMessage msg, JsonMessage resp) throws IOException, Exception {
        LOG.info("Handling get request");
        
        File requestedFile = new File("server_files/" + msg.getFile());
        LOG.info(String.format("Looking for file '%s' in server_files directory", msg.getFile()));
        
        if (requestedFile.exists()) {
            LOG.info("File found. Preparing response with base64 content and hash.");
            resp.setCmd("get_resp");
            resp.setFile(msg.getFile());
            resp.setValue(FileUtils.fileToBase64(requestedFile));
            resp.setHash(FileUtils.calculateSHA256(requestedFile));
            LOG.info(String.format("File '%s' successfully encoded and hashed", msg.getFile()));
        } else {
            LOG.info(String.format("File '%s' not found", msg.getFile()));
            resp.setCmd("get_resp");
            resp.setFile(msg.getFile());
            resp.setStatus("fail");
        }
    }
}
