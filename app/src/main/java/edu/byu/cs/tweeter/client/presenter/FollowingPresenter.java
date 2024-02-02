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

public class FollowingPresenter {
    private static final int PAGE_SIZE = 10;

    private PagedView<User> view;
    private FollowService followService;
    private UserService userService;

    private User lastFollowee;
    private boolean hasMorePages;
    private boolean isLoading = false;


    public FollowingPresenter (PagedView<User> view) {
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

    //FROM FOLLOWING DEMO
    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            followService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());
        }
    }

    public class GetFollowingObserver implements PagedObserver<User> {
        @Override
        public void handleSuccess(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);

            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addPage(followees);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayInfoMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayInfoMessage("Failed to get following because of exception: " + exception.getMessage());
        }
    }

    //USER TRY ON MY OWN
    public void onClick(TextView userAlias) {
        userService.GetUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserObserver());
        view.makeToast("Getting user's profile...");
    }

    public class GetUserObserver implements ValueObserver<User> {
        @Override
        public void handleSuccess(User user) {
            view.createIntent(user);
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
