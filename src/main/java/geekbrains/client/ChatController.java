package geekbrains.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;


public class ChatController implements Initializable {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField messageField, loginField;
    @FXML
    private HBox messagePanel, authPanel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ListView<String> clientList;

    private final Client client;


    public ChatController() {
        this.client = new Client(this);

    }

    public void setAuthen(boolean authen) {
        authPanel.setVisible(!authen);
        authPanel.setManaged(!authen);
        messagePanel.setVisible(authen);
        messagePanel.setManaged(authen);
        clientList.setVisible(authen);
        clientList.setManaged(authen);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthen(false);
    }

    public void displayMess(String text) {
        if (textArea.getText().isEmpty()) {
            textArea.setText(text);
        } else {
            textArea.setText(textArea.getText() + "\n" + text);
        }
    }

    public void displayClient(final String nickName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientList.getItems().add(nickName);
            }
        });
    }

    public void removeClient(final String nickName){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientList.getItems().remove(nickName);
            }
        });
    }


    public void sendAuth(ActionEvent actionEvent) {
        boolean authen = client.sendAuth(loginField.getText(), passwordField.getText());
        if (authen){
            loginField.clear();
            passwordField.clear();
            setAuthen(true);

        }

    }

    public void sendMessage(ActionEvent actionEvent) {
    client.sendMessage(messageField.getText());
    }

    public void close(){
        client.closeConnection();
    }

}
