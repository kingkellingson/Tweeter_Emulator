package edu.byu.cs.tweeter.client.model.service;

import android.widget.EditText;
import android.widget.TextView;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ValueObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends Service{

    //for getting the user
    public void GetUser(AuthToken currUserAuthToken, TextView userAlias, ValueObserver<User> getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAlias.getText().toString(), new GetUserHandler(getUserObserver));
        execute(getUserTask);
    }

    //LOGIN TASK
    public void GetLogin(EditText alias, EditText password, AuthenticationObserver getLoginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias.getText().toString(),
                password.getText().toString(), new AuthenticationHandler(getLoginObserver));
        execute(loginTask);
    }

    //REGISTER TASK
    public void GetRegister(EditText firstName, EditText lastName, EditText alias, EditText password, String imageBytesBase64, AuthenticationObserver getRegisterObserver) {
        RegisterTask registerTask = new RegisterTask(firstName.getText().toString(), lastName.getText().toString(),
                alias.getText().toString(), password.getText().toString(), imageBytesBase64, new AuthenticationHandler(getRegisterObserver));
        execute(registerTask);
    }

    //LOGOUT TASK
    public void getLogout(AuthToken authToken, SimpleNotificationObserver getLogoutObserver) {
        LogoutTask logoutTask = new LogoutTask(authToken, new SimpleNotificationHandler(getLogoutObserver));
        execute(logoutTask);
    }

}
