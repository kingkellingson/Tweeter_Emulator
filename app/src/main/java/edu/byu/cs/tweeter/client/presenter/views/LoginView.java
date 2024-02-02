package edu.byu.cs.tweeter.client.presenter.views;

import edu.byu.cs.tweeter.model.domain.User;

public interface LoginView extends ParentView{
    void createIntent(User loggedInUser);
}
