package Server;

import ca.pfv.spmf.algorithms.frequentpatterns.HUIM_GA.AlgoHUIM_GA;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class WorkerThread implements Runnable
{
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private int minUtil;

    private EventContainer events;
    private Encoder encoder;

    WorkerThread(Socket socket) throws IOException
    {
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

        encoder = new Encoder();
        events = new EventContainer(encoder);
    }


    @Override
    public void run()
    {
        try
        {
            getFile();
            File tempFile = writeToTempFile();
            File outputFile = File.createTempFile("output", ".tmp");

            //System.out.println(tempFile.getAbsolutePath());

            AlgoHUIM_GA algoHUIM_ga = new AlgoHUIM_GA();
            algoHUIM_ga.runAlgorithm(tempFile.getAbsolutePath(), outputFile.getAbsolutePath(), minUtil);
            sendData(outputFile);


            deleteFile(tempFile);
            deleteFile(tempFile);

            input.close();
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void deleteFile(File file)
    {
        if(!file.delete())
        {
            System.out.println("Could not delete file : " + file.getName());
            file.deleteOnExit();
        }
    }


    private void getFile() throws IOException
    {
        String line;
        boolean headerLine = true;
        minUtil = Integer.parseInt(input.readLine());

        while (!(line = input.readLine()).isEmpty())
        {
            if (headerLine)
            {
                headerLine = false;
                continue;
            }

            events.insertEvent(line);
        }
    }

    private File writeToTempFile() throws IOException
    {
        File tempFile = File.createTempFile("received-", ".tmp");
        events.writeToFile(tempFile);
        //tempFile.deleteOnExit();

        return tempFile;
    }

    private void sendData(File outputFile) throws IOException
    {
        BufferedReader outputReader = new BufferedReader(new FileReader(outputFile));
        String line;
        String[] spl;

        while (!(line = outputReader.readLine()).isEmpty())
        {
            spl = line.split("#UTIL:");

            String decoded = encoder.getDecoded(spl[0]);

            String toSend = decoded + " with total cost of" + spl[1] +". ";
            output.println(toSend);
        }
        outputReader.close();
        output.flush();
    }
}
