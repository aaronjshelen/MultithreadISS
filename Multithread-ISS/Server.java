import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class Server {

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

                Thread thread = new Thread(clientHandler);
                thread.start();

            }

        } catch (IOException e) {


        }


    }

    // /**
    //  * deals with Thread
    //  * @param clientHandler
    //  * @return
    //  */
    // private Thread Thread(ClientHandler clientHandler) {
    //     return null;
    // }

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

    public class ClientHandler implements Runnable{
    
        // keeps track of clients. responsible for broadcasting msgs to multiple clients
        public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        
        // this will be socket that gets passed from our server class
        private Socket socket;
    
        // used to read data (msgs sent from clients)
        private BufferedReader bufferedReader;
    
        // used to send data (msgs sent to clients)
        private BufferedWriter bufferedWriter;
    
        // identifies clients
        private String clientUsername;
    
        // constructor
        public ClientHandler(Socket socket) {
    
            try {
                this.socket = socket;
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.clientUsername = bufferedReader.readLine();
                clientHandlers.add(this);
                broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
    
        }
        
    
    
        @Override
        /**
         * everything on this method is run an a separate thread
         */
        public void run() {
            String messageFromClient;
    
            // while socket is connected, we listen for clients
            while(socket.isConnected()) {
                try {
                    /*
                     * program will halt here til we receive msg from client.
                     * we want to run this on a separate thread so our program
                     * isn't stopped at this line of code.
                     */
                    messageFromClient = bufferedReader.readLine(); 
                    broadcastMessage(messageFromClient);
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }
    
        /**
         * broadcasts msg we want to send
         * @param messageToSend
         */
        public void broadcastMessage(String messageToSend) {
    
            //we can loop thru arraylist and send msg to each client connected!
            for(ClientHandler clientHandler : clientHandlers){
                try {
                    if(!clientHandler.clientUsername.equals(clientUsername)) {
                        clientHandler.bufferedWriter.write(messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
    
        }
    
        /**
         * alerts if a client left the chat
         */
        public void removeClientHandler() {
            clientHandlers.remove(this);
            broadcastMessage("Server: " + clientUsername + " has left the chat!");
        }
    
    
        public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
            removeClientHandler();
            try {
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
                if(bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if(socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
    }



}