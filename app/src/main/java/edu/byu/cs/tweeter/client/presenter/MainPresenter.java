package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.concurrent.ExecutorService;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ValueObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.views.MainView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

    private MainView view;
    private UserService userService;
    private FollowService followService;
    private StatusService statusService;

    public MainPresenter (MainView view) {
        this.view = view;
        followService = new FollowService();
    }

    protected UserService getUserService() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    //CLEAR CACHE
    public void ClearCache() {
        Cache.getInstance().clearCache();
    }

    //LOGOUT TASK
    public void doLogout(AuthToken authtoken) {
        view.displayInfoMessage("Logging Out...");
        getUserService().getLogout(authtoken, new GetLogoutObserver());
    }

    public class GetLogoutObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            view.signifyLogout();
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage("Failed to logout because of exception: " + exception.getMessage());
        }
    }

    //IS FOLLOWER TASK
    public void isFollower(AuthToken currUserAuthToken, User currUser, User selectedUser) {
        followService.doIsFollow(currUserAuthToken, currUser, selectedUser, new IsFollowerObserver());
    }

    public class IsFollowerObserver implements ValueObserver<Boolean> {

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage("Failed to determine following relationship because of exception: " + exception.getMessage());
        }

        @Override
        public void handleSuccess(Boolean isFollower) {
            view.setButtonText(isFollower);
        }
    }

    //UNFOLLOW TASK
    public void doUnfollow(AuthToken currUserAuthToken, User selectedUser) {
        followService.getUnfollow(currUserAuthToken, selectedUser, new UnfollowObserver());
    }

    public class UnfollowObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            view.signifyFollow(true);
            view.enableFollowButton();
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to unfollow: " + message);
            view.enableFollowButton();
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage("Failed to unfollow because of exception: " + exception.getMessage());
            view.enableFollowButton();
        }
    }

    //FOLLOW TASK
    public void doFollow(AuthToken currUserAuthToken, User selectedUser) {
        followService.getFollow(currUserAuthToken, selectedUser, new FollowObserver());
    }

    public class FollowObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            view.signifyFollow(false);
            view.enableFollowButton();
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to follow: " + message);
            view.enableFollowButton();
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage("Failed to follow because of exception: " + exception.getMessage());
            view.enableFollowButton();
        }
    }

    //GET FOLLOWING COUNT TASK
    public void GetFollowingCount(ExecutorService executor, AuthToken currUserAuthToken, User selectedUser) {
        followService.GetFollowingCount(executor, currUserAuthToken, selectedUser, new GetFollowingCountObserver());
    }

    public class GetFollowingCountObserver implements ValueObserver<Integer> {

        @Override
        public void handleSuccess(Integer value) {
            view.setFolloweeCount(value);
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage("Failed to get following count because of exception: " + exception.getMessage());
        }
    }

    //GET FOLLOWER COUNT TASK
    public void GetFollowerCount(ExecutorService executor, AuthToken currUserAuthToken, User selectedUser) {
        followService.GetFollowersCount(executor, currUserAuthToken, selectedUser, new GetFollowersCountObserver());
    }

    public class GetFollowersCountObserver implements ValueObserver<Integer> {
        @Override
        public void handleSuccess(Integer value) {
            view.setFollowerCount(value);
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage("Failed to get followers count because of exception: " + exception.getMessage());
        }
    }

    //POST STATUS TASK
    public void PostStatus(AuthToken currUserAuthToken, Status newStatus) {
        view.displayInfoMessage("Posting Status...");
        getStatusService().PostStatus(currUserAuthToken, newStatus, new PostStatusObserver());
    }

    public class PostStatusObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            view.cancelPostingToast();
            view.displayInfoMessage("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage("Failed to post status because of exception: " + exception.getMessage());
        }
    }

}
