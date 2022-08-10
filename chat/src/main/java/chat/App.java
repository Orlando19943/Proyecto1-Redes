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
        String host = "";
        // loops while the user doesnt enter a valid username, password or hostname
        while (username.length() == 0) {
            System.out.print("\nIngrese su nombre de usuario: ");
            username = System.console().readLine();
        }
        while (password.length == 0) {
            System.out.print("\nIngrese su contraseña: ");
            password = System.console().readPassword();
        }
        while (host.length() == 0) {
            System.out.print("\nIngrese el host del servidor: ");
            host = System.console().readLine();
        }
        return new String []{username, String.valueOf(password), host};
    }
    public static void main( String[] args ) throws XMPPException 
    {
        String[] infoUser;
        String message, to;
        int option, n = 3;
        Client client;
        System.out.print("Bienvenido al chat");
        /* option = mainMenu();
        while (option < 0 || option > 3) {
            System.out.print("\nIngrese una opción válida: ");
            option = mainMenu();
        } */
        infoUser = loginOptions();
        client = new Client(infoUser[0], infoUser[1], infoUser[2]);
        client.connect();
        client.login();
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
        client.dissconect();
    }
}
