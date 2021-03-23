package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class StatusDAO {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final User testUser = new User("Test", "User", MALE_IMAGE_URL);

    private final Status status1 = new Status(user1, Calendar.getInstance().getTime().getTime(), "test1 @AllenAnderson");
    private final Status status2 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test2 @TestUser");
    private final Status status3 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test3 google.com");
    private final Status status4 = new Status(testUser, Calendar.getInstance().getTime().getTime(),
            "A really long message that should take up a few lines and has a mention to someone @AmyAmes. and a http://url.com");
    private final Status status5 = new Status(user4, Calendar.getInstance().getTime().getTime(), "post! @AmyAmes @BobBobson @ChrisColston @fakeuser");
    private final Status status6 = new Status(user2, Calendar.getInstance().getTime().getTime(), "test4 @BonnieBeatty");
    private final Status status7 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test5 @TestUser");
    private final Status status8 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test6 @TestUser");
    private final Status status9 = new Status(user1, Calendar.getInstance().getTime().getTime(), "test7 @GiovannaGiles");
    private final Status status10 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test8 @TestUser");
    private final Status status11 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test9 google.com");
    private final Status status12 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test10");
    private final Status status13 = new Status(user4, Calendar.getInstance().getTime().getTime(), "test11");
    private final Status status14 = new Status(user2, Calendar.getInstance().getTime().getTime(), "test12 @GiovannaGiles");
    private final Status status15 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test13 @TestUser");
    private final Status status16 = new Status(user2, Calendar.getInstance().getTime().getTime(), "test14 @GiovannaGiles");
    private final Status status17 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test15 @TestUser");
    private final Status status18 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test16 @TestUser");
    private final Status status19 = new Status(user1, Calendar.getInstance().getTime().getTime(), "test17 @GiovannaGiles");
    private final Status status20 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test18 @TestUser");
    private final Status status21 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test19 google.com");
    private final Status status22 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test20");
    private final Status status23 = new Status(user4, Calendar.getInstance().getTime().getTime(), "test21");
    private final Status status24 = new Status(user2, Calendar.getInstance().getTime().getTime(), "test22 @GiovannaGiles");
    private final Status status25 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test23 @TestUser");

    /**
     * Returns the statuses that the user specified in the request has in their feed. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose statuses are to be returned and any
     *                other information required to satisfy the request.
     * @return the feed response.
     */
    public FeedResponse getFeed(FeedRequest request) {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Status> allStatuses = getDummyFeed(request.getUserAlias());
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int statusesIndex = getFeedStartingIndex(request.getLastStatus(), allStatuses);

            for (int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                responseStatuses.add(allStatuses.get(statusesIndex));
            }

            hasMorePages = statusesIndex < allStatuses.size();
        }

        return new FeedResponse(responseStatuses, hasMorePages);
    }

    /**
     * Determines the index for the first status in the specified 'allStatuses' list that should
     * be returned in the current request. This will be the index of the next statuses after the
     * specified 'lastStatus'.
     *
     * @param lastStatus  the alias of the last status that was returned in the previous
     *                    request or null if there was no previous request.
     * @param allStatuses the generated list of statuses from which we are returning paged results.
     * @return the index of the first status to be returned.
     */
    private int getFeedStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int statusesIndex = 0;

        if (lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if (lastStatus.getMessage().equals(allStatuses.get(i).getMessage())) { // FIXME use date instead
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }

    /**
     * Returns the list of dummy feed data. This is written as a separate method to allow
     * mocking of the feed.
     *
     * @return the statuses.
     */
    List<Status> getDummyFeed(String userAlias) {
        return Arrays.asList(status1, status2, status3, status4, status5, status6, status7, status8,
                status9, status10, status11, status12, status13, status14, status15, status16, status17,
                status18, status19, status20, status21, status22, status23, status24, status25);
    }

    /**
     * Returns the statuses that the user specified in the request has in their story. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose statuses are to be returned and any
     *                other information required to satisfy the request.
     * @return the story response.
     */
    public StoryResponse getStory(StoryRequest request) {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Status> allStatuses = getDummyStory(request.getUserAlias());
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int statusesIndex = getStoryStartingIndex(request.getLastStatus(), allStatuses);

            for (int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                responseStatuses.add(allStatuses.get(statusesIndex));
            }

            hasMorePages = statusesIndex < allStatuses.size();
        }

        return new StoryResponse(responseStatuses, hasMorePages);
    }

    /**
     * Determines the index for the first status in the specified 'allStatuses' list that should
     * be returned in the current request. This will be the index of the next statuses after the
     * specified 'lastStatus'.
     *
     * @param lastStatus  the alias of the last status that was returned in the previous
     *                    request or null if there was no previous request.
     * @param allStatuses the generated list of statuses from which we are returning paged results.
     * @return the index of the first status to be returned.
     */
    private int getStoryStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int statusesIndex = 0;

        if (lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if (lastStatus.getMessage().equals(allStatuses.get(i).getMessage())) { // FIXME use date instead
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }

    /**
     * Returns the list of dummy story data. This is written as a separate method to allow
     * mocking of the story.
     *
     * @return the statuses.
     */
    List<Status> getDummyStory(String userAlias) {
        List<Status> allStatus = Arrays.asList(status1, status2, status3, status4, status5, status6, status7, status8,
                status9, status10, status11, status12, status13, status14, status15, status16, status17,
                status18, status19, status20, status21, status22, status23, status24, status25);
        List<Status> returnList = new ArrayList<>();
        for (int i = 0; i < allStatus.size(); i++) {
            if (allStatus.get(i).getUser().getAlias().equals("@TestUser")) { //TODO: .equals(userAlias)
                returnList.add(allStatus.get(i));
            }
        }
        return returnList;
    }



    public PostResponse post(PostRequest request) {
        return new PostResponse(true, "Post Successful!");
    }
}
