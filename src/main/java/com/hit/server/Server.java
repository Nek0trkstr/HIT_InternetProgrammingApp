package com.hit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private ArrayList<HandleRequest> clientThreads = new ArrayList<>();
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public Server(int port) {
        this.port = port;
        try {
            ServerSocket listener = new ServerSocket(port);
            Socket client = listener.accept();
            HandleRequest requestHandler = new HandleRequest(client);
            clientThreads.add(requestHandler);
            threadPool.execute(requestHandler);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
