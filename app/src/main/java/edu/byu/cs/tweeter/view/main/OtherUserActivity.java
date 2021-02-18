package edu.byu.cs.tweeter.view.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.presenter.CountPresenter;
import edu.byu.cs.tweeter.presenter.FollowButtonPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.ChangeFollowStateTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowerCountTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowingCountTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetIsFollowingTask;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class OtherUserActivity extends AppCompatActivity
        implements CountPresenter.View, GetFollowingCountTask.FollowingCountObserver, GetFollowerCountTask.FollowerCountObserver,
                    FollowButtonPresenter.View, GetIsFollowingTask.IsFollowingObserver, ChangeFollowStateTask.ChangeStateObserver {

    public static final String LOGGED_IN_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    public static final String OTHER_USER_KEY = "OtherUser";

    FollowButtonPresenter buttonPresenter;

    private boolean followButtonState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        User loggedInUser = (User) getIntent().getSerializableExtra(LOGGED_IN_USER_KEY);
        User otherUser = (User) getIntent().getSerializableExtra(OTHER_USER_KEY);
        if(loggedInUser == null) {
            throw new RuntimeException("Logged in user not passed to activity");
        }else if(otherUser == null){
            throw new RuntimeException("Other user not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        OtherUserSectionsPagerAdapter sectionsPagerAdapter = new OtherUserSectionsPagerAdapter(
                this, getSupportFragmentManager(), loggedInUser, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        buttonPresenter = new FollowButtonPresenter(this);
        Button follow_unfollowButton = findViewById(R.id.follow_unfollow_button);
        follow_unfollowButton.setOnClickListener(view ->
                {
                    ChangeFollowStateTask changeFollowStateTask = new ChangeFollowStateTask(buttonPresenter, this);
                    ChangeFollowStateRequest changeFollowStateRequest = new ChangeFollowStateRequest(loggedInUser.getAlias(), otherUser.getAlias());
                    changeFollowStateTask.execute(changeFollowStateRequest);
                }
        );

        TextView userName = findViewById(R.id.userName);
        userName.setText(otherUser.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(otherUser.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(otherUser.getImageBytes()));

        //Put two dummy numbers before starting the async task
        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, 0));
        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, 0));

        //create CountPresenter and the two count tasks, execute async count tasks.
        CountPresenter countPresenter = new CountPresenter(this);
        GetFollowerCountTask followerCountTask = new GetFollowerCountTask(countPresenter, this);
        FollowerCountRequest followerCountRequest = new FollowerCountRequest(otherUser.getAlias());
        followerCountTask.execute(followerCountRequest);
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(countPresenter, this);
        FollowingCountRequest followingCountRequest = new FollowingCountRequest(otherUser.getAlias());
        followingCountTask.execute(followingCountRequest);

        GetIsFollowingTask isFollowingTask = new GetIsFollowingTask(buttonPresenter, this);
        IsFollowingRequest isFollowingRequest = new IsFollowingRequest(loggedInUser.getAlias(), otherUser.getAlias());
        isFollowingTask.execute(isFollowingRequest);
    }

    @Override
    public void followerCountRetrieved(FollowerCountResponse followerCountResponse) {
        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, followerCountResponse.getCount()));
    }

    @Override
    public void followingCountRetrieved(FollowingCountResponse followingCountResponse) {
        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, followingCountResponse.getCount()));
    }

    @Override
    public void isFollowingRetrieved(IsFollowingResponse isFollowingResponse) {
        updateButtonState(isFollowingResponse.getIsFollowing());
    }

    @Override
    public void changeButtonStateRetrieved(ChangeFollowStateResponse changeFollowStateResponse) {
        updateButtonState(changeFollowStateResponse.getNewFollowingState());
    }

    private void updateButtonState(boolean isFollowing){
        Button follow_unfollowButton = findViewById(R.id.follow_unfollow_button);
        if(isFollowing){
            follow_unfollowButton.setText("Following");
            follow_unfollowButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLight));
        }else{
            follow_unfollowButton.setText("Follow");
            follow_unfollowButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
    }

    @Override
    public void handleException(Exception exception) {
        Log.e("MainActivity", exception.getMessage(), exception);
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

}
