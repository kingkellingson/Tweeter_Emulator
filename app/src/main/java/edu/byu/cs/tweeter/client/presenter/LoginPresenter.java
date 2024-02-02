package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.client.presenter.views.LoginView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    private LoginView view;
    private UserService userService;

    public LoginPresenter (LoginView view) {
        this.view = view;
        userService = new UserService();
    }

    //Get Login Task
    public void doLogin(EditText alias, EditText password) {
        userService.GetLogin(alias, password, new GetLoginObserver());
    }

    public class GetLoginObserver implements AuthenticationObserver {

        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {


            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            view.createIntent(loggedInUser);
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage("Failed to login because of exception: " + exception.getMessage());
        }
    }
}
