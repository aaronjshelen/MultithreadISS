import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server  {



    public static void main(String[] args) {

        Scanner scnr = new Scanner(System.in);
        int port;
        System.out.println("Enter port:");
        port = Integer.parseInt(scnr.nextLine());

        try(ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ServerThread(socket).start();

            }
        
        
        } catch (IOException e) {
            System.out.println("Server exepction: " + e.getMessage());
            e.printStackTrace();
        }


    }


}
