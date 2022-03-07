package geekbrains.client;

import geekbrains.CommonConstants;
import geekbrains.server.ClientHandler;
import geekbrains.server.ServerCommandConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Scanner scanner;



    private void openConnection() throws IOException {
        initializeNetwork();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String messageFromServer = inputStream.readUTF();
                        if (messageFromServer.startsWith("/w") && messageFromServer.equals(ClientHandler.getNickName())) {

                            System.out.println(messageFromServer);

                        } else
                            System.out.println(messageFromServer);
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String text = scanner.nextLine();
                        if (text.equals(ServerCommandConstants.SHUTDOWN)) {
                            sendMessage(text);
                            closeConnection();
                        } else {
                            sendMessage(text);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initializeNetwork() throws IOException {
        socket = new Socket(CommonConstants.SERVER_ADDRESS, CommonConstants.SERVER_PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }


    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        System.exit(1);
    }
}


