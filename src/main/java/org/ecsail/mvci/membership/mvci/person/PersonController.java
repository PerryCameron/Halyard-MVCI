package org.ecsail.mvci.membership.mvci.person;

import javafx.concurrent.Task;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import org.ecsail.fx.PersonFx;
import org.ecsail.interfaces.TabController;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.mvci.membership.MembershipView;
import org.ecsail.widgetfx.DialogueFx;


import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class PersonController extends TabController<PersonMessage> {

    private final PersonInteractor personInteractor;
    private final PersonView personView;
    private final MembershipView membershipView;
    private final ExecutorService executor;

    public PersonController(MembershipView membershipView, PersonFx personDTO) {
        PersonModel personModel = new PersonModel(membershipView, personDTO);
        personInteractor = new PersonInteractor(personModel);
        personView = new PersonView(personModel, this::action);
        this.membershipView = membershipView;
        this.executor = membershipView.getMembershipModel().getExecutorService();
    }

    @Override
    public Tab getView() {
        return personView.build();
    }

    @Override
    public void action(PersonMessage actionEnum) {
                switch (actionEnum) {
                    case SAVE_IMAGE -> saveImageCustomTask();
                    case GET_IMAGE -> getImageCustomTask();
                    case INSERT_AWARD -> runInTask(personInteractor::insertAward);
                    case UPDATE_AWARD -> runInTask(personInteractor::updateAward);
                    case DELETE_AWARD -> runInTask(personInteractor::deleteAward);
                    case INSERT_PHONE -> runInTask(personInteractor::insertPhone);
                    case UPDATE_PHONE -> runInTask(personInteractor::updatePhone);
                    case DELETE_PHONE -> runInTask(personInteractor::deletePhone);
                    case INSERT_EMAIL -> runInTask(personInteractor::insertEmail);
                    case UPDATE_EMAIL -> runInTask(personInteractor::updateEmail);
                    case DELETE_EMAIL -> runInTask(personInteractor::deleteEmail);
                    case INSERT_POSITION -> runInTask(personInteractor::insertPosition);
                    case UPDATE_POSITION -> runInTask(personInteractor::updatePosition);
                    case DELETE_POSITION -> runInTask(personInteractor::deletePosition);
                    case UPDATE_PERSON -> runInTask(personInteractor::updatePerson);
                    case MOVE_MEMBER_TO_MEMBERSHIP -> runInTask(personInteractor::moveMemberToMembership);
                    case DETACH_MEMBER_FROM_MEMBERSHIP -> runInTask(personInteractor::detachMemberFromMembership);
                    case DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP -> runInTask(personInteractor::detachPrimaryMemberFromMembership);
                    case DELETE_MEMBER_FROM_DATABASE -> runInTask(personInteractor::deleteMemberFromDatabase);
                    case CHANGE_MEMBER_TYPE -> runInTask(personInteractor::changeMemberType);
                }
    }

    private void getImageCustomTask() {
        personInteractor.getImage(executor);
    }

    private void saveImageCustomTask() {
        // get clipboard image on JavaFX thread
        Optional<Image> image = personInteractor.getClipBoardImage();
        // Call a method running on another thread
        image.ifPresent(img -> {
            personInteractor.saveImage(img, executor);
        });
    }

    public void runInTask(Supplier<PersonMessage> method) {  // I want to put in a method as a parameter
        Task<PersonMessage> task = new Task<>() {
            @Override
            protected PersonMessage call() {
                return method.get();
            }
        };
        task.setOnSucceeded(e -> {
            if (task.getValue() == PersonMessage.SUCCESS) {
                membershipView.sendMessage().accept(MembershipMessage.SUCCESS);
            } else {
                logFailure();
            }
        });
        task.setOnFailed(e -> logFailure());
        executor.submit(task);
    }

    private void logFailure() {
        String[] message = personInteractor.getErrorMessage();
        if (message[1].equals("0")) {
            personInteractor.logError(message[0] + ": " + message[2]);
        } else {
            personInteractor.logError(message[0] + " with ID: " + message[1] + message[2]);
        }
        DialogueFx.errorAlert(message[0], message[2]);
        membershipView.sendMessage().accept(MembershipMessage.FAIL);
    }

}
