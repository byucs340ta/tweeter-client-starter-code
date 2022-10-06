package edu.byu.cs.tweeter.client.model.service;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticateNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService {

    // MARK - Class Methods

    public void loginUser(String alias, String password, AuthenticateNotificationObserver getLoginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias, password, new AuthenticateNotificationHandler(getLoginObserver));
        BackgroundTaskUtils.runTask(loginTask);
    }

    public void registerUser(String firstName, String lastName, String alias,
                             String password, BitmapDrawable imageToUpload, AuthenticateNotificationObserver observer) {

        // Convert image to byte array.
        Bitmap image = imageToUpload.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthenticateNotificationHandler(observer));
        BackgroundTaskUtils.runTask(registerTask);
    }

    public void logoutUser(AuthToken authToken, SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(authToken, new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }

    public void getUserProfile (AuthToken authToken, String userAlias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(observer));
        BackgroundTaskUtils.runTask(getUserTask);
    }
}