package org.ecsail.mvci_template;

import javafx.scene.layout.Region;

public abstract class Controller {
    private View view;

    private Interactor interactor;

    public Controller() {
        // Create instances of the Model and View classes
        Model model = createModel();
        interactor = createInteractor(model);
        view = createView(model);
    }

    protected abstract Interactor createInteractor(Model model);
    protected abstract Model createModel();
    protected abstract View createView(Model model);

    public abstract Region getView();
}
