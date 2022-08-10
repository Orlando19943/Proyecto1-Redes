package chat;
import chat.client.Client;

public class App 
{
    public static void main( String[] args ) 
    {
        Client client = new Client("orlandotest", "123456789", "alumchat.fun");
        client.connect();
    }
}
