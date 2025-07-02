package com.mycompany.trabalho_redes.parte3.client;

import com.mycompany.trabalho_redes.parte3.common.Constants;
import com.mycompany.trabalho_redes.parte3.common.JsonUtil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Vinicius Corbellini
 */
public class UDPClient {

    private static final Logger LOG = Logger.getLogger(UDPClient.class.getName());
    
    private final String host;
    private final int port;
    
    private final int TIMEOUT = 3000;
    private final int BUFFER_SIZE;

    public UDPClient(String host, int port) {
        LOG.info("Initializing UDPClient");
        this.BUFFER_SIZE = Constants.BUFFER_SIZE;
        this.host = host;
        this.port = port;
    }
    
    public Map<String, Object> send(Map<String, Object> msg) throws SocketException, UnknownHostException, IOException{
        LOG.info("Initializing DatagramSocket");
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(TIMEOUT);
        
        LOG.info("Converting Message to data (byte[])");
        byte[] sendData = JsonUtil.toJson(msg).getBytes();
        
        LOG.info("Initializing DatagramPacket");
        DatagramPacket send_packet = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(host), port);
        LOG.info("Sending packet to server");
        socket.send(send_packet);
        
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket rcv_packet = new DatagramPacket(buffer, BUFFER_SIZE);
        LOG.info("Waiting for server's response");
        socket.receive(rcv_packet);
        
        LOG.info("Converting reponse: packet(JSON) -> String -> Map<String, Object>");
        String json_response = new String(rcv_packet.getData(), 0, rcv_packet.getLength());
        return JsonUtil.fromJson(json_response);
    }
}
