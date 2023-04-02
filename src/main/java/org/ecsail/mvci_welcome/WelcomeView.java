package org.ecsail.mvci_welcome;

import javafx.scene.control.Tab;
import javafx.util.Builder;

public class WelcomeView implements Builder<Tab> {

    WelcomeModel welcomeModel;
    public WelcomeView(WelcomeModel welcomeModel) {
        this.welcomeModel = welcomeModel;
    }

    @Override
    public Tab build() {
        return new Tab("Welcome");
    }
}
