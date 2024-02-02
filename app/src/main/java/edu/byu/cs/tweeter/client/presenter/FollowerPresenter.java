package edu.byu.cs.tweeter.client.presenter;

import android.widget.TextView;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ValueObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.views.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter {
    private static final int PAGE_SIZE = 10;

    private PagedView<User> view;
    private FollowService followService;
    private UserService userService;

    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public FollowerPresenter(PagedView<User> view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }

    //GETTERS AND SETTERS
    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    //FROM FOLLOWER DEMO
    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            followService.getFollower(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new GetFollowerObserver());
        }

    }

    public class GetFollowerObserver implements PagedObserver<User> {
        @Override
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);

            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addPage(followers);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayInfoMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayInfoMessage("Failed to get followers because of exception: " + exception.getMessage());
        }
    }

    //USER TRY ON MY OWN
    public void onClick(TextView userAlias) {
        userService.GetUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserObserver());
        view.makeToast("Getting user's profile...");
    }

    public class GetUserObserver implements ValueObserver<User> {
        @Override
        public void handleSuccess(User newUser) {
            view.createIntent(newUser);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayInfoMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayInfoMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
