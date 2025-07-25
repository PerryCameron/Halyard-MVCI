package org.ecsail.wrappers;

import org.ecsail.abstractions.ResponseWrapper;
import org.ecsail.pojo.Person;

public class PersonResponse extends ResponseWrapper<Person> {

    public PersonResponse(Person person) {
        super(person);
    }

    public PersonResponse() {
        super(null);
    }

    @Override
    protected Person createDefaultInstance() {
        return new Person();
    }

    public Person getPerson() {
        return getData();
    }

    public void setPerson(Person person) {
        setData(person);
    }
}

