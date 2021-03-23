package edu.byu.cs.tweeter.shared.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a status in the system.
 */
public class Status implements Comparable<Status>, Serializable {

    private User user;
    private long date;
    private String message;

    private Status() {
    }

    public Status(User user, long date, String message) {
        this.user = user;
        this.date = date;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public long getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public void setUser(User user) { this.user = user; }

    public void setDate(long date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
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
