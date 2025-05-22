package org.ecsail.mvci_new_membership;

import javafx.application.Platform;
import org.ecsail.fx.MembershipListDTO;
import org.ecsail.fx.NotesDTOFx;
import org.ecsail.fx.PersonFx;
import org.ecsail.interfaces.ConfigFilePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class NewMembershipInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(NewMembershipInteractor.class);
    private final NewMembershipModel newMembershipModel;
//    private final MembershipRepository memRepo;
//    private final PersonRepository personRepo;
//    private final MembershipIdRepository memberIdRepo;
//    private final NotesRepositoryImpl notesRepo;

//    public NewMembershipInteractor(NewMembershipModel newMembershipModel, Connections connections) {
//        this.newMembershipModel = newMembershipModel;
//        this.memRepo = new MembershipRepositoryImpl(connections.getDataSource());
//        this.personRepo = new PersonRepositoryImpl(connections.getDataSource());
//        this.memberIdRepo = new MembershipIdRepositoryImpl(connections.getDataSource());
//        this.notesRepo = new NotesRepositoryImpl(connections.getDataSource());
//    }

    public NewMembershipInteractor(NewMembershipModel newMembershipModel) {
        this.newMembershipModel = newMembershipModel;
    }

    public MembershipListDTO getMembershipList() {
        return newMembershipModel.getMembership();
    }

    public void getNextAvailableId() {
        try {
//            int nextNumber = memberIdRepo.getMembershipIdForNewestMembership() + 1;
            Platform.runLater(() -> {
//                newMembershipModel.setMembershipId(nextNumber);
                newMembershipModel.setTabMessage("Creating new membership");
//                newMembershipModel.setTabMessage("Setting new membership number to: " + nextNumber);
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            newMembershipModel.setTabMessage(e.getMessage());
        }
    }

    public void createMembershipObject() {
        try {
            String joinDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            MembershipListDTO membershipListDTO = new MembershipListDTO();
            membershipListDTO.setSelectedYear(Year.now().getValue());
            membershipListDTO.setJoinDate(joinDate);
            membershipListDTO.setMemType("FM");
            membershipListDTO.setPId(0);
            membershipListDTO.setMembershipId(newMembershipModel.getMembershipId());
//            MembershipListDTO membership = memRepo.insertMembership(membershipListDTO);
//            if(membership.getMsId() != 0) {
//                Platform.runLater(() -> {
//                    newMembershipModel.setTabMessage("Creating Membership MSID: " + membership.getMsId());
//                    newMembershipModel.setMembership(membership);
//                });
//            } else {
//                Platform.runLater(() -> {
//                    newMembershipModel.setTabMessage("Failed to assign MSID: " + membership.getMsId());
//                });
//            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            newMembershipModel.setTabMessage(e.getMessage());
        }
    }

    public void createPrimaryMember() {
        try {
            PersonFx personDTO = new PersonFx(newMembershipModel.getMembership().getMsId(), 1, true);
            personDTO.setBirthday(LocalDate.of(1900, 1, 1));
//            PersonDTO person = personRepo.insertPerson(personDTO);
//            if(person.getpId() != 0) {
//                Platform.runLater(() -> {
//                    newMembershipModel.setTabMessage("Creating Primary Member for membership PID: " + person.getpId());
//                    newMembershipModel.getMembership().setPId(person.getpId());
//                });
//            } else {
//                Platform.runLater(() -> {
//                    newMembershipModel.setTabMessage("Failed to assign person an ID: " + person.getpId());
//                });
//            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            newMembershipModel.setTabMessage(e.getMessage());
        }
    }

    public void updateMembership() {
        try {
//        int ok = memRepo.update(newMembershipModel.getMembership());
//        if(ok == 1)
//            Platform.runLater(() -> {
//                newMembershipModel.setTabMessage("Updated Membership with primary member");
//            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            newMembershipModel.setTabMessage(e.getMessage());
        }
    }

    public void createMembershipIdRow() {
        try {
//            MembershipIdDTO membershipIdDTO = new MembershipIdDTO();
//            membershipIdDTO.setFiscalYear(Year.now().getValue());
//            membershipIdDTO.setMsId(newMembershipModel.getMembership().getMsId());
//            membershipIdDTO.setMemType(newMembershipModel.getMembership().getMemType());
//            membershipIdDTO.setMembershipId(newMembershipModel.getMembershipId());
//            membershipIdDTO.setIsRenew(true);
//        int ok = memberIdRepo.insert(membershipIdDTO);
//        if(ok == 1)
//            Platform.runLater(() -> {
//                newMembershipModel.setTabMessage("Created Membership ID Object, MID: " + membershipIdDTO.getmId());
//            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            newMembershipModel.setTabMessage(e.getMessage());
        }
    }

    public void createMemoToDocument() {
        try {
            NotesDTOFx notesDTO = new NotesDTOFx("N", newMembershipModel.getMembership().getMsId());
            notesDTO.setMemoDate(LocalDate.now());
            notesDTO.setMemo("Created new membership record " + LocalDateTime.now());
//            int ok = notesRepo.insertNote(notesDTO);
//            if(ok == 1)
//            Platform.runLater(() -> {
//                newMembershipModel.setTabMessage("Note to document membership creation created");
//            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            newMembershipModel.setTabMessage(e.getMessage());
        }
    }

    public void showSuccess() {
        Platform.runLater(() -> {
            newMembershipModel.setTabMessage("New membership successfully created!!!");
        });
    }
}
