package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInput implements Runnable
{
    Server server;
    private boolean running;

    ConsoleInput(Server server)
    {
        this.server = server;
        running = true;
    }

    private String call() throws IOException
    {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        String input;
        do
        {
            try
            {
                // wait until we have data to complete a readLine()
                while (!br.ready())
                {
                    Thread.sleep(200);
                }
                input = br.readLine();
            }
            catch (InterruptedException e)
            {
                return null;
            }
        } while ("".equals(input));

        return input;
    }

    @Override
    public void run()
    {
        System.out.println("Server application started.");
        while (running)
        {
            String input = null;
            try
            {
                input = call();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if (input == null)
            {
                continue;
            }

            input = input.toLowerCase();

            switch (input)
            {
                case "exit":
                    server.stopListening();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }
}
