package edu.byu.cs.tweeter.client.view.main.register;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.RegisterTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;

public class RegisterFragment extends Fragment implements RegisterPresenter.View, RegisterTask.Observer {

    private static final String LOG_TAG = "RegisterFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    private Toast registerToast;

    private User user;
    private AuthToken authToken;
    private RegisterPresenter presenter;

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

        presenter = new RegisterPresenter(this);

        Button takePictureButton;
        takePictureButton = view.findViewById(R.id.TakePictureButton);

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(super.getActivity()),
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                }
            });

        EditText registerFirstName = (EditText)view.findViewById(R.id.RegisterFirstName);
        EditText registerLastName = (EditText)view.findViewById(R.id.RegisterLastName);
        EditText registerUsername = (EditText)view.findViewById(R.id.RegisterUsername);
        EditText registerPassword = (EditText)view.findViewById(R.id.RegisterPassword);

        Button registerButton = view.findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Makes a register request.
             *
             * @param view the view object that was clicked.
             */
            @Override
            public void onClick(View view) {
                registerToast = Toast.makeText(getContext(), "Registering New User", Toast.LENGTH_LONG);
                registerToast.show();

                String firstName = registerFirstName.getText().toString();
                String lastName = registerLastName.getText().toString();
                String username = registerUsername.getText().toString();
                String password = registerPassword.getText().toString();

                RegisterRequest registerRequest = new RegisterRequest(firstName,lastName,username, password);
                RegisterTask registerTask = new RegisterTask(presenter, RegisterFragment.this);
                registerTask.execute(registerRequest);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
        }
    }

    @Override
    public void registerSuccessful(RegisterResponse registerResponse) {
        Intent intent = new Intent(getContext(), MainActivity.class);

        intent.putExtra(MainActivity.CURRENT_USER_KEY, registerResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, registerResponse.getAuthToken());

        registerToast.cancel();
        startActivity(intent);
    }

    @Override
    public void registerUnsuccessful(RegisterResponse registerResponse) {
        Toast.makeText(getContext(), "Failed to register. " + registerResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception ex) {
        Log.e(LOG_TAG, ex.getMessage(), ex);
        Toast.makeText(getContext(), "Failed to login because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();

    }
}
