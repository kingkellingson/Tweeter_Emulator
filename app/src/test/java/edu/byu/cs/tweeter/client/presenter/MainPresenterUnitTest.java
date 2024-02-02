package edu.byu.cs.tweeter.client.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.views.MainView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterUnitTest {

    private MainView mockView;
    private UserService mockUserService;
    private StatusService mockStatusService;
    private Cache mockCache;
    private AuthToken mockAuthToken;
    private Status mockStatus;

    private MainPresenter mainPresenterSpy;

    @Before
    public void setup () {
        //Create Mocks
        mockView = Mockito.mock(MainView.class);
        mockUserService = Mockito.mock(UserService.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);
        mockAuthToken = Mockito.mock(AuthToken.class);
        mockStatus = Mockito.mock(Status.class);

        //getting it to return the userService whenever getUserService is called in the view.
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
    //    Mockito.doReturn(mockUserService).when(mainPresenterSpy).getUserService();
        Mockito.when(mainPresenterSpy.getUserService()).thenReturn(mockUserService);
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);
    }

    @Test
    public void testDoLogout_logoutSuccessful() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                //AuthToken authToken = invocation.getArgument(0, AuthToken.class);
                MainPresenter.GetLogoutObserver observer = invocation.getArgument(1, MainPresenter.GetLogoutObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        setupCallDisplayLogout(answer);

        //Mockito.verify(mockCache).clearCache();
        Mockito.verify(mockView).signifyLogout();
    }

    @Test
    public void testDoLogout_logoutFailedWithMessage() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                //AuthToken authToken = invocation.getArgument(0, AuthToken.class);
                MainPresenter.GetLogoutObserver observer = invocation.getArgument(1, MainPresenter.GetLogoutObserver.class);
                observer.handleFailure("the error message");
                return null;
            }
        };

        setupCallDisplayLogout(answer);

        verifyErrorResult("Failed to logout: the error message");
    }

    @Test
    public void testDoLogout_logoutFailedWithException() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                //AuthToken authToken = invocation.getArgument(0, AuthToken.class);
                MainPresenter.GetLogoutObserver observer = invocation.getArgument(1, MainPresenter.GetLogoutObserver.class);
                observer.handleException(new Exception("the exception message"));
                return null;
            }
        };

        setupCallDisplayLogout(answer);

        verifyErrorResult("Failed to logout because of exception: the exception message");
    }

    private void setupCallDisplayLogout(Answer<Void> answer) {
        Mockito.doAnswer(answer).when(mockUserService).getLogout(Mockito.any(), Mockito.any());

        mainPresenterSpy.doLogout(mockAuthToken);
        Mockito.verify(mockView).displayInfoMessage("Logging Out...");
    }

    private void verifyErrorResult(String s) {
        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
        Mockito.verify(mockView).displayInfoMessage(s);
    }

    @Test
    public void testPostStatus_successfulPost () {
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).PostStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.PostStatus(mockAuthToken, mockStatus);
        Mockito.verify(mockView).displayInfoMessage("Posting Status...");

        Mockito.verify(mockView).cancelPostingToast();
        Mockito.verify(mockView).displayInfoMessage("Successfully Posted!");
    }

    @Test
    public void testPostStatus_postingFailedWithMessage () {
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
                observer.handleFailure("the error message");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).PostStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.PostStatus(mockAuthToken, mockStatus);
        Mockito.verify(mockView).displayInfoMessage("Posting Status...");

        verifyErrorResult("Failed to post status: the error message");
    }

    @Test
    public void testPostStatus_postingFailedWithException () {
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);

                observer.handleException(new Exception("the exception message"));
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).PostStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.PostStatus(mockAuthToken, mockStatus);
        Mockito.verify(mockView).displayInfoMessage("Posting Status...");

        verifyErrorResult("Failed to post status because of exception: the exception message");
    }



}
