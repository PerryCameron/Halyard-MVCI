package org.ecsail.mvci.membership.mvci.person;

import javafx.scene.control.Tab;
import org.ecsail.fx.PersonFx;
import org.ecsail.interfaces.TabController;
import org.ecsail.mvci.membership.MembershipView;

public class PersonController extends TabController<PersonMessage> {

    private final PersonInteractor personInteractor;
    private final PersonView personView;

    public PersonController(MembershipView membershipView, PersonFx personDTO) {
        PersonModel personModel = new PersonModel(membershipView, personDTO);
        personInteractor = new PersonInteractor(personModel);
        personView = new PersonView(personModel, this::action);
    }

    @Override
    public Tab getView() {
        return personView.build();
    }

    @Override
    public void action(PersonMessage actionEnum) {
        switch (actionEnum) {
            case SAVE_IMAGE -> personInteractor.saveImage();
        }
    }

    public int getTesting() {
      return 0;
    }
}
