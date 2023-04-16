package org.ecsail.mvci_membership;

import com.sun.tools.javac.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.mvci_main.MainModel;

public class MembershipModel {

    private ObservableList<PersonDTO> people = FXCollections.observableArrayList();
    private final SimpleObjectProperty<MembershipListDTO> membership = new SimpleObjectProperty<>();
    private final BooleanProperty listsLoaded = new SimpleBooleanProperty(false);
    private final MainModel mainModel;


    public MainModel getMainModel() {
        return mainModel;
    }
    public boolean isListsLoaded() {
        return listsLoaded.get();
    }
    public BooleanProperty listsLoadedProperty() {
        return listsLoaded;
    }
    public void setListsLoaded(boolean listsLoaded) {
        this.listsLoaded.set(listsLoaded);
    }
    public ObservableList<PersonDTO> getPeople() {
        return people;
    }
    public void setPeople(ObservableList<PersonDTO> people) {
        this.people = people;
    }
    public MembershipListDTO getMembership() {
        return membership.get();
    }
    public SimpleObjectProperty<MembershipListDTO> membershipProperty() {
        return membership;
    }
    public void setMembership(MembershipListDTO membership) {
        this.membership.set(membership);
    }
    public MembershipModel(MembershipListDTO membershipListDTO, MainModel mainModel) {
        membership.set(membershipListDTO);
        this.mainModel = mainModel;
    }


}
