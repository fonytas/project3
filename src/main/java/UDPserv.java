import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentSkipListSet;


public class UDPserv implements Runnable {
    DatagramSocket socket;

    private String zos;

    public UDPserv(String zos) {
        this.zos = zos;
    }
    static ConcurrentSkipListSet<String> IP = new ConcurrentSkipListSet<>();

    public static ConcurrentSkipListSet IP_list(){
        return IP;
    }

    public void run() {
        try {
            socket = new DatagramSocket(8686, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            while (true) {
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                String message = new String(packet.getData()).trim();

                if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
                    byte[] sendData = "DISCOVER_FUIFSERVER_RESPONSE".getBytes();
                    IP.add(packet.getAddress().getHostAddress());
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);
                }

                if (message.equals(zos)){
                    byte[] sendData = zos.getBytes();
                    IP.add( message+ packet.getAddress().getHostAddress());
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);
                }
            }
        } catch (IOException ex) {
//            Logger.getLogger(UDPserv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}