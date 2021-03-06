package com.hit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private int port;
    private ArrayList<HandleRequest> clientThreads = new ArrayList<>();
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println( String.format("Server is listening on port %d", port));
        try (ServerSocket listener = new ServerSocket(port)) {
            while (true) {
                Socket client = listener.accept();
                HandleRequest requestHandler = new HandleRequest(client);
                clientThreads.add(requestHandler);
                threadPool.execute(requestHandler);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
