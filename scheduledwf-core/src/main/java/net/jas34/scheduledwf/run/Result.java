package net.jas34.scheduledwf.run;

/**
 * @author Jasbir Singh
 */
public class Result {

    private String id;
    private Status status;
    private Throwable exception;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public enum Status {
        SUCCESS,

        FAILURE;
    }
}
