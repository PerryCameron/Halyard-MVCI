package org.ecsail.mvci.membership.mvci.person;

import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;

public class PersonController extends Controller<PersonMessage> {

    private final PersonInteractor personInteractor;
    private final PersonView personView;

    public PersonController() {
        PersonModel personModel = new PersonModel();
        personInteractor = new PersonInteractor(personModel);
        personView = new PersonView(personModel, this::action);
    }

    @Override
    public Region getView() {
        return null;
    }

    @Override
    public void action(PersonMessage actionEnum) {

    }
}
