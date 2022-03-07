package geekbrains.server.authorization;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAuthServiceImpl implements AuthService {
    private final Map<String, UserData> users;

    public InMemoryAuthServiceImpl() {
        users = new HashMap<>();
        users.put("login1", new UserData("login1", "pass1", "first"));
        users.put("login2", new UserData("login2", "pass2", "second"));
        users.put("login3", new UserData("login3", "pass3", "third"));
    }

    @Override
    public void start() {
        System.out.println("Сервис аутентификации инициализирован");
    }

    @Override
    public synchronized String getNickNameByLoginAndPassword(String login, String password) {
        UserData user = users.get(login);
        // Ищем пользователя по логину и паролю, если нашли то возвращаем никнэйм
        if (user != null && user.getPassword().equals(password)) {
            return user.getNickName();
        }

        return null;
    }

    @Override
    public void end() {
        System.out.println("Сервис аутентификации отключен");
    }
}
