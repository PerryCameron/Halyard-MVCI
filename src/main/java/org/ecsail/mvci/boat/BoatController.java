package org.ecsail.mvci.boat;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.fx.BoatListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci.main.MainController;

public class BoatController extends Controller<BoatMessage> {
    MainController mainController;
    BoatInteractor boatInteractor;
    BoatView boatView;

    public BoatController(MainController mc, BoatListDTO bl) {
        mainController = mc;
        BoatModel boatModel = new BoatModel(mainController.getMainModel());
        boatModel.setBoatListDTO(bl);
        boatInteractor = new BoatInteractor(boatModel);
        boatView = new BoatView(boatModel, this::action);
//        getBoatData();
    }

    @Override
    public void action(BoatMessage action) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                switch (action) {
//                    case UPDATE_BOAT -> boatInteractor.updateBoat();
//                    case UPDATE_NOTE -> boatInteractor.updateNote();
//                    case INSERT_IMAGE -> insertImage();
//                    case INSERT_NOTE -> boatInteractor.insertNote();
//                    case SET_DEFAULT -> boatInteractor.setImageAsDefault();
//                    case DELETE_IMAGE -> boatInteractor.deleteImage();
//                    case DELETE_NOTE -> boatInteractor.deleteNote();
//                    case DELETE_OWNER -> boatInteractor.deleteOwner();
//                    case GET_MEMBERSHIP -> boatInteractor.getBoatOwner();
//                    case INSERT_OWNER -> boatInteractor.insertOwner();
//                    case DOWNLOAD_IMAGE -> downloadImage();
                    case INFORM -> System.out.println("inform");
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
        });
        new Thread(task).start();
    }

//    private void downloadImage() {
//        mainController.setSpinnerOffset(-200,-100);
//        mainController.showLoadingSpinner(true);
//        boatInteractor.downloadImage();
//    }
//
//    private void insertImage() {
//        mainController.setSpinnerOffset(-175,-25);
//        mainController.showLoadingSpinner(true);
//        boatInteractor.insertImage();
//    }
//
//    private void getBoatData() {
//            mainController.setSpinnerOffset(50,50);
//            mainController.showLoadingSpinner(true);
//            Task<Void> task = new Task<>() {
//                @Override
//                protected Void call() {
//                    boatInteractor.getBoatOwners();
//                    boatInteractor.getBoatSettings();
//                    boatInteractor.getBoatNotes();
//                    boatInteractor.getBoatPhotos();
//                    return null;
//                }
//            };
//            task.setOnSucceeded(e -> {
//                mainController.showLoadingSpinner(false);
//                boatInteractor.setDataLoaded(true);
//            });
//            new Thread(task).start();
//    }

    @Override
    public Region getView() { return boatView.build(); }
}
