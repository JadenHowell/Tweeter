package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {

    private final RegisterPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void registerSuccessful(RegisterResponse registerResponse);
        void registerUnsuccessful(RegisterResponse registerResponse);
        void handleException(Exception ex);
    }

    public RegisterTask(RegisterPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected RegisterResponse doInBackground(RegisterRequest... registerRequests) {
        RegisterResponse registerResponse = null;

        try {
            registerResponse = presenter.register(registerRequests[0]);

            if(registerResponse.isSuccess() && registerResponse.getMessage() == null) {
                loadImage(registerResponse.getUser());
            }
        } catch (IOException | TweeterRemoteException ex) {
            exception = ex;
        }
        return registerResponse;
    }

    private void loadImage(User user) {
        try {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.toString(), e);
        }
    }

    @Override
    protected void onPostExecute(RegisterResponse registerResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else if(registerResponse.isSuccess() && registerResponse.getMessage() == null) {
            observer.registerSuccessful(registerResponse);
        } else {
            observer.registerUnsuccessful(registerResponse);
        }
    }
}
