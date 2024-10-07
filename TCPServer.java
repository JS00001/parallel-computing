import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        // Variables for setting up connection and communication
        Socket Socket = null; // socket to connect with ServerRouter
        PrintWriter out = null; // for writing to ServerRouter
        BufferedReader in = null; // for reading form ServerRouter
        int serverPort = Integer.parseInt(args[1].split(":")[1]); // port number

        String routerHost = "127.0.0.1"; // ServerRouter host name
        int routerPort = 5555; // ServerRouter port

        // Tries to connect to the ServerRouter
        try {
            Socket = new Socket(routerHost, routerPort);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));

            // Send the port of this server to the server
            out.println(serverPort);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerHost);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerHost);
            System.exit(1);
        }

        // Variables for message passing
        String fromServer; // messages sent to ServerRouter
        String fromClient; // messages received from ServerRouter
        String clientAddress = args[0]; // destination IP (Client)

        // Communication process (initial sends/receives)
        out.println(clientAddress); // initial send (IP of the Client)
        fromClient = in.readLine(); // initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromClient);

        // Communication while loop
        while ((fromClient = in.readLine()) != null) {
            System.out.println("Client said: " + fromClient);
            fromServer = fromClient.toUpperCase(); // converting received message to upper case
            System.out.println("Server said: " + fromServer);
            out.println(fromServer); // sending the converted message back to the Client via ServerRouter

            // exit statement
            if (fromClient.equals("Bye.")) {
                System.out.println("Server: terminating...");
                break;
            }
        }

        // closing connections
        out.close();
        in.close();
        Socket.close();
    }
}