import java.io.*;
import java.net.*;
import java.util.Date;

import java.util.Scanner;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;


public class ServerThread extends Thread {

    private Socket socket;
    private double turnAroundSum;
    private int requestCount;

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

    BufferedReader reader;
    PrintWriter writer;
    
    
    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));


            String text = reader.readLine();
            
            
            double turnaroundStart, turnAroundEnd;
 
            do {
                turnAroundSum = 0;
                requestCount = 0;
                turnaroundStart = startTime();

                // Performs the request commands
                switch(text) {
                    case "1":
                        turnAroundEnd = endTime(turnaroundStart);
                        writer.write("Date and Time: " + new Date().toString() + " | Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();
                        requestCount++;
                        turnAroundSum += turnAroundEnd;
                        break;
                    case "2":
                        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
                        turnAroundEnd = endTime(turnaroundStart);
                        writer.write("Server uptime: " + uptime + " milliseconds | Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();

                        break;
                    case "3":
                        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
                        long usedMemory = heapMemoryUsage.getUsed();
                        long maxMemory = heapMemoryUsage.getMax();                        
                        turnAroundEnd = endTime(turnaroundStart);
                        writer.write("Memory Use: " + usedMemory + " bytes used out of " + maxMemory + " bytes | Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();

                        break;
                    case "4":
                        Runtime runtime = Runtime.getRuntime();
                        String[] commands = {"netstat", ""};
                        Process netstatProc = runtime.exec(commands);
                        BufferedReader netstatReader = new BufferedReader(new InputStreamReader(netstatProc.getInputStream()));
                        //BufferedReader netstatErrorReader = new BufferedReader(new InputStreamReader(netstatProc.getErrorStream()));

                        String s = null;
                        while ((s = netstatReader.readLine()) != null) {
                            writer.print(s);
                        }

                        turnAroundEnd = endTime(turnaroundStart);

                        writer.close();
                        
                        //writer.flush();

                        break;
                    case "5":
                        turnAroundEnd = endTime(turnaroundStart);
                        //writer.print("Current Users: Not sure how to find this yet | Turnaround Time: " + turnAroundEnd + "ms\n");
                        try {
                            ProcessBuilder pb = new ProcessBuilder("who");
                            Process process = pb.start();
                            Scanner scanner = new Scanner(process.getInputStream());
                            while (scanner.hasNextLine()) {
                                writer.write(scanner.nextLine() + " | Turnaround Time: " + turnAroundEnd + "ms\n");
                                writer.flush();
                            }
                            scanner.close();
                        } catch (IOException e) {
                            writer.write("Error executing who command: " + e.getMessage());
                            writer.flush();
                        }
                        writer.flush();

                        break;
                    case "6":
                        turnAroundEnd = endTime(turnaroundStart);
                        Process runningproc = Runtime.getRuntime().exec("ps -aux");
                        BufferedReader runningprocBR = new BufferedReader(new InputStreamReader(runningproc.getInputStream()));
                        writer.write("Running Processes: " + runningprocBR.readLine() + " | Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();

                        break;

                }

            

            } while (!text.equals("bye"));

            


        } catch (IOException e) {

        } finally {

            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                writer.close();
            }
        }
        
    } //end run

}

