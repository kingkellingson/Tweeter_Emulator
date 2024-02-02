package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticationObserver extends ServiceObserver{
    void handleSuccess(User loggedInUser, AuthToken authToken);
}
