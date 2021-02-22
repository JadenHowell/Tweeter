package edu.byu.cs.tweeter.model.service.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.model.service.request.FeedRequest}.
 */
public class FeedResponse extends PagedResponse {

    private List<Status> statusList;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FeedResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param feed the list of statuses to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public FeedResponse(List<Status> feed, boolean hasMorePages) {
        super(true, hasMorePages);
        this.statusList = feed;
    }

    /**
     * Returns the statuses for the corresponding request.
     *
     * @return the status list.
     */
    public List<Status> getStatusList() {
        return statusList;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FeedResponse that = (FeedResponse) param;

        return (Objects.equals(statusList, that.statusList) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusList);
    }
}
