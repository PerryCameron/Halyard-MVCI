package org.ecsail.mvci_membership;

import javafx.beans.property.SimpleObjectProperty;
import org.ecsail.dto.MembershipListDTO;

public class MembershipModel {

    SimpleObjectProperty<MembershipListDTO> membership = new SimpleObjectProperty<>();




    public MembershipListDTO getMembership() {
        return membership.get();
    }
    public SimpleObjectProperty<MembershipListDTO> membershipProperty() {
        return membership;
    }
    public void setMembership(MembershipListDTO membership) {
        this.membership.set(membership);
    }
    public MembershipModel(MembershipListDTO membershipListDTO) {
        membership.set(membershipListDTO);
    }
}
