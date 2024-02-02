package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedItemHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ValueObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service{
    //GetFollowing Handler
    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PagedObserver<User> getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PagedItemHandler<User>(getFollowingObserver));
        execute(getFollowingTask);
    }


    //GetFollower Handler
    public void getFollower(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, PagedObserver<User> getFollowerObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new PagedItemHandler<User>(getFollowerObserver));
        execute(getFollowersTask);
    }


    // GetFollowersCountHandler
    public void GetFollowersCount(ExecutorService executor, AuthToken currUserAuthToken, User selectedUser, ValueObserver getFollowersCountObserver) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetCountHandler(getFollowersCountObserver));
        executor.execute(followersCountTask);
    }

    // GetFollowingCountHandler
    public void GetFollowingCount(ExecutorService executor, AuthToken currUserAuthToken, User selectedUser, ValueObserver getFollowingCountObserver) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(currUserAuthToken,
                selectedUser, new GetCountHandler(getFollowingCountObserver));
        executor.execute(followingCountTask);
    }

    // IsFollowerHandler
    public void doIsFollow(AuthToken currUserAuthToken, User currUser, User selectedUser, ValueObserver<Boolean> isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken, currUser, selectedUser, new IsFollowerHandler(isFollowerObserver));
        execute(isFollowerTask);
    }

    // FollowHandler
    public void getFollow(AuthToken currUserAuthToken, User selectedUser, SimpleNotificationObserver followObserver) {
        FollowTask followTask = new FollowTask(currUserAuthToken,
                selectedUser, new SimpleNotificationHandler(followObserver));
        execute(followTask);
    }

    // UnfollowHandler
    public void getUnfollow(AuthToken currUserAuthToken, User selectedUser, SimpleNotificationObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken,
                selectedUser, new SimpleNotificationHandler(unfollowObserver));
        execute(unfollowTask);
    }
}

