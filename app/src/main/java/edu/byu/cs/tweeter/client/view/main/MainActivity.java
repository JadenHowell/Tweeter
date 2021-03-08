package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.presenter.CountPresenter;
import edu.byu.cs.tweeter.client.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.client.view.LoginActivity;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFollowerCountTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity
        implements CountPresenter.View, GetFollowerCountTask.FollowerCountObserver, GetFollowingCountTask.FollowingCountObserver,
        LogoutTask.Observer, LogoutPresenter.View{

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        // We should use a Java 8 lambda function for the listener (and all other listeners), but
        // they would be unfamiliar to many students who use this code.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = PostDialogFragment.newInstance(user, authToken);
                newFragment.show(getSupportFragmentManager(), "post");
            }
        });

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

        //Put two dummy numbers before starting the async task
        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, 0));
        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, 0));

        //create CountPresenter and the two count tasks, execute async count tasks.
        CountPresenter countPresenter = new CountPresenter(this);
        GetFollowerCountTask followerCountTask = new GetFollowerCountTask(countPresenter, this);
        FollowerCountRequest followerCountRequest = new FollowerCountRequest(user.getAlias());
        followerCountTask.execute(followerCountRequest);

        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(countPresenter, this);
        FollowingCountRequest followingCountRequest = new FollowingCountRequest(user.getAlias());
        followingCountTask.execute(followingCountRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        if(R.id.logoutMenu == item.getItemId()){
            //Toast.makeText(this, "Clicked logout", Toast.LENGTH_SHORT).show();
            LogoutPresenter logoutPresenter = new LogoutPresenter(this);
            LogoutTask logoutTask = new LogoutTask(logoutPresenter, this);
            LogoutRequest logoutRequest = new LogoutRequest(user.getAlias());
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