import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Scanner;

public class Client {
    
    public static void main(String[] args) {

        // if(args.length < 2) {
        //     return;
        // }

        // String hostname = args[0];
        // int port = Integer.parseInt(args[1]);

        Scanner scnr = new Scanner(System.in);

        System.out.println("Enter hostname: ");
        String hostname = scnr.nextLine();
        System.out.println("Enter port: ");
        int port = scnr.nextInt();

        try(Socket socket = new Socket(hostname, port)) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            Console console = System.console();
            String text;

            do {
                text = console.readLine("Enter a number:\n1) Date and Time\n2) Uptime\n3) Memory Use\n4) Netstat\n5) Current Users\n6) Running Processes\n");

                
                writer.println(text);

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String stuff = reader.readLine();
                System.out.println(stuff);

            } while(!text.equals("bye"));


        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }

    }

}
