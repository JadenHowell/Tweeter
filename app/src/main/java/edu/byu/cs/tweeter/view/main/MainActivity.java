package edu.byu.cs.tweeter.view.main;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.presenter.CountPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowerCountTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowingCountTask;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity
        implements CountPresenter.View, GetFollowerCountTask.FollowerCountObserver, GetFollowingCountTask.FollowingCountObserver {

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
    public void handleException(Exception exception) {
        Log.e("MainActivity", exception.getMessage(), exception);
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}