package com.mycompany.trabalho_redes.parte1.model;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 *
 * @author manoCorbas
 */
public class NetworkHandler {

    private static final Logger LOG = Logger.getLogger(NetworkHandler.class.getName());
    
    private int buffer_size;
    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private NetworkInterface network_inteface;

    public NetworkHandler(String multicastIp, int port) throws IOException {
        this.buffer_size = 1024;
        this.group = InetAddress.getByName(multicastIp);
        this.port = port;
        
        this.socket = new MulticastSocket(port);
        this.network_inteface = getValidNetworkInterface();
        //socket.setNetworkInterface(network_inteface);
        
        join();
        LOG.info("NetworkHandler criado para grupo " + group.getHostAddress() + ":" + port +
                 " usando interface: " + network_inteface.getDisplayName()+ "\n");
    }

    private NetworkInterface getValidNetworkInterface() throws IOException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (ni.isUp() && !ni.isLoopback() && !ni.isVirtual()) {
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    if (ia.getAddress() instanceof Inet4Address) {
                        LOG.info("Interface de rede valida encontrada: " + ni.getDisplayName()+ "\n");
                        return ni;
                    }
                }
            }
        }
        throw new IOException("Nenhuma interface de rede com IPv4 válida encontrada.");
    }

    public void join() throws IOException {
        //this.socket.joinGroup(new InetSocketAddress(group, port), network_inteface);
        this.socket.joinGroup(new InetSocketAddress(group, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        LOG.info("Entrou no grupo multicast: " + group.getHostAddress() + ":" + port+ "\n");
    }

    public void leave() throws IOException {
        this.getSocket().leaveGroup(
                new InetSocketAddress(this.getGroup(), this.getPort()),
                //network_inteface);
                 NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        LOG.info("Saiu do grupo multicast: " + group.getHostAddress() + ":" + port+ "\n");
    }

    public void close() {
        this.socket.close();
    }

    public int getBuffer_size() {
        return buffer_size;
    }

    public void setBuffer_size(int buffer_size) {
        this.buffer_size = buffer_size;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    public void setSocket(MulticastSocket socket) {
        this.socket = socket;
    }

    public InetAddress getGroup() {
        return group;
    }

    public void setGroup(InetAddress group) {
        this.group = group;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public NetworkInterface getNetwork_inteface() {
        return network_inteface;
    }

    public void setNetwork_inteface(NetworkInterface network_inteface) {
        this.network_inteface = network_inteface;
    }

    @Override
    public String toString() {
        return "NetworkHandler{"
                + "\nsocket=" + socket
                + ",\n group=" + group
                + ",\n port=" + port + '}';
    }
}
