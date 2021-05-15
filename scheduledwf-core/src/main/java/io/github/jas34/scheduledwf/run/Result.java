package io.github.jas34.scheduledwf.run;

/**
 * @author Jasbir Singh
 */
public class Result {

    private String id;
    private Status status;
    private Throwable exception;

    public Result() {}

    public Result(String id) {
        this(id, null, null);
    }

    public Result(String id, Status status, Throwable exception) {
        this.id = id;
        this.status = status;
        this.exception = exception;
    }

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

    public void setException(Throwable exception) {
        this.exception = exception;
    }

}
