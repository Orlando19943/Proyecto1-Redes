package chat;

import org.jivesoftware.smack.XMPPException;

import chat.client.Client;

public class Test {
    public static void main(String[] args) throws XMPPException {
        Client client;
        client = new Client("","","", false);
        client.setServer("alumchat.fun");
        client.connect();
        client.setUsername("orlandotest");
        client.setPassword("123456789");
        client.login();
        //client.sendMessage("Mensaje de prueba", "orlandocabrera@alumchat.fun");
        //client.recieveMessage();
        //String x = System.console().readLine();
        client.disconect();
    }
}
