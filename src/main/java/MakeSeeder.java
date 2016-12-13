import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;

public class MakeSeeder {
    public static void download(String torrent_file_path, String original_file_dir) throws InterruptedException {
        try {

            final progressBar it = new progressBar();

            JFrame frame = new JFrame("Torrent");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(it);
            frame.pack();
            frame.setVisible(true);

            String MyIP = RetrieveAddress.getMyIP();
            Client client = new Client(InetAddress.getByName(MyIP), SharedTorrent.fromFile(new File(torrent_file_path), new File(original_file_dir)));
            client.setMaxDownloadRate(0);
            client.setMaxUploadRate(0);

            client.addObserver(new Observer() {
                @Override
                public void update(Observable observable, Object data) {
                    Client client = (Client) observable;

                    final int progress = Math.round(client.getTorrent().getCompletion());
                    long downloaded = client.getTorrent().getDownloaded();
                    long uploaded = client.getTorrent().getUploaded();
                    try {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                it.updateBar(progress);
                            }
                        });
                        java.lang.Thread.sleep(100);
                    } catch (InterruptedException e) {
                        ;
                    }
                }
            });

            client.download();
            client.share(3600);
            client.waitForCompletion();

        } catch (UnknownHostException e) {
//            System.out.println(e.getMessage());
        } catch (IOException e) {
//            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
        } catch (Exception e) {
//            e.printStackTrace();
        }

    }
}
