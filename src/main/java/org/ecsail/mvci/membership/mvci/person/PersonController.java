package org.ecsail.mvci.membership.mvci.person;

import javafx.concurrent.Task;
import javafx.scene.control.Tab;
import org.ecsail.fx.PersonFx;
import org.ecsail.interfaces.TabController;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.mvci.membership.MembershipView;

public class PersonController extends TabController<PersonMessage> {

    private final PersonInteractor personInteractor;
    private final PersonView personView;
    private final MembershipView membershipView;

    public PersonController(MembershipView membershipView, PersonFx personDTO) {
        PersonModel personModel = new PersonModel(membershipView, personDTO);
        personInteractor = new PersonInteractor(personModel);
        personView = new PersonView(personModel, this::action);
        this.membershipView = membershipView;
    }

    @Override
    public Tab getView() {
        return personView.build();
    }

    @Override
    public void action(PersonMessage actionEnum) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                switch (actionEnum) {
                    case SAVE_IMAGE -> personInteractor.saveImage();
                    case INSERT_AWARD -> personInteractor.insertAward();
                    case UPDATE_AWARD -> personInteractor.updateAward();
                    case DELETE_AWARD -> personInteractor.deleteAward();
                    case INSERT_PHONE -> personInteractor.insertPhone();
                    case UPDATE_PHONE -> personInteractor.updatePhone();
                    case DELETE_PHONE -> personInteractor.deletePhone();
                    case UPDATE_EMAIL -> personInteractor.updateEmail();
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            membershipView.sendMessage().accept(MembershipMessage.SUCCESS);
        });
        task.setOnFailed(e -> {
            membershipView.sendMessage().accept(MembershipMessage.FAIL);
        });
        new Thread(task).start();
    }   // In the interest of learning, what is the scenario which the task would be considered a failure. Is it if it triggers an exception?
}
