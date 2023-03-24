import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{

    //responsible for listening for incoming connections/clients +
    //creating socket object to communicate w them
    private ServerSocket serverSocket;

    // socket constructor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // responsible for keeping server running!
    // we want the server running until server socket is closed
    public void startServer() {
        
        try {

            // while server socket is not closed, we are waiting for client to connect
            while(!serverSocket.isClosed()) {

                
                /*
                 * listens for a connection to be made to this socket and accepts it.
                 * this is a blocking method - program will be halted here til a client connects.
                 * when a client does connect, a socket object is returned which can be used to communicate w client.
                 */
                Socket socket = serverSocket.accept();

                System.out.println("A new client has connected :D");

                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = Thread(clientHandler);
                thread.start();

            }

        } catch (IOException e) {


        }


    }

    /**
     * deals with Thread
     * @param clientHandler
     * @return
     */
    private Thread Thread(ClientHandler clientHandler) {
        return null;
    }

    /**
     * if an error occurs, shuts down server socket
    */
    public void closeServerSocket() {
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
        
    }



}