package util;

import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.Location;
import com.hit.dm.Place;
import com.hit.graph.Edge;
import com.hit.graph.Graph;
import com.hit.graph.Vertex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;

public class CLI implements Runnable {
    private Scanner input;
    private PrintWriter output;
    private HashSet<Runnable> propertyChangeListeners = new HashSet<>();
    private ArrayList<Thread> runningThreads = new ArrayList<>();

    public CLI (InputStream in, OutputStream out) {
        input = new Scanner(in);
        output = new PrintWriter(out);
    }

    public void addPropertyChangeListener(Runnable listener) {
       propertyChangeListeners.add(listener);
    }

    @Override
    public void run() {
        String clientInput = null;
        while(true) {
            output.println("Please input your command. Write 'help' to get available options.");
            output.flush();
            clientInput = input.nextLine();
            switch (clientInput.toLowerCase(Locale.ROOT)) {
                case "start":
                    for (Runnable runnable: propertyChangeListeners) {
                        Thread thread = new Thread(runnable);
                        thread.start();
                        runningThreads.add(thread);
                    }
                    break;
                case "stop":
                    for (Thread thread: runningThreads) {
                        thread.interrupt();
                        runningThreads.remove(thread);
                    }
                case "seed":
                    Location park = new Location("Greenwich Park", "Awesome park");

                    Place A = new Place("St. Mary's Gate");
                    Place B = new Place("Shepherd State Clock");
                    Place C = new Place("Statue of General James Wolfe");
                    Place D = new Place("Queen Elisabeth Oak");
                    Place E = new Place("Anglo-Saxon Cemetery");

                    park.addVertex(A);
                    park.addVertex(B);
                    park.addVertex(C);
                    park.addVertex(D);
                    park.addVertex(E);

                    park.addEdge(new Edge(A, B, 6));
                    park.addEdge(new Edge(B, A, 6));
                    park.addEdge(new Edge(A, D, 1));
                    park.addEdge(new Edge(D, A, 1));
                    park.addEdge(new Edge(B, D, 2));
                    park.addEdge(new Edge(D, B, 2));
                    park.addEdge(new Edge(B, E, 2));
                    park.addEdge(new Edge(E, B, 2));
                    park.addEdge(new Edge(B, C, 5));
                    park.addEdge(new Edge(C, B, 5));
                    park.addEdge(new Edge(C, E, 5));
                    park.addEdge(new Edge(E, C, 5));
                    park.addEdge(new Edge(D, E, 1));
                    park.addEdge(new Edge(E, D, 1));

                    IDao fileStorage = new DaoFileImpl("datasource.txt");
                    try {
                        Files.delete(java.nio.file.Path.of("src/main/resources/datasource.txt"));
                    }
                    catch (NoSuchFileException ex) { }
                    catch (IOException ex) {
                        // File permission problems are caught here.
                        ex.printStackTrace();
                    }

                    fileStorage.saveLocation(park);
                    break;
                case "help":
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Available commands: ");
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append("'start' to start server");
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append("'stop' to stop server");
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append("'seed' to init dataset");
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append("'help' to get help");
                    stringBuilder.append(System.lineSeparator());
                    output.println(stringBuilder);
                    output.flush();
                    break;
                default:
                    output.println("Wrong input, try 'help' to get commands");
                    output.flush();
            }
        }
    }
}
