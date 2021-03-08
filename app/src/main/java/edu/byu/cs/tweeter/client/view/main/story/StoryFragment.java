package edu.byu.cs.tweeter.client.view.main.story;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;
import edu.byu.cs.tweeter.client.presenter.StoryPresenter;
import edu.byu.cs.tweeter.client.presenter.UserPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetStoryTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetUserTask;
import edu.byu.cs.tweeter.client.view.main.OtherUserActivity;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryFragment extends Fragment implements StoryPresenter.View, UserPresenter.View, GetUserTask.Observer {

    private static final String LOG_TAG = "StoryFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private User user;
    private AuthToken authToken;
    private StoryPresenter presenter;

    private StoryFragment.StoryRecyclerViewAdapter storyRecyclerViewAdapter;

    public StoryFragment() {
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
    public static StoryFragment newInstance(User user, AuthToken authToken) {
        StoryFragment fragment = new StoryFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new StoryPresenter(this);

        RecyclerView storyRecyclerView = view.findViewById(R.id.storyRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        storyRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryFragment.StoryRecyclerViewAdapter();
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);

        storyRecyclerView.addOnScrollListener(new StoryFragment.StoryRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }


    /**
     * The ViewHolder for the RecyclerView that displays the Story data.
     */
    private class StoryHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView date;
        private final TextView message;
        private User otherUser;

        /**
         * Creates an instance and sets an OnClickListener for the status's row.
         *
         * @param itemView the view on which the user will be displayed.
         */
        StoryHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if(viewType == ITEM_VIEW) {
                userImage = itemView.findViewById(R.id.userImage);
                userAlias = itemView.findViewById(R.id.userAlias);
                userName = itemView.findViewById(R.id.userName);
                date = itemView.findViewById(R.id.date);
                message = itemView.findViewById(R.id.message);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startOtherUserActivity(otherUser);
                    }
                });
            } else {
                userImage = null;
                userAlias = null;
                userName = null;
                date = null;
                message = null;
            }
        }

        /**
         * Binds the status's data to the view.
         *
         * @param status the status.
         */
        void bindStatus(Status status) {
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(status.getUser().getImageBytes()));
            userAlias.setText(status.getUser().getAlias());
            userName.setText(String.format("%s %s", status.getUser().getFirstName(), status.getUser().getLastName()));
            date.setText(status.getDate().toString());
            message.setText(parseAlias(status.getMessage()));
            message.setMovementMethod(LinkMovementMethod.getInstance());
            otherUser = status.getUser();
        }
    }

    private SpannableString parseAlias(String content) {
        SpannableString clickableContent = new SpannableString(content);

        int start = 0;
        for (String word : content.split(" ")) {
            int len = word.length();
            String userAlias;
            if (word.charAt(len-1) == '.') {
                len--;
                userAlias = word.substring(0, len);
            } else {
                userAlias = word;
            }
            if (word.charAt(0) == '@') {
                clickableContent.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        getUser(userAlias);
                    }
                }, start, start + len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            start += len + 1;
        }

        return clickableContent;
    }

    private void getUser(String userAlias) {
        UserPresenter userPresenter = new UserPresenter(this);

        GetUserTask getUserTask = new GetUserTask(userPresenter, this);
        UserRequest request = new UserRequest(userAlias);
        getUserTask.execute(request);
    }

    private void startOtherUserActivity(User otherUser) {
        Intent intent = new Intent(getContext(), OtherUserActivity.class);

        intent.putExtra(OtherUserActivity.LOGGED_IN_USER_KEY, user);
        intent.putExtra(OtherUserActivity.AUTH_TOKEN_KEY, authToken);
        intent.putExtra(OtherUserActivity.OTHER_USER_KEY, otherUser);

        startActivity(intent);
    }

    /**
     * The adapter for the RecyclerView that displays the Story data.
     */
    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryFragment.StoryHolder> implements GetStoryTask.Observer {

        private final List<Status> story = new ArrayList<>();

        private edu.byu.cs.tweeter.shared.domain.Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of story data.
         */
        StoryRecyclerViewAdapter() {
            loadMoreItems();
        }

        /**
         * Adds new statuses to the list from which the RecyclerView retrieves the statuses it displays
         * and notifies the RecyclerView that items have been added.
         *
         * @param newStatuses the statuses to add.
         */
        void addItems(List<Status> newStatuses) {
            int startInsertPosition = story.size();
            story.addAll(newStatuses);
            this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
        }

        /**
         * Adds a single user to the list from which the RecyclerView retrieves the statuses it
         * displays and notifies the RecyclerView that an item has been added.
         *
         * @param status the status to add.
         */
        void addItem(Status status) {
            story.add(status);
            this.notifyItemInserted(story.size() - 1);
        }

        /**
         * Removes a status from the list from which the RecyclerView retrieves the statuses it displays
         * and notifies the RecyclerView that an item has been removed.
         *
         * @param status the user to remove.
         */
        void removeItem(Status status) {
            int position = story.indexOf(status);
            story.remove(position);
            this.notifyItemRemoved(position);
        }

        /**
         *  Creates a view holder for a status to be displayed in the RecyclerView or for a message
         *  indicating that new rows are being loaded if we are waiting for rows to load.
         *
         * @param parent the parent view.
         * @param viewType the type of the view (ignored in the current implementation).
         * @return the view holder.
         */
        @NonNull
        @Override
        public StoryFragment.StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StoryFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new StoryFragment.StoryHolder(view, viewType);
        }

        /**
         * Binds the status at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         * @param storyHolder the ViewHolder to which the status should be bound.
         * @param position the position (in the list of statuses) that contains the status to be
         *                 bound.
         */
        @Override
        public void onBindViewHolder(@NonNull StoryFragment.StoryHolder storyHolder, int position) {
            if(!isLoading) {
                storyHolder.bindStatus(story.get(position));
            }
        }

        /**
         * Returns the current number of statuses available for display.
         * @return the number of statuses available for display.
         */
        @Override
        public int getItemCount() {
            return story.size();
        }

        /**
         * Returns the type of the view that should be displayed for the item currently at the
         * specified position.
         *
         * @param position the position of the items whose view type is to be returned.
         * @return the view type.
         */
        @Override
        public int getItemViewType(int position) {
            return (position == story.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more story
         * data.
         */
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetStoryTask getStoryTask = new GetStoryTask(presenter, this);
            StoryRequest request = new StoryRequest(user.getAlias(), PAGE_SIZE, (lastStatus == null ? null : lastStatus.getUser().getAlias()));
            getStoryTask.execute(request);
        }

        /**
         * A callback indicating more story data has been received. Loads the new statuses
         * and removes the loading footer.
         *
         * @param storyResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void storyRetrieved(StoryResponse storyResponse) {
            List<Status> story = storyResponse.getStatusList();

            lastStatus = (story.size() > 0) ? story.get(story.size() -1) : null;
            hasMorePages = storyResponse.getHasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(story);
        }

        /**
         * A callback indicating that an exception was thrown by the presenter.
         *
         * @param exception the exception.
         */
        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            removeLoadingFooter();
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * Adds a dummy status to the list of users so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        private void addLoadingFooter() {
            addItem(new Status(new User("Test", "User",
                    "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), Calendar.getInstance().getTime(), ""));
        }

        /**
         * Removes the dummy status from the list of users so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(story.get(story.size() - 1));
        }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class StoryRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        StoryRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        /**
         * Determines whether the user has scrolled to the bottom of the currently available data
         * in the RecyclerView and asks the adapter to load more data if the last load request
         * indicated that there was more data to load.
         *
         * @param recyclerView the RecyclerView.
         * @param dx the amount of horizontal scroll.
         * @param dy the amount of vertical scroll.
         */
        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }

    @Override
    public void getUserSuccessful(UserResponse response) {
        startOtherUserActivity(response.getUser());
    }

    @Override
    public void getUserUnsuccessful(UserResponse response) {
        Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getActivity(), "Failed to get user because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}