import java.io.*;
import java.net.*;
import java.util.Date;

import javax.swing.event.TableColumnModelListener;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;


public class ServerThread extends Thread {

    private Socket socket;

    public double startTime() {
        double start = System.currentTimeMillis();
        return start;
    }

    public double endTime(double start) {
        double end = System.currentTimeMillis();
        return (end - start);
    }

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


            String text = reader.readLine();

            double turnaroundStart, turnAroundEnd;
 
            do {
                turnaroundStart = startTime();

                // Performs the request commands
                switch(text) {
                    case "1":
                        writer.println("Date and Time: " + new Date().toString());

                        turnAroundEnd = endTime(turnaroundStart);

                        writer.print("Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();
                        break;
                    case "2":
                        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
                        writer.println("Server uptime: " + uptime + " milliseconds");
                        
                        turnAroundEnd = endTime(turnaroundStart);

                        writer.print("Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();
                        break;
                    case "3":
                        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
                        long usedMemory = heapMemoryUsage.getUsed();
                        long maxMemory = heapMemoryUsage.getMax();
                        writer.println("Memory Use: " + usedMemory + " bytes used out of " + maxMemory + " bytes");
                        
                        turnAroundEnd = endTime(turnaroundStart);

                        writer.print("Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();

                        break;
                    case "4":
                        writer.println("Netstat: Not sure how to find this yet");
                        
                        turnAroundEnd = endTime(turnaroundStart);

                        writer.print("Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();

                        break;
                    case "5":
                        writer.println("Current Users: Not sure how to find this yet");

                        turnAroundEnd = endTime(turnaroundStart);

                        writer.print("Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();
                        break;
                    case "6":
                        writer.println("Running Processes: Not sure how to find this yet");

                        turnAroundEnd = endTime(turnaroundStart);

                        writer.print("Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();

                        break;
                }

            } while (!text.equals("bye"));

        } catch (IOException e) {

        }

    } //end run

}

