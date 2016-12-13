import java.io.File;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;


public class Handle {

    static String torrent_name = "new_file.torrent";
    static String checkmaster="M";
    static String tcp_port="6785";
    static String path;
    static String filetosend;
    static String windowName = System.getProperty("user.name").toUpperCase();


    static ConcurrentSkipListSet<String> get_list() {
        return UDPserv.IP_list();
    }

    static String get_master_ip(String getsubstring) {
        ConcurrentSkipListSet<String> att = UDPserv.IP_list();
        for (String x : att) {
            if (x.startsWith(getsubstring)) {
                String IP_master = x.substring(getsubstring.length());
                return IP_master;
            }
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        Thread udpRegist = new Thread(new UDPregister());
        Thread udpRegistM = new Thread(new UDPregisterM(checkmaster));
        Thread serv = new Thread(new UDPserv(checkmaster));
        udpRegist.start();
        serv.start();
        path = "C:/Users/" + windowName + "/Desktop/";

        System.out.println("Please input file name if you are a master|| Please type C if you are a client");
        Scanner ujm = new Scanner(System.in);
        filetosend = ujm.nextLine();

        String type = "";
        boolean found_status = false;
        while (found_status == false) {
            File file = new File(path + filetosend);
            if (file.exists()) {
                found_status = true;
                type = "master";
            }
            if (get_master_ip(checkmaster).equals("")) {
                continue;
            } else {
                type = "client";
                break;
            }
        }
        if (type.equals("master")) {
            udpRegist.stop();
            udpRegistM.start();

            String IPP = RetrieveAddress.getMyIP();
            Thread give_torrent = new Thread(new ServerSide(tcp_port,path+torrent_name));
            torrentMaker.makeTorrent(path+ filetosend,path,torrent_name,IPP,InetAddress.getByName(IPP));

            give_torrent.start();

            MakeSeeder.download(path+torrent_name,path);

        } else if (type.equals("client")) {
            String torrentGetter_url = get_master_ip(checkmaster);
            Thread.sleep(5000);
            ClientSide s = new ClientSide(torrentGetter_url,tcp_port,path+torrent_name);
            s.send_request();
            MakeSeeder.download(path+torrent_name,path);
        }
    }
}


