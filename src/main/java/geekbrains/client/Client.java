package geekbrains.client;

import geekbrains.CommonConstants;
import geekbrains.server.ServerCommandConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private final ChatController chatController;

    public Client(ChatController chatController) {
        this.chatController = chatController;

    }


    private void openConnection() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String messageFromServer = inputStream.readUTF();
                        if (messageFromServer.startsWith(ServerCommandConstants.ENTER)) {
                            String[] client = messageFromServer.split(" ");
                            chatController.displayMess("Пользователь " + client[1]+ " зашел в чат");
                        } else if (messageFromServer.startsWith(ServerCommandConstants.EXIT)) {
                            String[] client = messageFromServer.split(" ");
                            chatController.removeClient(client[1]);
                            chatController.displayMess(client[1] + " вышел из чата");
                        } else if (messageFromServer.startsWith(ServerCommandConstants.CLIENTS)) {
                            String[] client = messageFromServer.split(" ");
                            for (int i = 1; i < client.length; i++) {
                                chatController.displayMess(client[i]);
                            }

                        } else chatController.displayMess(messageFromServer);

                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (true) {
//                        String text = scanner.nextLine();
//                        if (text.equals(ServerCommandConstants.SHUTDOWN)) {
//                            sendMessage(text);
//                            closeConnection();
//                        } else {
//                            sendMessage(text);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
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

    public boolean sendAuth(String login, String password) {
        try {
            if (socket == null || socket.isClosed()) {
                initializeNetwork();
            }
            outputStream.writeUTF(ServerCommandConstants.AUTHORIZATION + " " + login + " " + password);
            boolean authen = inputStream.readBoolean();
            if (authen) {
                openConnection();
            }
            return authen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeConnection() {
        try {
            outputStream.writeUTF(ServerCommandConstants.EXIT);
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        System.exit(1);
    }
}


