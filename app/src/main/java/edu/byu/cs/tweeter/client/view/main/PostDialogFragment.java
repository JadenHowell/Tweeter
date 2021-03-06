package edu.byu.cs.tweeter.client.view.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;
import edu.byu.cs.tweeter.client.presenter.PostPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.PostTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDialogFragment extends DialogFragment implements PostPresenter.View, PostTask.Observer {
    private static final String LOG_TAG = "PostDialogFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private User user;
    private AuthToken authToken;
    private ImageView userImage;
    private TextView userAlias;
    private TextView userName;
    private TextView statusText;
    private String message;

    private PostPresenter presenter;

    public PostDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the logged in user.
     * @param authToken the auth token for this user's session.
     * @return the fragment.
     */
    public static PostDialogFragment newInstance(User user, AuthToken authToken) {
        PostDialogFragment fragment = new PostDialogFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        presenter = new PostPresenter(this);

        View view = inflater.inflate(R.layout.fragment_post_dialog, null);

        userImage = view.findViewById(R.id.userImage);
        userAlias = view.findViewById(R.id.userAlias);
        userName = view.findViewById(R.id.userName);
        statusText = view.findViewById(R.id.status_text);

        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        userImage.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));
        userAlias.setText(user.getAlias());
        userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Post Status", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        message = statusText.getText().toString();

                        Status status = new Status(user, Calendar.getInstance().getTime(), message);
                        post(status);
                    }
                });
        return builder.create();
    }

    private void post(Status status) {
        PostTask postTask = new PostTask(presenter, this);
        PostRequest request = new PostRequest(status);
        postTask.execute(request);
    }

    @Override
    public void postSuccessful(PostResponse response) {
        Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void postUnsuccessful(PostResponse response) {
        Toast.makeText(getActivity(), "Failed to post because of exception: " + response.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getActivity(), "Failed to post because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}