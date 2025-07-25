package org.ecsail.wrappers;

import org.ecsail.abstractions.ResponseWrapper;
import org.ecsail.pojo.Phone;

public class PhoneResponse extends ResponseWrapper<Phone> {

    public PhoneResponse(Phone phone) {
        super(phone);
    }

    public PhoneResponse() {
        super(null);
    }

    @Override
    protected Phone createDefaultInstance() {
        return new Phone();
    }

    public Phone getPhone() {
        return getData();
    }

    public void setPhone(Phone phone) {
        setData(phone);
    }
}

