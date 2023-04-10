package org.ecsail.mvci_main;

import javafx.scene.layout.Region;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;

public class MainInteractor implements ConfigFilePaths {

    private final MainModel mainModel;
    public MainInteractor(MainModel mainModel) {
        this.mainModel = mainModel;
        FileIO.checkPath(SCRIPTS_FOLDER);
        FileIO.checkPath(LOG_FOLDER);
    }

    public Region returnController(String tabName, MainController mainController) {
        return null;
    }
}
