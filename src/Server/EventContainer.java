package Server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class EventContainer
{
    private ArrayList<Event> events;
    private Encoder encoder;

    EventContainer(Encoder encoder)
    {
        events = new ArrayList<Event>();
        this.encoder = encoder;
    }

    void insertEvent(String event)
    {
        String[] spl = event.split(",");

        if (spl.length == 8)
        {
            //spl[7] = ip , spl[4] = name , spl[2] = context
            events.add(new Event(spl[7], spl[4], spl[2]));
        }
    }

    void writeToFile(File tempFile) throws IOException
    {
        for (Event e : events)
        {
            e.encode(encoder);
        }

        encoder.writeToFile(tempFile);
    }
}
