package edu.byu.cs.tweeter.view.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class OtherUserActivity extends AppCompatActivity {

    public static final String LOGGED_IN_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    public static final String OTHER_USER_KEY = "OtherUser";

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

        Button follow_unfollowButton = findViewById(R.id.follow_unfollow_button);
        follow_unfollowButton.setOnClickListener(view ->
                        Toast.makeText(this, "Clicked follow", Toast.LENGTH_SHORT).show()
        );

        TextView userName = findViewById(R.id.userName);
        userName.setText(otherUser.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(otherUser.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(otherUser.getImageBytes()));

        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, 42));

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, 27));
    }

}
