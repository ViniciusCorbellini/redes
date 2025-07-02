package com.mycompany.trabalho_redes.parte1.controller;

import com.mycompany.trabalho_redes.parte1.model.Message;
import com.mycompany.trabalho_redes.parte1.model.NetworkHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mycompany.trabalho_redes.parte1.model.Receiver;
import com.mycompany.trabalho_redes.parte1.model.Sender;
import com.mycompany.trabalho_redes.parte1.util.JsonUtil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinicius Corbellini
 */
public class Controller {

    private static final Logger LOG = Logger.getLogger(Controller.class.getName());
    
    private NetworkHandler nh;

    private Receiver rx;
    private Sender tx;
    
    private LinkedList<Message> fila_mensagens;
    private ArrayList<String> chat_msgs;
    private Thread receiverThread;
    
    public void send(Message m) throws Exception {
        if (this.nh == null) {
            throw new Exception("O network handler e nulo!\n");
        }
        LOG.info("\nEnviando mensagem: " + m + "\n");
        this.tx.send(m, this.nh);
    }

    public void receive() throws InterruptedException, Exception {
        LOG.info("Iniciando thread de recebimento...\n");
        receiverThread = new Thread(() -> {
            try {
                rx.receive(nh);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Erro no controller ao tentar receber pacotes: " + e.getMessage(), e + "\n");
                JOptionPane.showMessageDialog(null, ("Erro no controller ao tentar receber pacotes" + e.getMessage()));
            }
        });
        receiverThread.start();
        handshake();
    }

    public void enter(String ip, int port, String username) throws IOException, Exception {
        if (this.nh != null) {//Erro aqui pq eu nao sei
            LOG.info("Saindo do grupo anterior...\n");
            this.nh.leave();
        }
        
        LOG.info("Entrando no grupo multicast: " + ip + ":" + port+ "\n");
        NetworkHandler nh = new NetworkHandler(ip, port);
        this.nh = nh;
        
        this.rx = new Receiver(this);
        this.receive();
    }
    
    public void notifyMsgQueue(Message m) {
        LOG.fine("Mensagem adicionada a fila: " + m+ "\n");
        fila_mensagens.add(m);
    }

    // ===== Metodos privados
    private void handshake() throws JsonProcessingException, IOException, Exception {
        Message m = new Message("GRUPO", "Novo usuario entrou no grupo ip: " + this.nh.getGroup() + " | port: " + this.nh.getPort() + " > Diga olá!");
        LOG.info("Realizando handshake com o grupo.\n");
        String m_json = JsonUtil.JsonStringFormat(m);
        byte[] data = m_json.getBytes();

        DatagramPacket p = new DatagramPacket(data, data.length, this.nh.getGroup(), this.nh.getPort());
        this.nh.getSocket().send(p);
    }

    // ===== Construtores
    public Controller() {
        this.fila_mensagens = new LinkedList<>();
        this.chat_msgs = new ArrayList<>();
        this.tx = new Sender("");
        LOG.info("Controller inicializado.\n");
    }

    // ===== Getters 
    public NetworkHandler getNh() {
        return nh;
    }

    public LinkedList<Message> getFila_mensagens() {
        return fila_mensagens;
    }

    public void setFila_mensagens(LinkedList<Message> fila_mensagens) {
        this.fila_mensagens = fila_mensagens;
    }

    public ArrayList<String> getChat_msgs() {
        return chat_msgs;
    }

    public void setChat_msgs(ArrayList<String> chat_msgs) {
        this.chat_msgs = chat_msgs;
    }

    public Receiver getRx() {
        return rx;
    }

    public void setRx(Receiver rx) {
        this.rx = rx;
    }

    public Sender getTx() {
        return tx;
    }

    public void setTx(Sender tx) {
        this.tx = tx;
    }

    @Override
    public String toString() {
        return "Controller{"
                + "\nnh=" + nh
                + "\n, fila_mensagens=" + fila_mensagens
                + "\n, rx=" + rx
                + "\n, tx=" + tx + '}';
    }
}
