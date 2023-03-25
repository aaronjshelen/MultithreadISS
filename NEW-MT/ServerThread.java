import java.io.*;
import java.net.*;
import java.util.Date;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;


public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);


            String text;
 
            do {
                
                text = reader.readLine();

                // Performs the request commands
                switch(text) {
                    case "1":
                        writer.println("Date and Time: " + new Date().toString());
                        break;
                    case "2":
                        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
                        writer.println("Server uptime: " + uptime + " milliseconds");
                        break;
                    case "3":
                        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
                        long usedMemory = heapMemoryUsage.getUsed();
                        long maxMemory = heapMemoryUsage.getMax();
                        writer.println("Memory Use: " + usedMemory + " bytes used out of " + maxMemory + " bytes");
                        break;
                    case "4":
                        writer.println("Netstat: Not sure how to find this yet");
                        break;
                    case "5":
                        writer.println("Current Users: Not sure how to find this yet");
                        break;
                    case "6":
                        writer.println("Running Processes: Not sure how to find this yet");
                        break;
                }

 
            } while (!text.equals("bye"));

        } catch (IOException e) {

        }



    }

}