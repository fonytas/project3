
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;


public class UDPregister implements Runnable {

    DatagramSocket c = null;

    public void run(){
        try{
            c = new DatagramSocket();
            c.setBroadcast(true);
            byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();
            while (true){
                try{
                    byte[] buf = new byte[256];
                    InetAddress ip = InetAddress.getByName("255.255.255.255");
                    DatagramPacket packet = new DatagramPacket(sendData,sendData.length,ip,8686);
                    c.send(packet);
                    //System.out.println(getClass().getName() + ": Request packet sent to: 255.255.255.255 (DEFAULT)");

                }catch(IOException e){
//                    e.printStackTrace();
                }

                // Broadcast the message over all the network interfaces
                Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                    if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                        continue; // Don't want to broadcast to the loopback interface
                    }

                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        InetAddress broadcast = interfaceAddress.getBroadcast();
                        if (broadcast == null) {
                            continue;
                        }
                        try {
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8686);
                            c.send(sendPacket);
                        } catch (Exception e) {
                        }
                    }
                }

                byte[] recvBuf = new byte[15000];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                c.receive(receivePacket);

                String message = new String(receivePacket.getData()).trim();
                if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {

                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            // Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}