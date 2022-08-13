package chat;
import org.jivesoftware.smack.XMPPException;

import chat.client.Client;

public class App 
{
    public static int principalMenu() {
        System.out.print("\nEscoja una opción");
        System.out.print("\n1. Crear cuenta");
        System.out.print("\n2. Iniciar sesión");
        System.out.print("\n3. Salir");
        System.out.print("\n-> ");
        int option = Integer.parseInt(System.console().readLine());
        return option;
    }

    public static int mainMenu(){
        System.out.print("\nEscoja una opción");
        System.out.print("\n1. Enviar mensaje");
        System.out.print("\n2. Ver mensajes");
        System.out.print("\n3. Salir");
        System.out.print("\n-> ");
        int option = Integer.parseInt(System.console().readLine());
        return option;
    }
    public static String[] loginOptions(){
        String username = "";
        char[] password = {};
        // loops while the user doesnt enter a valid username, password or hostname
        while (username.length() == 0) {
            System.out.print("\nIngrese su nombre de usuario: ");
            username = System.console().readLine();
        }
        while (password.length == 0) {
            System.out.print("\nIngrese su contraseña: ");
            password = System.console().readPassword();
        }
        return new String []{username, String.valueOf(password)};
    }

    public static String hostName(){
        String host = "";
        while (host.length() == 0) {
            System.out.print("\nIngrese el host del servidor: ");
            host = System.console().readLine();
        }
        return host;
    }
    public static void main( String[] args ) throws XMPPException 
    {
        boolean run = false;
        String[] infoUser;
        String message, to;
        int option, n = 3;
        String host = "";
        Client client = new Client ("","","",true);
        host = hostName();
        client.setServer(host);
        while (!client.connect()){
            System.out.printf("No se ha logrado conectar con el host...\n");
            System.out.printf("Ingrese nuevamente el host \n");
            host = hostName();
            client.setServer(host);
        }
        infoUser = loginOptions();
        client.setUsername(infoUser[0]);
        client.setPassword(infoUser[1]);
        while (!client.login()){
            System.out.printf("No se ha logrado iniciar sesión, contraseña o usuario incorrectos...\n");
            infoUser = loginOptions();
            client.connect();
            client.setUsername(infoUser[0]);
            client.setPassword(infoUser[1]);
        }
        client.recieveMessage();
        option = mainMenu();
        while (option < n){
            switch (option) {
                case 1:
                    System.out.print("\nIngrese el nombre del usuario para mandar un mensaje: ");
                    to = System.console().readLine();
                    System.out.print("\nIngrese el mensaje: ");
                    System.out.print("\n-> ");
                    message = System.console().readLine();
                    client.sendMessage(message,to);
                    option = mainMenu();
                    break;
                case 2:
                    option = mainMenu();
                    break;
                case 3:
                    option = n;
                    break;
                default:
                    System.out.print("\nIngrese una opción válida: ");
                    option = mainMenu();
                    break;
            }
        }
        client.disconect();
    }
}
