package edu.byu.cs.tweeter.view.main.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoginTask;
import edu.byu.cs.tweeter.view.main.login.LoginFragment;

public class RegisterFragment extends Fragment implements LoginPresenter.View, LoginTask.Observer {

    private static final String LOG_TAG = "LoginFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    private Toast registerToast;

    private User user;
    private AuthToken authToken;
    private LoginPresenter presenter;

    public static RegisterFragment newInstance(User user, AuthToken authToken) {
        RegisterFragment fragment = new RegisterFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new LoginPresenter(this);

        Button loginButton = view.findViewById(R.id.RegisterButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Makes a login request. The user is hard-coded, so it doesn't matter what data we put
             * in the LoginRequest object.
             *
             * @param view the view object that was clicked.
             */
            @Override
            public void onClick(View view) {
                registerToast = Toast.makeText(getContext(), "Registering New User", Toast.LENGTH_LONG);
                registerToast.show();

                // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
                LoginRequest loginRequest = new LoginRequest("dummyUserName", "dummyPassword");
                LoginTask loginTask = new LoginTask(presenter, RegisterFragment.this);
                loginTask.execute(loginRequest);
            }
        });

        return view;
    }

    @Override
    public void loginSuccessful(LoginResponse loginResponse) {

    }

    @Override
    public void loginUnsuccessful(LoginResponse loginResponse) {

    }

    @Override
    public void handleException(Exception ex) {

    }
}
