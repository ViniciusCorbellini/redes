package com.mycompany.trabalho_redes.parte1.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mycompany.trabalho_redes.parte1.util.JsonUtil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Logger;

/**
 *
 * @author manoCorbas
 */
public class Sender {

    private static final Logger LOG = Logger.getLogger(Sender.class.getName());
    
    private String username;

    public Sender(String username) {
        this.username = username;
        LOG.info("Sender criado para o usuario: " + username+ "\n");
    }
   
    public void send(Message m, NetworkHandler nh) throws JsonProcessingException, IOException  {
        String msg_json_str = JsonUtil.JsonStringFormat(m);
        byte[] data = msg_json_str.getBytes();

        DatagramPacket p = new DatagramPacket(data, data.length, nh.getGroup(), nh.getPort());
        nh.getSocket().send(p);
        
        LOG.info("[" + username + "] → Mensagem enviada para " + nh.getGroup().getHostAddress() +
                     ":" + nh.getPort() + " -> Conteudo: " + msg_json_str+ "\n");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Sender{" + "username=" + username + '}';
    }
}
