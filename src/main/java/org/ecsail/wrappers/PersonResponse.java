package org.ecsail.wrappers;


import org.ecsail.pojo.Person;

public class PersonResponse {

    private boolean success;
    private Person position;
    private String message;

    public PersonResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.position = new Person();
    }

    public PersonResponse() {
    }

    public PersonResponse(Person person) {
        this.success = false;
        this.message = "Success";
        this.position = person;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Person getPosition() {
        return position;
    }

    public void setPosition(Person position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

