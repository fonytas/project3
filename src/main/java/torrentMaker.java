
import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;

public class torrentMaker {
    public static void makeTorrent(String file_location,String torrentFile_Dir,String torrent_filename,String master,InetAddress mas) {

        String sharedFile = file_location;

        try {

            String urll = "http://"+master+":9492"+"/"+"announce";
            URI lol = new URI(urll);
            Tracker tracker = new Tracker(new InetSocketAddress(mas,9492));
            tracker.start();
            Torrent torrent = Torrent.create(new File(sharedFile),lol , "createdByAuthor");
            FileOutputStream fos = new FileOutputStream(torrentFile_Dir+torrent_filename);
            torrent.save( fos );
            fos.close();
            tracker.announce((TrackedTorrent.load(new File(torrentFile_Dir+torrent_filename))));

        } catch ( Exception e ) {
        }

    }

}
