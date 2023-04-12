package org.ecsail.mvci_roster;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.SettingsRepositoryImpl;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.ecsail.static_calls.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RosterInteractor {

    private static final Logger logger = LoggerFactory.getLogger(RosterInteractor.class);
    private RosterModel rosterModel;
    private MembershipRepository membershipRepo;
    private SettingsRepository settingsRepo;

    public RosterInteractor(RosterModel rm, Connections connections) {
        rosterModel = rm;
        membershipRepo = new MembershipRepositoryImpl(connections.getDataSource());
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
    }

    public void getSelectedRoster() {
        try {
            rosterModel.setRosters(FXCollections.observableArrayList(membershipRepo.getAllRoster(rosterModel.getSelectedYear())));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public void setRosterToTableview() {
        rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
        rosterModel.getRosters().sort(Comparator.comparing(MembershipListDTO::getMembershipId));

        updateRecords(rosterModel.getRosters().size());
    }

    private void updateRecords(int number) { rosterModel.setNumberOfRecords(String.valueOf(number)); }

    public void changeState() {
        rosterModel.getRosters().clear();
        Method method;
        try {
            method = membershipRepo.getClass().getMethod(rosterModel.getSelectedRadioBox().getMethod(),Integer.class);
            rosterModel.getRosters().setAll((List<MembershipListDTO>) method.invoke(membershipRepo, rosterModel.getSelectedYear()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateRecords(rosterModel.getRosters().size());
        rosterModel.getRosters().sort(Comparator.comparing(MembershipListDTO::getMembershipId));
    }

    public void getRadioChoices() {
        try {
        rosterModel.getRadioChoices().addAll(FXCollections.observableArrayList(settingsRepo.getRadioChoices()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void fillTableView() {
        if(!rosterModel.getTextFieldString().equals("")) {
            rosterModel.getSearchedRosters().clear();
            rosterModel.getSearchedRosters().addAll(searchString(rosterModel.getTextFieldString()));
            rosterModel.getRosterTableView().setItems(rosterModel.getSearchedRosters());
            rosterModel.setIsActiveSearch(true);
        } else { // if search box has been cleared
            rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
            rosterModel.setIsActiveSearch(false);
            rosterModel.getSearchedRosters().clear();
        }
    }

    private ObservableList<MembershipListDTO> searchString(String searchTerm) {
        String text = searchTerm.toLowerCase();
        ObservableList<MembershipListDTO> searchedMemberships = FXCollections.observableArrayList();
        boolean hasMatch = false;
        for(MembershipListDTO membershipListDTO: rosterModel.getRosters()) {
            Field[] fields1 = membershipListDTO.getClass().getDeclaredFields();
            Field[] fields2 = membershipListDTO.getClass().getSuperclass().getDeclaredFields();
            Field[] allFields = new Field[fields1.length + fields2.length];
            Arrays.setAll(allFields, i -> (i < fields1.length ? fields1[i] : fields2[i - fields1.length]));
            for(Field field: allFields) {
                if(fieldIsSearchable(field.getName())) {
                    field.setAccessible(true);
                    String value = StringTools.returnFieldValueAsString(field, membershipListDTO).toLowerCase();
                    if (value.contains(text)) hasMatch = true;
                }
            }  // add boat DTO here
            if(hasMatch)
                searchedMemberships.add(membershipListDTO);
            hasMatch = false;
        }
        return searchedMemberships;
    }

    private boolean fieldIsSearchable(String fieldName) {
        return rosterModel.getCheckBoxes().stream()
                .filter(dto -> dto.getDTOFieldName().equals(fieldName))
                .findFirst()
                .map(SettingsCheckBox::isSearchable)
                .orElse(false);
    }

    public void getRosterSettings() {
        rosterModel.setRosterSettings(FXCollections.observableArrayList(settingsRepo.getSearchableListItems()));
    }

    public void setListsLoaded() { rosterModel.setListsLoaded(true); }

}
