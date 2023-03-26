import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.synth.SynthSplitPaneUI;

public class Client extends Thread {
    private String choice;
    private String hostname;
    private int portName;

    public Client(String host, String choice, int port) {
        this.hostname = host;
        this.choice = choice;
        this.portName = port;
    }

    @Override
    public void run() {
        // if(args.length < 2) {
        //     return;
        // }

        // String hostname = args[0];
        // int port = Integer.parseInt(args[1]);

        try(Socket socket = new Socket(this.hostname, this.portName)) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text;

            //System.out.println();
            //int numClients = Integer.parseInt(console.readLine("How many clients would you like to generate (1-25)\n"));

            //writer.println(Integer.toString(numClients));

            text = this.choice;

            //System.out.println("in run: " + text);

                
            writer.println(text);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String stuff = reader.readLine();
            System.out.println(stuff);

        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }

    }
    
    public static void main(String[] args) throws InterruptedException {
        Scanner scnr = new Scanner(System.in);
        String hostname, choice;
        int port, numClients;

        System.out.println("Enter hostname:");
        hostname = scnr.nextLine();
        System.out.println("Enter port:");
        port = Integer.parseInt(scnr.nextLine());

        do {

            System.out.println("\nEnter a number:\n1) Date and Time\n2) Uptime\n3) Memory Use\n4) Netstat\n5) Current Users\n6) Running Processes");
            choice = scnr.nextLine();
            System.out.println("How many clients would you like to generate? (1-25):");
            numClients = Integer.parseInt(scnr.nextLine());
    
            for (int i = 0; i < numClients; i++) {
                new Client(hostname, choice, port).start();
                
            }

            TimeUnit.SECONDS.sleep(1);
            
            System.out.println("Do you want to stop? (Yes or No):");
            choice = scnr.nextLine();

        } while (!(choice.equals("Yes")));

    } //end main

}