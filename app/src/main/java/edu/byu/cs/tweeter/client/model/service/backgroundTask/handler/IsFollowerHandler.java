package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ValueObserver;

public class IsFollowerHandler extends BackgroundTaskHandler<ValueObserver<Boolean>> {

    public IsFollowerHandler(ValueObserver<Boolean> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, ValueObserver<Boolean> observer) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }
}
