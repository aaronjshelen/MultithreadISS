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

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);


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
                        writer.print("Date and Time: " + new Date().toString() + " | Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();
                        requestCount++;
                        turnAroundSum += turnAroundEnd;
                        break;
                    case "2":
                        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
                        turnAroundEnd = endTime(turnaroundStart);
                        writer.print("Server uptime: " + uptime + " milliseconds | Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();

                        break;
                    case "3":
                        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
                        long usedMemory = heapMemoryUsage.getUsed();
                        long maxMemory = heapMemoryUsage.getMax();                        
                        turnAroundEnd = endTime(turnaroundStart);
                        writer.print("Memory Use: " + usedMemory + " bytes used out of " + maxMemory + " bytes | Turnaround Time: " + turnAroundEnd + "ms\n");
                        writer.flush();

                        break;
                    case "4":
                        turnAroundEnd = endTime(turnaroundStart);
                        Process netstatProc = Runtime.getRuntime().exec("netstat -a");
                        BufferedReader br = new BufferedReader(new InputStreamReader(netstatProc.getInputStream()));

                        String str;
                        str = br.readLine();

                        // do {
                        //     str = br.readLine();
                        //     //writer.println(str + " | Turnaround Time: " + turnAroundEnd + "ms\n");
                        // } while(str != null);

                        

                        // while(str != null){
                            
                        //     writer.println(str + " | Turnaround Time: " + turnAroundEnd + "ms\n");
                        //     str = br.readLine();
                        // }

                        
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
                                writer.println(scanner.nextLine() + " | Turnaround Time: " + turnAroundEnd + "ms\n");
                            }
                            scanner.close();
                        } catch (IOException e) {
                            writer.println("Error executing who command: " + e.getMessage());
                        }
                        writer.flush();

                        break;
                    case "6":
                        turnAroundEnd = endTime(turnaroundStart);
                        Process runningproc = Runtime.getRuntime().exec("ps -aux");
                        BufferedReader runningprocBR = new BufferedReader(new InputStreamReader(runningproc.getInputStream()));
                        writer.print("Running Processes: " + runningprocBR.readLine() + " | Turnaround Time: " + turnAroundEnd + "ms\n");
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
            }
            
            writer.close();
        }
        
    } //end run

}

