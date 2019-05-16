package Client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client
{

    private static String fileName;
    private static int minUtil;
    private static PrintWriter output;
    private static BufferedReader input;

    //checks if first argument is ip address and second is port
    private static boolean validArguments(String[] args)
    {
        return ((args.length == 2) && args[0].matches("(\\d)+(.)(\\d)+(.)(\\d)+(.)(\\d)+(.)(\\d)") && args[1].matches("(\\d)+"));
    }

    private static void askForInput()
    {
        Scanner sc = new Scanner(System.in);

        System.out.print("Set file name: ");
        fileName = sc.nextLine();

        System.out.print("Set minutil parameter: ");
        minUtil = Integer.parseInt(sc.nextLine());
    }

    public static void main(String[] args)
    {
        if (!validArguments(args))
        {
            System.out.println("Invalid arguments. Run program with client <ip address> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        askForInput();

        try (Socket socket = new Socket(host, port))
        {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            output.println(minUtil);
            sendFile();
            output.println("");
            output.flush();

            receiveData();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private static void sendFile() throws IOException
    {
        BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
        String row;

        while ((row = fileReader.readLine()) != null)
        {
            //skip empty rows -- the only empty road we send is the last row to indicate transfer is over
            if (row.isEmpty() || row.trim().equals("") || row.trim().equals("\n"))
            {
                continue;
            }
            output.println(row);
        }
    }

    private static void receiveData() throws IOException
    {
        String toPrint;
        while ((toPrint = input.readLine()) != null)
        {
            System.out.println(toPrint);
        }

        input.close();
        output.close();
    }
}
