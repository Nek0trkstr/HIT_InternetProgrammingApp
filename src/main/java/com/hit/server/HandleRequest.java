package com.hit.server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.Location;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;

public class HandleRequest implements Runnable {
    private Socket socket;
    private Controller tripMapController = new Controller();

    public HandleRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run()  {
        try(Scanner input = new Scanner(new InputStreamReader(socket.getInputStream()))) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String reqString = input.next();
            Gson gson = new Gson();

            Type ref = new TypeToken<Request<Location>>(){}.getType();
            Request<Location> request= gson.fromJson(reqString, ref);

            String action = request.getHeaders().getAction();
            String controller = request.getHeaders().getController();

            Location location = request.getBody();
            switch (controller) {
                case "TripMap":
                    switch (action) {
                        case "GetLocation":
                            Location respLocation = tripMapController.getLocation(location.getName());
                            writer.println(new Response<Location>(200, respLocation));
                            break;
                        case "CreateLocation":
                            tripMapController.createLocation(location);
                            writer.println(new Response<Location>(201, null));
                            break;
                        case "EditLocation":
                            tripMapController.editLocation(location);
                            break;
                        case "DELETE":
                            tripMapController.deleteLocation(location.getName());
                            break;
                    }
            }
            writer.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
