package util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
                case "help":
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Available commands: ");
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append("'start' to start server");
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append("'stop' to stop server");
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
