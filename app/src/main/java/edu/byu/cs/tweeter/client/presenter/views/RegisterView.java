package edu.byu.cs.tweeter.client.presenter.views;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface RegisterView extends ParentView {
    void createIntent(User registeredUser, AuthToken authToken);
}
