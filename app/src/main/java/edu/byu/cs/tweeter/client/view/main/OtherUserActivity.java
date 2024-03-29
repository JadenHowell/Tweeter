package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.presenter.CountPresenter;
import edu.byu.cs.tweeter.client.presenter.FollowButtonPresenter;
import edu.byu.cs.tweeter.client.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.client.view.LoginActivity;
import edu.byu.cs.tweeter.client.view.asyncTasks.ChangeFollowStateTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFollowerCountTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetIsFollowingTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

public class OtherUserActivity extends AppCompatActivity
        implements CountPresenter.View, GetFollowingCountTask.FollowingCountObserver, GetFollowerCountTask.FollowerCountObserver,
                    FollowButtonPresenter.View, GetIsFollowingTask.IsFollowingObserver, ChangeFollowStateTask.ChangeStateObserver,
                    LogoutPresenter.View, LogoutTask.Observer{

    public static final String LOGGED_IN_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    public static final String OTHER_USER_KEY = "OtherUser";

    FollowButtonPresenter buttonPresenter;
    User loggedInUser, otherUser;
    AuthToken authToken;

    private boolean followButtonState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        loggedInUser = (User) getIntent().getSerializableExtra(LOGGED_IN_USER_KEY);
        otherUser = (User) getIntent().getSerializableExtra(OTHER_USER_KEY);
        authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        if(loggedInUser == null) {
            throw new RuntimeException("Logged in user not passed to activity");
        }else if(otherUser == null){
            throw new RuntimeException("Other user not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        OtherUserSectionsPagerAdapter sectionsPagerAdapter = new OtherUserSectionsPagerAdapter(
                this, getSupportFragmentManager(), otherUser, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        buttonPresenter = new FollowButtonPresenter(this);
        Button follow_unfollowButton = findViewById(R.id.follow_unfollow_button);
        follow_unfollowButton.setOnClickListener(view ->
                {
                    ChangeFollowStateTask changeFollowStateTask = new ChangeFollowStateTask(buttonPresenter, this);
                    ChangeFollowStateRequest changeFollowStateRequest = new ChangeFollowStateRequest(loggedInUser.getAlias(), otherUser.getAlias(), authToken);
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
        FollowerCountRequest followerCountRequest = new FollowerCountRequest(otherUser.getAlias(), authToken);
        followerCountTask.execute(followerCountRequest);
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(countPresenter, this);
        FollowingCountRequest followingCountRequest = new FollowingCountRequest(otherUser.getAlias(), authToken);
        followingCountTask.execute(followingCountRequest);

        GetIsFollowingTask isFollowingTask = new GetIsFollowingTask(buttonPresenter, this);
        IsFollowingRequest isFollowingRequest = new IsFollowingRequest(loggedInUser.getAlias(), otherUser.getAlias(), authToken);
        isFollowingTask.execute(isFollowingRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        if(R.id.logoutMenu == item.getItemId()){
            //Toast.makeText(this, "Clicked logout", Toast.LENGTH_SHORT).show();
            LogoutPresenter logoutPresenter = new LogoutPresenter(this);
            LogoutTask logoutTask = new LogoutTask(logoutPresenter, this);
            LogoutRequest logoutRequest = new LogoutRequest(loggedInUser.getAlias(), authToken);
            logoutTask.execute(logoutRequest);
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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
    public void logoutSuccessful(LogoutResponse logoutResponse) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void logoutUnsuccessful(LogoutResponse logoutResponse) {
        Toast.makeText(this, "Logout unsuccessful, try agin", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e("MainActivity", exception.getMessage(), exception);
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

}
