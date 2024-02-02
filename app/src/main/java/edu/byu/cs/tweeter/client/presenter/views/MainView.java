package edu.byu.cs.tweeter.client.presenter.views;

public interface MainView extends ParentView{
    void signifyLogout();

    void signifyFollow(boolean value);

    void enableFollowButton();

    void setFolloweeCount(int count);

    void setFollowerCount(int count);

    void setButtonText(boolean isFollower);

    void cancelPostingToast();
}
