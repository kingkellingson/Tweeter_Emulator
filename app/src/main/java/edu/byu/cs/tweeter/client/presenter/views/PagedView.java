package edu.byu.cs.tweeter.client.presenter.views;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView<T> extends ParentView{
    void setLoadingStatus(boolean value);

    void addPage(List<T> items);

    void createIntent(User newUser);

    void makeToast(String message);
}
