package org.ecsail.mvci_connect;

import javafx.collections.FXCollections;
import org.ecsail.dto.LoginDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.widgetfx.ObjectFx;

import java.util.ArrayList;
import java.util.List;

public class ConnectInteractor {
    private ConnectModel model;
    public ConnectInteractor(ConnectModel connectModel) {
        this.model = connectModel;
        connectModel.getLoginDTOS().addAll(FXCollections.observableArrayList(setLogins()));
        setDefaultLogin();
    }

    public List<LoginDTO> setLogins() {
        List<LoginDTO> loginDTOS = new ArrayList<>();
        if (FileIO.hostFileExists())
            FileIO.openLoginObjects(loginDTOS);
        else {
            loginDTOS.add(ObjectFx.createLoginDTO()); // we are starting application for the first time
            loginDTOS.add(ObjectFx.createLoginDTO2());
            loginDTOS.add(ObjectFx.createLoginDTO3());
        }

        return loginDTOS;
    }

    public void setDefaultLogin() {
        model.setSelectedLogin(model.getLoginDTOS().stream()
                .filter(loginDTO -> loginDTO.isDefault() == true).findFirst().orElse(null));
    }
}
