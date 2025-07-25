package org.ecsail.abstractions;

public abstract class ResponseWrapper<T> {
    protected boolean success;
    protected String message;
    protected T data;

    public ResponseWrapper(T data) {
        this.success = false;
        this.message = "";
        this.data = data != null ? data : createDefaultInstance();
    }

    // Abstract method to create a default instance of T
    protected abstract T createDefaultInstance();

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}