package Server;

class Event
{
    private String ip;
    private String type;
    private String description;

    Event(String ip, String type, String description)
    {
        this.ip = ip;
        this.type = type;
        this.description = description;
    }

    void encode(Encoder encoder)
    {
        String toEncode = type + ":" + description;

        encoder.encode(toEncode, type,ip);
    }
}
