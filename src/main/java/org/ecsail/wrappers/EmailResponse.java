package org.ecsail.wrappers;

import org.ecsail.abstractions.ResponseWrapper;

import org.ecsail.pojo.Email;

public class EmailResponse extends ResponseWrapper<Email> {

    public EmailResponse(Email email) {
        super(email);
    }

    public EmailResponse() {
        super(null);
    }

    @Override
    protected Email createDefaultInstance() {
        return new Email();
    }

    public Email getEmail() {
        return getData();
    }

    public void setEmail(Email email) {
        setData(email);
    }
}

