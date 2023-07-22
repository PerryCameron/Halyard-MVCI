package org.ecsail.mvci_membership;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import org.ecsail.dto.*;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.repository.implementations.*;
import org.ecsail.repository.interfaces.*;
import org.ecsail.static_tools.HandlingTools;
import org.ecsail.widgetfx.TableViewFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;

public class DataBaseService {
    private final MembershipModel membershipModel;
    private final PersonRepository peopleRepo;
    private final PhoneRepository phoneRepo;
    private final EmailRepository emailRepo;
    private final AwardRepository awardRepo;
    private final OfficerRepository officerRepo;
    private final MembershipIdRepository membershipIdRepo;
    private final BoatRepository boatRepo;
    private static final Logger logger = LoggerFactory.getLogger(DataBaseService.class);
    private final SlipRepository slipRepo;
    private final NotesRepository notesRepo;
    private final MembershipRepository membershipRepo;


    public DataBaseService(DataSource dataSource, MembershipModel membershipModel) {
        this.membershipModel = membershipModel;
        peopleRepo = new PersonRepositoryImpl(dataSource);
        phoneRepo = new PhoneRepositoryImpl(dataSource);
        emailRepo = new EmailRepositoryImpl(dataSource);
        awardRepo = new AwardRepositoryImpl(dataSource);
        officerRepo = new OfficerRepositoryImpl(dataSource);
        membershipIdRepo = new MembershipIdRepositoryImpl(dataSource);
        boatRepo = new BoatRepositoryImpl(dataSource);
        slipRepo = new SlipRepositoryImpl(dataSource);
        notesRepo = new NotesRepositoryImpl(dataSource);
        membershipRepo = new MembershipRepositoryImpl(dataSource);
    }

    public void getPersonLists(MembershipListDTO ml) { // not on FX thread because lists added before UI is launched
        HandlingTools.queryForList(() -> {
            List<PersonDTO> personDTOS = peopleRepo.getActivePeopleByMsId(ml.getMsId());
            for (PersonDTO person : personDTOS) {
                person.setPhones(FXCollections.observableArrayList(phoneRepo.getPhoneByPid(person.getpId())));
                person.setEmail(FXCollections.observableArrayList(emailRepo.getEmail(person.getpId())));
                person.setAwards(FXCollections.observableArrayList(awardRepo.getAwards(person)));
                person.setOfficer(FXCollections.observableArrayList(officerRepo.getOfficer(person)));
            }
            membershipModel.setPeople(FXCollections.observableArrayList(personDTOS));
        }, membershipModel.getMainModel(), logger);
    }

    public void getIds(MembershipListDTO ml) {
        HandlingTools.queryForList(() -> {
            List<MembershipIdDTO> membershipIdDTOS = membershipIdRepo.getIds(ml.getMsId());
            Platform.runLater(() -> {
                membershipModel.getMembership().setMembershipIdDTOS(FXCollections.observableArrayList(membershipIdDTOS));
            });
        }, membershipModel.getMainModel(), logger);
    }

    public void getBoats(MembershipListDTO ml) {
        HandlingTools.queryForList(() -> {
            List<BoatDTO> boats = boatRepo.getBoatsByMsId(ml.getMsId());
            Platform.runLater(() -> {
                membershipModel.getMembership()
                        .setBoatDTOS(FXCollections.observableArrayList(boats));
            });
        }, membershipModel.getMainModel(), logger);
    }

    public void getNotes(MembershipListDTO ml) {
        HandlingTools.queryForList(() -> {
            List<NotesDTO> notesDTOS = notesRepo.getMemosByMsId(ml.getMsId());
            Platform.runLater(() -> membershipModel.getMembership()
                    .setNotesDTOS(FXCollections.observableArrayList(notesDTOS)));
        }, membershipModel.getMainModel(), logger);
    }

    public void getSlipInfo(MembershipListDTO ml) {
        HandlingTools.queryForList(() -> {
            Platform.runLater(() -> membershipModel.setSlip(slipRepo.getSlip(ml.getMsId())));
        }, membershipModel.getMainModel(), logger);
        logger.info("Slip is loaded");
        Platform.runLater(() -> {
            // member does not own a slip
            if (membershipModel.getSlip().getMs_id() == 0) membershipModel.setSlipRelationStatus(SlipUser.slip.noSlip);
                // member owns a slip
            else if (membershipModel.getSlip().getMs_id() == membershipModel.getMembership().getMsId()) {
                // member owns slip and is not subleasing
                if(membershipModel.getSlip().getSubleased_to() == 0) membershipModel.setSlipRelationStatus(SlipUser.slip.owner);
                    // member owns slip but is subleasing
                else setOwnAndSublease();
                // member does not own but is subleasing
            } else setSubLeaser();
        });
    }

    private void setSubLeaser() {
        membershipModel.setSlipRelationStatus(SlipUser.slip.subLeaser);
        // gets the current id of the slip owner
        membershipModel.setMembershipId(String.valueOf(membershipIdRepo.getCurrentId(membershipModel.getSlip().getMs_id()).getMembership_id()));
    }

    private void setOwnAndSublease() {
        membershipModel.setSlipRelationStatus(SlipUser.slip.ownAndSublease);
        // gets the id of the subLeaser for the current year
        membershipModel.setMembershipId(String.valueOf(membershipIdRepo.getCurrentId(membershipModel.getSlip().getSubleased_to()).getMembership_id()));
    }

    protected void updateMembershipList() {
        HandlingTools.executeQuery(() ->
                membershipRepo.update(membershipModel.getMembership()), membershipModel.getMainModel(), logger);
    }

    protected void updateBoat() {
        HandlingTools.executeQuery(() ->
                boatRepo.update(membershipModel.getSelectedBoat()), membershipModel.getMainModel(), logger);
    }

    protected void updateMembershipId() {
        HandlingTools.executeQuery(() ->
                membershipIdRepo.update(membershipModel.getSelectedMembershipId()), membershipModel.getMainModel(), logger);
    }

    protected void updateNote() {
        System.out.println("updateNote:"); // TODO this is triggering when you scroll the tableview (wierd)
        System.out.println(membershipModel.getSelectedNote()); // TODO I think I fixed
//        executeQuery(() -> notesRepo.update(membershipModel.getSelectedNote()));
    }

    protected void changeMemberType() {
        System.out.println("changeMemberType");
    }

    protected void removeMemberFromMembership() {
        System.out.println("remove from membership");
    }

    public void updatePhone() {
        HandlingTools.executeQuery(() ->
                phoneRepo.update(membershipModel.getSelectedPhone()), membershipModel.getMainModel(), logger);
    }

    protected void updateEmail() {
        HandlingTools.executeQuery(() ->
                emailRepo.update(membershipModel.getSelectedEmail()), membershipModel.getMainModel(), logger);
    }

    protected void updateAward() {
        HandlingTools.executeQuery(() ->
                awardRepo.update(membershipModel.getSelectedAward()), membershipModel.getMainModel(), logger);
    }

    public void updateOfficer() {
        HandlingTools.executeQuery(() ->
                officerRepo.update(membershipModel.getSelectedOfficer()), membershipModel.getMainModel(), logger);
    }

    public void updatePerson() {
        HandlingTools.executeQuery(() ->
                peopleRepo.update(membershipModel.getSelectedPerson()), membershipModel.getMainModel(), logger);
    }

    public void deletePerson() {
        System.out.println("Delete Person");
    }

    public void movePerson() {
        System.out.println("Move Person");
    }

    public void deleteMembershipId() {
        System.out.println("Delete Membership");
    }

    public void deleteNote() {
        System.out.println("Delete Note");
    }

    public void deletePhone() {
        if (HandlingTools.executeQuery(() -> phoneRepo.delete(membershipModel.getSelectedPhone()),
            membershipModel.getMainModel(), logger))
                membershipModel.getSelectedPerson().getPhones().remove(membershipModel.getSelectedPhone());
    }

    public void deleteEmail() {
        if (HandlingTools.executeQuery(() -> emailRepo.delete(membershipModel.getSelectedEmail()),
            membershipModel.getMainModel(), logger))
                membershipModel.getSelectedPerson().getEmail().remove(membershipModel.getSelectedEmail());
    }

    public void deleteAward() {
        if (HandlingTools.executeQuery(() -> awardRepo.delete(membershipModel.getSelectedAward()),
            membershipModel.getMainModel(), logger))
                membershipModel.getSelectedPerson().getAwards().remove(membershipModel.getSelectedAward());
    }

    public void deleteOfficer() {
        if (HandlingTools.executeQuery(() -> officerRepo.delete(membershipModel.getSelectedOfficer()),
            membershipModel.getMainModel(), logger))
                membershipModel.getSelectedPerson().getOfficers().remove(membershipModel.getSelectedOfficer());
    }

    protected void deleteBoat() {
        System.out.println("Delete Boat");
//        executeQuery(() -> boatRepo.delete(membershipModel.getSelectedBoat()));
    }


    public void insertMembershipId() {
        System.out.println("Insert MembershipId");
    }

    public void insertNote() {
        System.out.println("Insert Note");
    }

    public void insertPerson() {
        System.out.println("Insert Person");
    }

    public void insertPhone() {
        PhoneDTO phoneDTO = new PhoneDTO(membershipModel.getSelectedPerson().getpId());
        if (HandlingTools.executeQuery(() -> phoneRepo.insert(phoneDTO), membershipModel.getMainModel(), logger)) {
            Platform.runLater(() -> {
                membershipModel.setSelectedPhone(phoneDTO);
                logger.info("Created new phone entry");
                membershipModel.getSelectedPerson().getPhones().add(phoneDTO);
                membershipModel.getSelectedPerson().getPhones().sort(Comparator.comparing(PhoneDTO::getPhoneId).reversed());
                TableViewFx.requestFocusOnTable(membershipModel.getPhoneTableView().get(membershipModel.getSelectedPerson()));
            });
        }
    }

    public void insertEmail() {
        EmailDTO emailDTO = new EmailDTO(membershipModel.getSelectedPerson().getpId());
        if (HandlingTools.executeQuery(() -> emailRepo.insert(emailDTO), membershipModel.getMainModel(), logger)) {
            Platform.runLater(() -> {
                membershipModel.setSelectedEmail(emailDTO);
                membershipModel.getSelectedPerson().getEmail().add(emailDTO);
                membershipModel.getSelectedPerson().getEmail().sort(Comparator.comparing(EmailDTO::getEmail_id).reversed());
                TableViewFx.requestFocusOnTable(membershipModel.getEmailTableView().get(membershipModel.getSelectedPerson()));
            });
        }
    }

    public void insertAward() {
        AwardDTO awardDTO = new AwardDTO(membershipModel.getSelectedPerson().getpId());
        if (HandlingTools.executeQuery(() -> awardRepo.insert(awardDTO), membershipModel.getMainModel(), logger)) {
            Platform.runLater(() -> {
                membershipModel.setSelectedAward(awardDTO);
                membershipModel.getSelectedPerson().getAwards().add(awardDTO);
                membershipModel.getSelectedPerson().getAwards().sort(Comparator.comparing(AwardDTO::getAwardId).reversed());
                TableViewFx.requestFocusOnTable(membershipModel.getAwardTableView().get(membershipModel.getSelectedPerson()));
            });
        }
    }

    public void insertOfficer() {
        OfficerDTO officerDTO = new OfficerDTO(membershipModel.getSelectedPerson().getpId());
        if (HandlingTools.executeQuery(() -> officerRepo.insert(officerDTO), membershipModel.getMainModel(), logger)) {
            Platform.runLater(() -> {
                membershipModel.setSelectedOfficer(officerDTO);
                membershipModel.getSelectedPerson().getOfficers().add(officerDTO);
                membershipModel.getSelectedPerson().getOfficers().sort(Comparator.comparing(OfficerDTO::getOfficerId).reversed());
                TableViewFx.requestFocusOnTable(membershipModel.getOfficerTableView().get(membershipModel.getSelectedPerson()));
            });
        }
    }

    protected void insertBoat() {
        HandlingTools.executeQuery(() ->
                boatRepo.insert(membershipModel.getSelectedBoat()), membershipModel.getMainModel(), logger);
    }


}
