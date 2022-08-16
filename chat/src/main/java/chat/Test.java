package chat;

import org.jivesoftware.smack.XMPPException;

import chat.client.Client;

public class Test{
    public static void main(String[] args) throws XMPPException {
        Client client;
        // orlandocabrera@conference.alumchat.fun
        client = new Client("","","", false);
        client.setServer("alumchat.fun");
        client.setUsername("orlandotest");
        client.setPassword("123456789");
        client.connect();
        client.login();
        client.recieveMessage();
        client.joinRoom("orlandocabrera@conference.alumchat.fun", "orlando");
        //client.addFriend("zbot@alumchat.fun");
        String x = System.console().readLine();
        //client.sendMessageGroup("Mensaje de room", "orlandocabrera@conference.alumchat.fun");
        client.sendMessageGroup("Mensaje de room", "orlandocabrera@conference.alumchat.fun");
        //client.sendFile("orlandocabrera@alumchat.fun/4ujb77gm9f", "nine2.txt");
        x = System.console().readLine();
        client.disconect();
    }
}
