import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

public class Server  {



    public static void main(String[] args) {

        Scanner scnr = new Scanner(System.in);
        int port;
        System.out.println("Enter port:");
        port = Integer.parseInt(scnr.nextLine());
        double turnAroundSum = 0;
        int requestCount = 0;



        try(ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected\n");
                ServerThread serverThread = new ServerThread(socket, turnAroundSum, requestCount);
                serverThread.start();

                System.out.println("Turn Around Sum: " + serverThread.getSum() + "\n");
                System.out.println("Turn Around Average: " + serverThread.getSum()/serverThread.getCount() + "\n");

            }

        
        } catch (IOException e) {
            System.out.println("Server exepction: " + e.getMessage());
            e.printStackTrace();
        }


    }
    public static class ServerThread extends Thread {

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
    
    
        public ServerThread(Socket socket, double turnAroundSum, int requestCount) {
            this.socket = socket;
            this.turnAroundSum = turnAroundSum;
            this.requestCount = requestCount;
        }

        public int getCount(){
            return requestCount;
        }

        public double getSum () {
            return turnAroundSum;
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
                            turnAroundEnd = endTime(turnaroundStart);
                            long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
                            writer.write("Server uptime: " + uptime + " milliseconds | Turnaround Time: " + turnAroundEnd + "ms\n");
                            writer.flush();
                            requestCount++;
                            turnAroundSum += turnAroundEnd;
                            break;
                        case "3":
                            turnAroundEnd = endTime(turnaroundStart);   
                            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
                            long usedMemory = heapMemoryUsage.getUsed();
                            long maxMemory = heapMemoryUsage.getMax();                        
                            writer.write("Memory Use: " + usedMemory + " bytes used out of " + maxMemory + " bytes | Turnaround Time: " + turnAroundEnd + "ms\n");
                            writer.flush();
                            requestCount++;
                            turnAroundSum += turnAroundEnd;
                            break;
                        case "4":
                            turnAroundEnd = endTime(turnaroundStart);
                            Runtime runtime = Runtime.getRuntime();
                            String[] commands = {"netstat", ""};
                            Process netstatProc = runtime.exec(commands);
                            BufferedReader netstatReader = new BufferedReader(new InputStreamReader(netstatProc.getInputStream()));
                            //BufferedReader netstatErrorReader = new BufferedReader(new InputStreamReader(netstatProc.getErrorStream()));
    
                            String s = null;
                            while ((s = netstatReader.readLine()) != null) {
                                writer.print(s);
                                writer.flush();
                            }
    
                            
    
                            writer.close();
                            requestCount++;
                            turnAroundSum += turnAroundEnd;
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
                            requestCount++;
                            turnAroundSum += turnAroundEnd;
                            break;
                        case "6":
                            turnAroundEnd = endTime(turnaroundStart);
                            Process runningproc = Runtime.getRuntime().exec("ps -aux");
                            BufferedReader runningprocBR = new BufferedReader(new InputStreamReader(runningproc.getInputStream()));
                            writer.write("Running Processes: " + runningprocBR.readLine() + " | Turnaround Time: " + turnAroundEnd + "ms\n");
                            writer.flush();
                            requestCount++;
                            turnAroundSum += turnAroundEnd;
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
}
