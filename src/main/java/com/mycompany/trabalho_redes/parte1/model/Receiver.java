package com.mycompany.trabalho_redes.parte1.model;

import com.mycompany.trabalho_redes.parte1.util.JsonUtil;
import com.mycompany.trabalho_redes.parte1.controller.Controller;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Logger;

/**
 *
 * @author manoCorbas
 */
public class Receiver {

    private Controller controller;
    private static final Logger LOG = Logger.getLogger(Receiver.class.getName());
    
    
    public Receiver(Controller controller) {
        this.controller = controller;
    }

    public void receive(NetworkHandler nh) throws IOException {
        byte[] buffer = new byte[nh.getBuffer_size()];
        DatagramPacket p = new DatagramPacket(buffer, buffer.length);

        LOG.info("Iniciando recebimento de mensagens no grupo multicast " +
                nh.getGroup().getHostAddress() + ":" + nh.getPort()+ "\n");
        
        while (true) {
            LOG.info("Escutando o canal!\n");
            nh.getSocket().receive(p);
            
            Message m = JsonUtil.getMessageFromPacket(p);
            LOG.info("Mensagem recebida de " + p.getAddress().getHostAddress() +
                        ":" + p.getPort() + " → Conteúdo: " + m.toString()+ "\n");
            controller.notifyMsgQueue(m);
        }
    }
}
