package org.ecsail.mvci_connect;

import javafx.collections.FXCollections;
import org.ecsail.dto.LoginDTO;
import org.ecsail.fileio.FileIO;

import java.util.ArrayList;
import java.util.List;

public class ConnectInteractor {
    private ConnectModel model;
    public ConnectInteractor(ConnectModel connectModel) {
        this.model = connectModel;
        connectModel.getLoginDTOS().addAll(FXCollections.observableArrayList(setLogins()));
        setDefaultLogin();
        System.out.println(model.getSelectedLogin());
    }

    public List<LoginDTO> setLogins() {
        List<LoginDTO> loginDTOS = new ArrayList<>();
        if (FileIO.hostFileExists())
            FileIO.openLoginObjects(loginDTOS);
        else
            // we are starting application for the first time
            loginDTOS.add(new LoginDTO(3306,3306, 22, "testhost", "testuser", "testpass", "testsshuser",
                    "testsshpass", System.getProperty("user.home") + "/.ssh/known_hosts",
                    System.getProperty("user.home") + "/.ssh/id_rsa", true, true));
        return loginDTOS;
    }

    public void setDefaultLogin() {
        model.setSelectedLogin(model.getLoginDTOS().stream()
                .filter(loginDTO -> loginDTO.isDefault() == true).findFirst().orElse(null));
    }
}
