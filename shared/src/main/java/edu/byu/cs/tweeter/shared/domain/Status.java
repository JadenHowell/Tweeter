package edu.byu.cs.tweeter.shared.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a status in the system.
 */
public class Status implements Comparable<Status>, Serializable {

    private final User user;
    private final Date date;
    private final String message;

    public Status(User user, Date date, String message) {
        this.user = user;
        this.date = date;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return status.getUser().equals(user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.toString() + date + message);
    }

    @Override
    public String toString() {
        return "Status{" +
                "date='" + date + '\'' +
                ", user='" + user.toString() + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int compareTo(Status status) {
        return this.getMessage().compareTo(status.getMessage());
    }
}
