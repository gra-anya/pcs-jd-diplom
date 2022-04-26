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

            String word = in.readLine();
            BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
            List<PageEntry> pageEntries = engine.search(word);

            Gson gson = new GsonBuilder().create();
            var json = gson.toJson(pageEntries);
            out.println(json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}