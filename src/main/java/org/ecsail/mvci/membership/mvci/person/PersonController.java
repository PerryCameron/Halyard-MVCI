package org.ecsail.mvci.membership.mvci.person;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Tab;
import org.ecsail.fx.PersonFx;
import org.ecsail.interfaces.TabController;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.mvci.membership.MembershipView;
import org.ecsail.widgetfx.DialogueFx;

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
        Task<PersonMessage> task = new Task<>() {
            @Override
            protected PersonMessage call() {
                switch (actionEnum) {
                    case SAVE_IMAGE -> { return personInteractor.saveImage(); }
                    case INSERT_AWARD -> { return personInteractor.insertAward(); }
                    case UPDATE_AWARD -> { return personInteractor.updateAward(); }
                    case DELETE_AWARD -> { return personInteractor.deleteAward(); }
                    case INSERT_PHONE -> { return personInteractor.insertPhone(); }
                    case UPDATE_PHONE -> { return personInteractor.updatePhone(); }
                    case DELETE_PHONE -> { return personInteractor.deletePhone(); }
                    case INSERT_EMAIL -> { return personInteractor.insertEmail(); }
                    case UPDATE_EMAIL -> { return personInteractor.updateEmail(); }
                    case DELETE_EMAIL -> { return personInteractor.deleteEmail(); }
                    case INSERT_POSITION -> { return personInteractor.insertPosition(); }
                    case UPDATE_POSITION -> { return personInteractor.updatePosition(); }
                    case DELETE_POSITION -> { return personInteractor.deletePosition(); }
                    default -> { return PersonMessage.NULL; }
                }
            }
        };
        task.setOnSucceeded(e -> {
            System.out.println(task.getValue());
            if (task.getValue() == PersonMessage.SUCCESS) {
                membershipView.sendMessage().accept(MembershipMessage.SUCCESS);
            } else {
                membershipView.sendMessage().accept(MembershipMessage.FAIL);
            }
        });
        task.setOnFailed(e -> {
            String[] message = personInteractor.getErrorMessage();
            if (message[1].equals("0")) {
                personInteractor.logError(message[0] + ": " + message[2]);
            } else {
                personInteractor.logError(message[0] + " with ID: " + message[1] + message[2]);
            }
            DialogueFx.errorAlert(message[0], message[2]);
            membershipView.sendMessage().accept(MembershipMessage.FAIL);
        });
        new Thread(task).start();
    }
}
