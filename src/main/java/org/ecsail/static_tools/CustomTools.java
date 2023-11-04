package org.ecsail.static_tools;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.ecsail.mvci_membership.InvoiceView;
import org.ecsail.mvci_membership.MembershipMessage;
import org.ecsail.mvci_membership.MembershipView;

import java.util.regex.Pattern;

public class CustomTools {
    public static void removeExistingTabAndCreateNew(MembershipView membershipView) {
        // Regex pattern to match a 4-digit integer
        Pattern yearPattern = Pattern.compile("^\\d{4}$");
        // Stream through tabs and remove ones matching the pattern
        membershipView.getMembershipModel().getInfoTabPane().getTabs().removeIf(t ->
                yearPattern.matcher(t.getText()).matches()
        );
        Tab tab = new InvoiceView(membershipView).build();
        membershipView.getMembershipModel().getInfoTabPane().getTabs().add(tab);
        membershipView.getMembershipModel().getInfoTabPane().getSelectionModel().select(tab);
        membershipView.sendMessage().accept(MembershipMessage.SELECT_INVOICE);
    }

    public static void selectTabByText(String text, TabPane tabPane) {
        tabPane.getTabs().stream()
                .filter(tab -> text.equals(tab.getText()))
                .findFirst()
                .ifPresent(tab -> tabPane.getSelectionModel().select(tab));
    }
}
