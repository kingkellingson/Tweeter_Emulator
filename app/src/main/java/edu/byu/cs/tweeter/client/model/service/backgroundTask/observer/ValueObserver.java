package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

public interface ValueObserver<T> extends ServiceObserver{
    void handleSuccess(T value);
}
