package org.ecsail.mvci.newmembership;


import javafx.beans.property.*;
import javafx.scene.layout.VBox;
import org.ecsail.fx.MembershipListDTO;
import org.ecsail.fx.PersonFx;
import org.ecsail.mvci.main.MainModel;

public class NewMembershipModel {
    private final MainModel mainModel;
    private final ObjectProperty<VBox> whiteVBox = new SimpleObjectProperty<>();
    private final ObjectProperty<MembershipListDTO> membership = new SimpleObjectProperty<>();
    private final ObjectProperty<PersonFx> primary = new SimpleObjectProperty<>();
    private final StringProperty tabMessage = new SimpleStringProperty("Creating New Membership");
    private final IntegerProperty membershipId = new SimpleIntegerProperty();













    public NewMembershipModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public String getTabMessage() {
        return tabMessage.get();
    }

    public StringProperty tabMessageProperty() {
        return tabMessage;
    }

    public void setTabMessage(String tabMessage) {
        this.tabMessage.set(tabMessage);
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    public VBox getWhiteVBox() {
        return whiteVBox.get();
    }

    public ObjectProperty<VBox> whiteVBoxProperty() {
        return whiteVBox;
    }

    public void setWhiteVBox(VBox whiteVBox) {
        this.whiteVBox.set(whiteVBox);
    }

    public int getMembershipId() {
        return membershipId.get();
    }

    public IntegerProperty membershipIdProperty() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId.set(membershipId);
    }

    public MembershipListDTO getMembership() {
        return membership.get();
    }

    public ObjectProperty<MembershipListDTO> membershipProperty() {
        return membership;
    }

    public void setMembership(MembershipListDTO membership) {
        this.membership.set(membership);
    }

    public PersonFx getPrimary() {
        return primary.get();
    }

    public ObjectProperty<PersonFx> primaryProperty() {
        return primary;
    }

    public void setPrimary(PersonFx primary) {
        this.primary.set(primary);
    }
}
