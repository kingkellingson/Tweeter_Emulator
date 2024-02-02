package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.CountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ValueObserver;

public class GetCountHandler extends BackgroundTaskHandler<ValueObserver> {
    public GetCountHandler(ValueObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, ValueObserver observer) {
        int count = data.getInt(CountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
