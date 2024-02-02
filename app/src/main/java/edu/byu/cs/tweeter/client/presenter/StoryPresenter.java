package edu.byu.cs.tweeter.client.presenter;

import android.widget.TextView;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ValueObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.views.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {
    private static final int PAGE_SIZE = 10;

    private PagedView<Status> view;
    private StatusService statusService;
    private UserService userService;

    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;

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

    public StoryPresenter (PagedView<Status> view) {
        this.view = view;
        statusService = new StatusService();
        userService = new UserService();
    }

    //Get Story Task
    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetStoryObserver());

        }

    }

    public class GetStoryObserver implements PagedObserver<Status> {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

            setHasMorePages(hasMorePages);
            view.addPage(statuses);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayInfoMessage("Failed to get story: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayInfoMessage("Failed to get story because of exception: " + exception.getMessage());
        }
    }


    //USER TASK
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
