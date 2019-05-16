package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable
{
    private int port;
    private boolean listening;
    private ConsoleInput cInput;
    private ServerSocket serverSocket;
    private ExecutorService executor;

    private Server(int port)
    {
        this.port = port;
        listening = false;

        serverSocket = null;
        executor = null;
    }

    public static void main(String[] args)
    {
        if (args.length != 1 || !args[0].matches("(\\d)+"))
        {
            System.out.println("Invalid parameters");
            return;
        }
        Server server = new Server(Integer.parseInt(args[0]));
        Thread serverThr = new Thread(server);

        serverThr.start();
    }

    private void beginListening() throws IOException
    {
        listening = true;
        cInput = new ConsoleInput(this);
        Thread cInputThr = new Thread(cInput);
        cInputThr.start();

        executor = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(port);

        while (listening)
        {
            try
            {
                listen();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        //if we want not to wait for people to finish
        //executor.shutdownNow();
    }

    private void listen() throws IOException
    {

        Socket socket = null;
        try
        {
            socket = serverSocket.accept();
        }
        catch (IOException ignored)
        {
            listening = false;
            return;
        }

        WorkerThread worker = new WorkerThread(socket);
        executor.execute(worker);

    }

    void stopListening()
    {
        try
        {
            serverSocket.close();
        }
        catch (IOException ignored)
        {
            listening = false;
        }
        listening = false;
    }

    @Override
    public void run()
    {
        try
        {
            beginListening();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
