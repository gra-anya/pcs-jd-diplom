import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Main {
    static int port = 8989;

    public static void main(String[] args){
//        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
//        System.out.println(engine.search("бизнес"));

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
            Gson gson = new GsonBuilder().create();

            while (true) {
                String word = in.readLine();
                List<PageEntry> pageEntries = engine.search(word);

                var json = gson.toJson(pageEntries);
                out.println(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}