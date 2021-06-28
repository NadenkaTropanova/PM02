package com.salesregister.controller;

import com.salesregister.Main;
import com.salesregister.controller.popup.PopupWindow;
import com.salesregister.request.RegistrationRequest;
import com.salesregister.service.UserService;
import com.salesregister.service.exception.UserAlreadyExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class RegistrationController {
    private final UserService userService;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    void register(ActionEvent event) throws IOException {
        String login = this.login.getText();
        String password = this.password.getText();

        if (login.length() == 0) {
            PopupWindow.openWindow("Ошибка", "Введите логин");
        }

        if (password.length() == 0) {
            PopupWindow.openWindow("Ошибка", "Введите пароль");
        }

        RegistrationRequest request = new RegistrationRequest();
        request.setEmail(login);
        request.setPassword(password);

        try {
            userService.register(request);
            OperationController.load();
        } catch (UserAlreadyExistsException e) {
            PopupWindow.openWindow("Ошибка", "Имя занято");
        }
    }

    @FXML
    void toSignIn(ActionEvent event) throws IOException {
        AuthenticationController.load();
    }

    public static void load() throws IOException {
        FXMLLoader loader = new FXMLLoader(RegistrationController.class.getResource("/registration.fxml"));
        loader.setControllerFactory(Main.getApplicationContext().getBeanFactory()::getBean);
        Parent view = loader.load();
        Main.getStage().setScene(new Scene(view));
        Main.getStage().show();
    }
}