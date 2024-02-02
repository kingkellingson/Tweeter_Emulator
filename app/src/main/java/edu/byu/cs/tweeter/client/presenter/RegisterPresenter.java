package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.client.presenter.views.RegisterView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {

    public void validateRegistration(Editable firstName, Editable lastName, Editable alias, Editable password, Drawable imageToUpload) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    private RegisterView view;
    private UserService userService;

    public RegisterPresenter (RegisterView view) {
        this.view = view;
        userService = new UserService();
    }

    //Register Task
    public void doRegister(EditText firstName, EditText lastName, EditText alias, EditText password, Bitmap image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);
        userService.GetRegister(firstName, lastName, alias, password, imageBytesBase64, new GetRegisterObserver());
    }

    public class GetRegisterObserver implements AuthenticationObserver {

        @Override
        public void handleSuccess(User registeredUser, AuthToken authToken) {
            view.createIntent(registeredUser, authToken);
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage("Failed to register because of exception: " + exception.getMessage());
        }
    }

}
