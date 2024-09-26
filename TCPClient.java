import java.io.*;
import java.net.*;
import java.util.Arrays;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        Socket Socket = null; // socket to connect with ServerRouter

        PrintWriter out = null; // for writing to ServerRouter
        BufferedReader in = null; // for reading form ServerRouter
        InetAddress addr = InetAddress.getLocalHost();
        String host = addr.getHostAddress(); // Client machine's IP
        int clientPort = Integer.parseInt(args[1].split(":")[1]); // port number

        String routerHost = "127.0.0.1"; // ServerRouter host name
        int routerPort = 5555; // ServerRouter port

        // Tries to connect to the ServerRouter
        try {
            Socket = new Socket(routerHost, routerPort);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));

            // Send the port of this client to the server
            out.println(clientPort);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerHost);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerHost);
            System.exit(1);
        }

        // Variables for message passing
        Reader reader = new FileReader("file.txt");
        BufferedReader fromFile = new BufferedReader(reader); // reader for the string file
        String fromServer; // messages received from ServerRouter
        String fromUser; // messages sent to ServerRouter
        String serverHost = args[0]; // destination IP (Server)
        long t0, t1, t;

        // Communication process (initial sends/receives)
        out.println(serverHost); // initial send (IP of the destination Server)
        fromServer = in.readLine(); //initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromServer);
        out.println(host); // Client sends the IP of its machine as initial send
        t0 = System.currentTimeMillis();

        // Communication while loop
        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            t1 = System.currentTimeMillis();
            if (fromServer.equals("Bye.")) // exit statement
                break;
            t = t1 - t0;
            System.out.println("Cycle time: " + t);

            fromUser = fromFile.readLine(); // reading strings from a file
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                out.println(fromUser); // sending the strings to the Server via ServerRouter
                t0 = System.currentTimeMillis();
            }
        }

        // closing connections
        out.close();
        in.close();
        Socket.close();
    }
}