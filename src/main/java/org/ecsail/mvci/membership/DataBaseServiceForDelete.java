package org.ecsail.mvci.membership;

//import org.ecsail.repository.implementations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class DataBaseServiceForDelete {
    private final MembershipModel membershipModel;
//    private final PersonRepository peopleRepo;
//    private final PhoneRepository phoneRepo;
//    private final EmailRepository emailRepo;
//    private final AwardRepository awardRepo;
//    private final OfficerRepository officerRepo;
//    private final MembershipIdRepository membershipIdRepo;
//    private final BoatRepository boatRepo;
//    private final SlipRepository slipRepo;
//    private final NotesRepository notesRepo;
//    private final MembershipRepository membershipRepo;
//    private final InvoiceRepository invoiceRepo;
    private static final Logger logger = LoggerFactory.getLogger(DataBaseServiceForDelete.class);


    public DataBaseServiceForDelete(DataSource dataSource, MembershipModel membershipModel) {
        this.membershipModel = membershipModel;
//        peopleRepo = new PersonRepositoryImpl(dataSource);
//        phoneRepo = new PhoneRepositoryImpl(dataSource);
//        emailRepo = new EmailRepositoryImpl(dataSource);
//        awardRepo = new AwardRepositoryImpl(dataSource);
//        officerRepo = new OfficerRepositoryImpl(dataSource);
//        membershipIdRepo = new MembershipIdRepositoryImpl(dataSource);
//        boatRepo = new BoatRepositoryImpl(dataSource);
//        slipRepo = new SlipRepositoryImpl(dataSource);
//        notesRepo = new NotesRepositoryImpl(dataSource);
//        membershipRepo = new MembershipRepositoryImpl(dataSource);
//        invoiceRepo = new InvoiceRepositoryImpl(dataSource);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////  SELECT  ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    public void selectPersons() { // not on FX thread because lists added before UI is launched
//        HandlingTools.queryForList(() -> {
//            List<PersonDTO> personDTOS = peopleRepo.getActivePeopleByMsId(membershipModel.getMembership().getMsId());
//            for (PersonDTO person : personDTOS) {
//                person.setPhones(FXCollections.observableArrayList(phoneRepo.getPhoneByPid(person.getpId())));
//                person.setEmail(FXCollections.observableArrayList(emailRepo.getEmail(person.getpId())));
//                person.setAwards(FXCollections.observableArrayList(awardRepo.getAwards(person)));
//                person.setOfficer(FXCollections.observableArrayList(officerRepo.getOfficer(person)));
//            }
//            membershipModel.setPeople(FXCollections.observableArrayList(personDTOS));
//        }, membershipModel.getMainModel(), logger);
//    }
//
//    public void selectIds() {
//        HandlingTools.queryForList(() -> {
//            List<MembershipIdDTO> membershipIdDTOS = membershipIdRepo.getIds(membershipModel.getMembership().getMsId());
//            Platform.runLater(() -> {
//                membershipModel.getMembership().setMembershipIdDTOS(FXCollections.observableArrayList(membershipIdDTOS));
//                membershipModel.getIdTableView().setItems(membershipModel.getMembership().getMembershipIdDTOS());
//                membershipModel.getMembership().getMembershipIdDTOS()
//                        .sort(Comparator.comparing(MembershipIdDTO::getFiscalYear).reversed());
//            });
//        }, membershipModel.getMainModel(), logger);
//    }
//
//    public void selectInvoices() {
//        HandlingTools.queryForList(() -> {
//            List<InvoiceDTO> invoiceDTOS = invoiceRepo.getInvoicesByMsid(membershipModel.getMembership().getMsId());
//            Platform.runLater(() -> {
//                membershipModel.getMembership().setInvoiceDTOS(FXCollections.observableArrayList(invoiceDTOS));
//                membershipModel.getInvoiceListTableView().setItems(membershipModel.getMembership().getInvoiceDTOS());
//                membershipModel.getMembership().getInvoiceDTOS().sort(Comparator.comparing(InvoiceDTO::getYear).reversed());
//            });
//        }, membershipModel.getMainModel(), logger);
//    }
//
//    public void selectBoats() {
//        HandlingTools.queryForList(() -> {
//            List<BoatDTO> boats = boatRepo.getBoatsByMsId(membershipModel.getMembership().getMsId());
//            Platform.runLater(() -> membershipModel.getMembership()
//                    .setBoatDTOS(FXCollections.observableArrayList(boats)));
//        }, membershipModel.getMainModel(), logger);
//    }
//
//    public void selectNotes() {
//        HandlingTools.queryForList(() -> {
//            List<NotesDTO> notesDTOS = notesRepo.getMemosByMsId(membershipModel.getMembership().getMsId());
//            Platform.runLater(() -> membershipModel.getMembership()
//                    .setNotesDTOS(FXCollections.observableArrayList(notesDTOS)));
//            membershipModel.getMembership().getNotesDTOS().sort(Comparator.comparing(NotesDTO::getMemoDate).reversed());
//        }, membershipModel.getMainModel(), logger);
//    }
//
//    public void selectSlipInfo() {
//        HandlingTools.queryForList(() -> Platform.runLater(()
//                -> membershipModel.setSlip(slipRepo.getSlip(membershipModel.getMembership().getMsId()))), membershipModel.getMainModel(), logger);
//        logger.info("Slip is loaded");
//        Platform.runLater(() -> {
//            // member does not own a slip
//            if (membershipModel.getSlip().getMs_id() == 0) membershipModel.setSlipRelationStatus(SlipUser.slip.noSlip);
//                // member owns a slip
//            else if (membershipModel.getSlip().getMs_id() == membershipModel.getMembership().getMsId()) {
//                // member owns slip and is not subleasing
//                if(membershipModel.getSlip().getSubleased_to() == 0) membershipModel.setSlipRelationStatus(SlipUser.slip.owner);
//                    // member owns slip but is subleasing
//                else setOwnAndSublease();
//                // member does not own but is subleasing
//            } else setSubLeaser();
//        });
//    }
//
//    public void selectInvoice() {
//        HandlingTools.queryForList(() -> {
//            List<InvoiceItemDTO> invoiceItemDTOS = invoiceRepo.getInvoiceItemsByInvoiceId(membershipModel.getSelectedInvoice().getId());
//            List<PaymentDTO> paymentDTOS = invoiceRepo.getPaymentByInvoiceId(membershipModel.getSelectedInvoice().getId());
//            Platform.runLater(() -> {
//                membershipModel.getSelectedInvoice().getInvoiceItemDTOS().clear();
//                membershipModel.getSelectedInvoice().getPaymentDTOS().clear();
//                membershipModel.getSelectedInvoice().setInvoiceItemDTOS(FXCollections.observableArrayList(invoiceItemDTOS));
//                membershipModel.getSelectedInvoice().setPaymentDTOS(FXCollections.observableArrayList(paymentDTOS));
//                // always choose the opposite of what the boolean is so the listener will trigger
//                membershipModel.getSelectedInvoice().setListLoaded(!membershipModel.getSelectedInvoice().isListLoaded());
//            });
//        }, membershipModel.getMainModel(), logger);
//    }
//
//    public void selectFees() {
//        HandlingTools.queryForList(() -> {
//            List<FeeDTO> feeDTOS = invoiceRepo.getFeesFromYear(membershipModel.getSelectedInvoice().getYear());
//            List<DbInvoiceDTO> dbInvoiceDTOS = invoiceRepo.getDbInvoiceByYear(membershipModel.getSelectedInvoice().getYear());
//            Platform.runLater(() -> {
//                membershipModel.getSelectedInvoice().setFeeDTOS(FXCollections.observableArrayList(feeDTOS));
//                membershipModel.getSelectedInvoice().setDbInvoiceDTOS(FXCollections.observableArrayList(dbInvoiceDTOS));
//                membershipModel.getSelectedInvoice().feesLoadedProperty().set(!membershipModel.getSelectedInvoice().isFeesLoaded());
//            });
//        }, membershipModel.getMainModel(), logger);
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////  UPDATE  ////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    protected void updateMembershipList() { // UPDATE_MEMBERSHIP_LIST
//        HandlingTools.executeQuery(() ->
//                membershipRepo.update(membershipModel.getMembership()), membershipModel.getMainModel(), logger);
//    }
//
//    protected void updateBoat() {
//        HandlingTools.executeQuery(() ->
//                boatRepo.update(membershipModel.getSelectedBoat()), membershipModel.getMainModel(), logger);
//    }
//    protected void updateMembershipId() {
//        HandlingTools.executeQuery(() ->
//                membershipIdRepo.update(membershipModel.getSelectedMembershipId()), membershipModel.getMainModel(), logger);
//    }
//
//    protected void updateNote() {
//        HandlingTools.executeQuery(() ->
//                notesRepo.update(membershipModel.getSelectedNote()), membershipModel.getMainModel(), logger);
//    }
//
//    public void updatePhone() {
//        HandlingTools.executeQuery(() ->
//                phoneRepo.update(membershipModel.getSelectedPhone()), membershipModel.getMainModel(), logger);
//    }
//
//    protected void updateEmail() {
//        HandlingTools.executeQuery(() ->
//                emailRepo.update(membershipModel.getSelectedEmail()), membershipModel.getMainModel(), logger);
//    }
//
//    protected void updateAward() {
//        HandlingTools.executeQuery(() ->
//                awardRepo.update(membershipModel.getSelectedAward()), membershipModel.getMainModel(), logger);
//    }
//
//    protected void updateOfficer() {
//        HandlingTools.executeQuery(() ->
//                officerRepo.update(membershipModel.getSelectedOfficer()), membershipModel.getMainModel(), logger);
//    }
//
//    protected void updatePerson() {
//        HandlingTools.executeQuery(() ->
//                peopleRepo.update(membershipModel.getSelectedPerson()), membershipModel.getMainModel(), logger);
//    }
//
//    public void updateInvoice() {
//        boolean successful = HandlingTools.executeBatchQuery(() ->
//                invoiceRepo.updateBatch(membershipModel.getSelectedInvoice()), membershipModel.getMainModel(), logger
//        );
//        if(successful) membershipModel.setInvoiceSaved(Success.YES);
//        else membershipModel.setInvoiceSaved(Success.NO);
//    }
//
//    public void saveInvoice() {
//        HandlingTools.executeBatchQuery(() ->
//                invoiceRepo.updateBatch(membershipModel.getSelectedInvoice()), membershipModel.getMainModel(), logger
//        );
//    }
//
//    public void updateInvoiceOnly() {
//    HandlingTools.executeQuery(() ->
//            invoiceRepo.update(membershipModel.getSelectedInvoice()), membershipModel.getMainModel(), logger);
//    }
//
//    public void updatePayment() {
//        HandlingTools.executeQuery(() ->
//                invoiceRepo.update(membershipModel.getSelectedPayment()), membershipModel.getMainModel(), logger);
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////  DELETE  ////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    protected void deletePerson() {
//        if (HandlingTools.executeQuery(() -> peopleRepo.delete(membershipModel.getSelectedPerson()),
//                membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.setReturnMessage(MembershipMessage.DELETE_MEMBER_FROM_DATABASE_SUCCEED);
//            });
//        else
//            Platform.runLater(() -> {
//                membershipModel.setReturnMessage(MembershipMessage.DELETE_MEMBER_FROM_DATABASE_FAIL);
//            });
//    }
//
//    protected void deletePhone() {
//        if (HandlingTools.executeQuery(() -> phoneRepo.delete(membershipModel.getSelectedPhone()),
//            membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.getSelectedPerson().getPhones().remove(membershipModel.getSelectedPhone());
//            });
//    }
//
//    protected void deleteEmail() {
//        if (HandlingTools.executeQuery(() -> emailRepo.delete(membershipModel.getSelectedEmail()),
//                membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.getSelectedPerson().getEmail().remove(membershipModel.getSelectedEmail());
//            });
//    }
//
//    public void deleteAward() {
//        if (HandlingTools.executeQuery(() -> awardRepo.delete(membershipModel.getSelectedAward()),
//                membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.getSelectedPerson().getAwards().remove(membershipModel.getSelectedAward());
//            });
//    }
//
//    protected void deleteOfficer() {
//        if (HandlingTools.executeQuery(() -> officerRepo.delete(membershipModel.getSelectedOfficer()),
//                membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.getSelectedPerson().getOfficers().remove(membershipModel.getSelectedOfficer());
//            });
//    }
//
//    protected void deleteNote() {
//        if (HandlingTools.executeQuery(() -> notesRepo.delete(membershipModel.getSelectedNote()),
//                membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.getMembership().getNotesDTOS().remove(membershipModel.getSelectedNote());
//            });
//    }
//
//    protected void deleteBoat() {  /// only removes boat owner
//        if (HandlingTools.executeQuery(() -> boatRepo.deleteBoatOwner(membershipModel.getMembership(), membershipModel.getSelectedBoat()),
//                membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.getMembership().getBoatDTOS().remove(membershipModel.getSelectedBoat());
//            });
//    }
//
//    public void deleteMembershipId() {
//        if (HandlingTools.executeQuery(() -> membershipIdRepo.delete(membershipModel.getSelectedMembershipId()),
//                membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.getMembership().getMembershipIdDTOS().remove(membershipModel.getSelectedMembershipId());
//            });
//    }
//
//    public void deletePayment() {
//        PaymentDTO paymentDTO = membershipModel.getSelectedPayment();
//        if (HandlingTools.executeQuery(() -> invoiceRepo.delete(paymentDTO),
//                membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> membershipModel.getSelectedInvoice().getPaymentDTOS().remove(paymentDTO));
//    }
//
//    public void deleteInvoice() {
//        if (HandlingTools.executeQuery(() -> invoiceRepo.deleteItemsByInvoiceID(membershipModel.getSelectedInvoice()),
//                membershipModel.getMainModel(), logger))  // delete items
//            if (HandlingTools.executeQuery(() -> invoiceRepo.deletePaymentsByInvoiceID(membershipModel.getSelectedInvoice()),
//                    membershipModel.getMainModel(), logger))  // delete payments
//                if (HandlingTools.executeQuery(() -> invoiceRepo.delete(membershipModel.getSelectedInvoice()),
//                        membershipModel.getMainModel(), logger))  // delete invoice
//                    membershipModel.getMembership().getInvoiceDTOS().remove(membershipModel.getSelectedInvoice());
//                else
//                    logger.error("Failed to delete invoice {}", membershipModel.getSelectedInvoice().getId());
//            else
//                logger.error("Failed to delete payments for invoice {}", membershipModel.getSelectedInvoice().getId());
//        else
//            logger.error("Failed to delete invoice {}", membershipModel.getSelectedInvoice().getId());
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////  INSERT  ////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public void insertPhone() {
//        PhoneDTO phoneDTO = new PhoneDTO(membershipModel.getSelectedPerson().getpId());
//        if (HandlingTools.executeQuery(() -> phoneRepo.insert(phoneDTO), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                membershipModel.setSelectedPhone(phoneDTO);
//                logger.info("Created new phone entry");
//                membershipModel.getSelectedPerson().getPhones().add(phoneDTO);
//                membershipModel.getSelectedPerson().getPhones().sort(Comparator.comparing(PhoneDTO::getPhoneId).reversed());
//                TableViewFx.requestFocusOnTable(membershipModel.getPhoneTableView().get(membershipModel.getSelectedPerson()));
//            });
//        }
//    }
//
//    public void insertEmail() {
//        EmailDTO emailDTO = new EmailDTO(membershipModel.getSelectedPerson().getpId());
//        if (HandlingTools.executeQuery(() -> emailRepo.insert(emailDTO), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                membershipModel.setSelectedEmail(emailDTO);
//                membershipModel.getSelectedPerson().getEmail().add(emailDTO);
//                membershipModel.getSelectedPerson().getEmail().sort(Comparator.comparing(EmailDTO::getEmail_id).reversed());
//                TableViewFx.requestFocusOnTable(membershipModel.getEmailTableView().get(membershipModel.getSelectedPerson()));
//            });
//        }
//    }
//
//    public void insertAward() {
//        AwardDTO awardDTO = new AwardDTO(membershipModel.getSelectedPerson().getpId());
//        if (HandlingTools.executeQuery(() -> awardRepo.insert(awardDTO), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                membershipModel.setSelectedAward(awardDTO);
//                membershipModel.getSelectedPerson().getAwards().add(awardDTO);
//                membershipModel.getSelectedPerson().getAwards().sort(Comparator.comparing(AwardDTO::getAwardId).reversed());
//                TableViewFx.requestFocusOnTable(membershipModel.getAwardTableView().get(membershipModel.getSelectedPerson()));
//            });
//        }
//    }
//
//    public void insertOfficer() {
//        OfficerDTO officerDTO = new OfficerDTO(membershipModel.getSelectedPerson().getpId());
//        if (HandlingTools.executeQuery(() -> officerRepo.insert(officerDTO), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                membershipModel.setSelectedOfficer(officerDTO);
//                membershipModel.getSelectedPerson().getOfficers().add(officerDTO);
//                membershipModel.getSelectedPerson().getOfficers().sort(Comparator.comparing(OfficerDTO::getOfficerId).reversed());
//                TableViewFx.requestFocusOnTable(membershipModel.getOfficerTableView().get(membershipModel.getSelectedPerson()));
//            });
//        }
//    }
//
//    public void insertNote(String note) {
//        NotesDTO notesDTO = new NotesDTO("N", membershipModel.getMembership().getMsId());
//        notesDTO.setMemo(note);
//        if (HandlingTools.executeQuery(() -> notesRepo.insertNote(notesDTO), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                membershipModel.setSelectedNote(notesDTO);
//                membershipModel.getMembership().getNotesDTOS().add(notesDTO);
//                membershipModel.getMembership().getNotesDTOS().sort(Comparator.comparing(NotesDTO::getMemoId).reversed());
//                TableViewFx.requestFocusOnTable(membershipModel.getNotesTableView());
//            });
//        }
//    }
//
//    protected void insertBoat() {
//        BoatDTO boatDTO = new BoatDTO(membershipModel.getMembership().getMsId());
//        if (HandlingTools.executeQuery(() -> boatRepo.insert(boatDTO), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                membershipModel.setSelectedBoat(boatDTO);
//                membershipModel.getMembership().getBoatDTOS().add(boatDTO);
//                membershipModel.getMembership().getBoatDTOS().sort(Comparator.comparing(BoatDTO::getBoatId).reversed());
//                TableViewFx.requestFocusOnTable(membershipModel.getNotesTableView());
//            });
//        }
//    }
//
//    public void insertMembershipId() {
//        MembershipIdDTO membershipIdDTO = new MembershipIdDTO(
//                membershipModel.getMembership().getSelectedYear(),
//                membershipModel.getMembership().getMsId(),
//                membershipModel.getMembership().getMembershipId(),
//                membershipModel.getMembership().getMemType());
//        if (HandlingTools.executeQuery(() -> membershipIdRepo.insert(membershipIdDTO), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                membershipModel.setSelectedMembershipId(membershipIdDTO);
//                membershipModel.getMembership().getMembershipIdDTOS().add(membershipIdDTO);
//                membershipModel.getMembership().getMembershipIdDTOS().sort(Comparator.comparing(MembershipIdDTO::getmId).reversed());
//                TableViewFx.requestFocusOnTable(membershipModel.getIdTableView());
//            });
//        }
//    }
//    public void insertPerson() {
//        if (HandlingTools.executeQuery(() -> peopleRepo.insert(membershipModel.getSelectedPerson()), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                PersonDTO p = membershipModel.getSelectedPerson();
//                insertNote("New Person: " + p.getFullName() + "(p_id " + p.getpId()
//                        + ") added as " + MemberType.getByCode(p.getMemberType()));
//                membershipModel.setReturnMessage(MembershipMessage.INSERT_PERSON_SUCCEED);
//            });
//        }
//    }
//
//    public void insertPayment() {
//        PaymentDTO paymentDTO = new PaymentDTO(0, membershipModel.getSelectedInvoice().getId(), null, "CH", DateTools.getDate(), "0", 1);
//        if(HandlingTools.executeQuery(() -> invoiceRepo.insert(paymentDTO), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                membershipModel.getSelectedInvoice().getPaymentDTOS().add(paymentDTO);
//            });
//        } else System.out.println("Failed to add paymentDTO");
//    }
//
//    public void insertInvoiceNote() {
//        // create our DTO
//        NotesDTO notesDTO = new NotesDTO("I", membershipModel.getMembership().getMsId());
//        notesDTO.setInvoiceId(membershipModel.getSelectedInvoice().getId());
//        notesDTO.setMemoDate(LocalDate.parse(membershipModel.getSelectedInvoice().getPaymentDTOS().get(0).getPaymentDate()));
//        // add noteDTO to database
//        if (HandlingTools.executeQuery(() -> notesRepo.insertNote(notesDTO), membershipModel.getMainModel(), logger)) {
//            Platform.runLater(() -> {
//                // add and sort
//                membershipModel.getMembership().getNotesDTOS().add(0, notesDTO);
//                membershipModel.getExtraTabPane().getTabs().stream()
//                        .filter(tab -> "Notes".equals(tab.getText()))
//                        .findFirst()
//                        .ifPresent(tab -> membershipModel.getExtraTabPane().getSelectionModel().select(tab));
//                // let us open it for editing
//                membershipModel.getNotesTableView().getSelectionModel().select(0);
//                Optional<TableColumn<NotesDTO, ?>> noteColumnOptional = membershipModel
//                        .getNotesTableView().getColumns().stream()
//                        .filter(column -> "Note".equals(column.getText()))
//                        .findFirst();
//                if (noteColumnOptional.isPresent()) {
//                    TableColumn<NotesDTO, ?> noteColumn = noteColumnOptional.get();
//                    membershipModel.getNotesTableView().requestFocus();
//                    membershipModel.getNotesTableView().edit(0, noteColumn);
//                }
//            });
//        }
//    }
//
////    warning: [unchecked] unchecked cast
////                        .map(column -> (TableColumn<NotesDTO, String>) column)
////            ^
////    required: TableColumn<NotesDTO,String>
////    found:    TableColumn<NotesDTO,CAP#1>
////    where CAP#1 is a fresh type-variable:
////    CAP#1 extends Object from capture of ?
//
//
//    public void insertInvoice() {
//        MembershipListDTO membership = membershipModel.getMembership();
//        InvoiceDTO invoiceDTO = new InvoiceDTO(membership.getMsId(), membershipModel.getSelectedInvoiceCreateYear());
//        invoiceDTO.setSupplemental(invoiceExists());
//        HandlingTools.executeQuery(() -> invoiceRepo.insert(invoiceDTO), membershipModel.getMainModel(), logger);
//        List<DbInvoiceDTO> dbInvoiceDTOS = invoiceRepo.getDbInvoiceByYear(membershipModel.getSelectedInvoiceCreateYear());
//        List<FeeDTO> feeDTOS = invoiceRepo.getFeesFromYear(membershipModel.getSelectedInvoiceCreateYear());
//        for (DbInvoiceDTO dbInvoiceDTO : dbInvoiceDTOS) {
//            InvoiceItemDTO item = new InvoiceItemDTO(
//                    0,
//                    invoiceDTO.getId(),
//                    invoiceDTO.getMsId(),
//                    invoiceDTO.getYear(),
//                    dbInvoiceDTO.getFieldName(),
//                    dbInvoiceDTO.isCredit(),
//                    "0.00", 0, false, "none");
//            if (dbInvoiceDTO.isItemized()) {
//                item.setCategory(dbInvoiceDTO.isItemized());
//                invoiceDTO.getInvoiceItemDTOS().add(item);
//                // make categories
//                feeDTOS.stream().filter(feeDTO -> feeDTO.getFieldName().equals(item.getFieldName())).forEach(feeDTO ->
//                        invoiceDTO.getInvoiceItemDTOS().add(new InvoiceItemDTO(0, invoiceDTO.getId(),
//                                invoiceDTO.getMsId(), invoiceDTO.getYear(), feeDTO.getDescription()
//                                , dbInvoiceDTO.isCredit(), "0.00", 0, false, dbInvoiceDTO.getFieldName())));
//            } else invoiceDTO.getInvoiceItemDTOS().add(item);
//        }
//        boolean successfulItemCreation = HandlingTools.executeBatchQuery(() ->
//                invoiceRepo.insertBatch(invoiceDTO), membershipModel.getMainModel(), logger
//        );
//        if(successfulItemCreation) {
//            Platform.runLater(() -> {
//                membershipModel.setSelectedInvoice(invoiceDTO);
//                membershipModel.getMembership().getInvoiceDTOS().add(invoiceDTO);
//            });
//        }
////        System.out.println(invoiceDTO.toFullInvoiceString());
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////   MISC   ////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    protected void movePerson() {
//        System.out.println("Move Person");
//    }
//
//    public boolean invoiceExists() {
//        int selectedYear = membershipModel.getSelectedInvoiceCreateYear();
//        return HandlingTools.executeExistsQuery(() ->
//                invoiceRepo.exists(membershipModel.getMembership(),selectedYear), membershipModel.getMainModel(), logger);
//    }
//
//    private void setSubLeaser() {
//        membershipModel.setSlipRelationStatus(SlipUser.slip.subLeaser);
//        // gets the current id of the slip owner
//        membershipModel.setMembershipId(String.valueOf(membershipIdRepo.getCurrentId(membershipModel.getSlip().getMs_id()).getMembershipId()));
//    }
//
//    private void setOwnAndSublease() { // already inside Platform.runLater
//        membershipModel.setSlipRelationStatus(SlipUser.slip.ownAndSublease);
//        // gets the id of the subLeaser for the current year
//        membershipModel.setMembershipId(String.valueOf(membershipIdRepo.getCurrentId(membershipModel.getSlip().getSubleased_to()).getMembershipId()));
//    }
//
//    protected void changeMemberType() {
//        System.out.println("changeMemberType");
//    }
//
//    protected void detachMemberFromMembership() { // DETACH_MEMBER_FROM_MEMBERSHIP
//        if(HandlingTools.executeQuery(() ->
//                peopleRepo.update(membershipModel.getSelectedPerson()), membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.setReturnMessage(MembershipMessage.DELETE_MEMBER_FROM_DATABASE_SUCCEED);
//            });
//    }
//
//    public void detachPrimaryMemberFromMembership() { // DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP
//        if(HandlingTools.executeQuery(() ->
//                peopleRepo.update(membershipModel.getSelectedPerson()), membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.setReturnMessage(MembershipMessage.DELETE_PRIMARY_MEMBER_FROM_DATABASE_SUCCEED);
//            });
//    }
//
//    protected void swapSecondaryToPrimary() { // MOVE_SECONDARY_TO_PRIMARY
//        if(HandlingTools.executeQuery(() ->
//                peopleRepo.update(membershipModel.getSelectedPerson()), membershipModel.getMainModel(), logger))
//            Platform.runLater(() -> {
//                membershipModel.setReturnMessage(MembershipMessage.MOVE_SECONDARY_TO_PRIMARY_SUCCEED);
//            });
//    }
//
//    public MembershipListDTO getMembershipListByIdAndYear(int membershipId, int year) {
//        return membershipRepo.getMembershipListByIdAndYear(membershipId,year);
//    }
//
//    public List<MembershipIdDTO> getAllMembershipIdsByYear(int year) { {
//        return membershipIdRepo.getAllMembershipIdsByYear(year);
//    }}
//
//    public MembershipDTO getCurrentMembershipChair() {
//        return membershipRepo.getCurrentMembershipChair();
//    }
//
//    public boolean existsSlipWithMsId() {
//        return slipRepo.existsSlipWithMsId(membershipModel.getMembership().getMsId());
//    }
//
//    public int deleteBoatOwner(int msId) {
//        return boatRepo.deleteBoatOwner(msId);
//    }
//
//
//    public int deleteNotes(int msId) {
//        return notesRepo.deleteNotes(msId);
//    }
//
//    public int[] deleteAllPaymentsAndInvoicesByMsId(int msId) {
//        return invoiceRepo.deleteAllPaymentsAndInvoicesByMsId(msId);
//    }
//
//    public int deleteWaitList(int msId) {
//        return slipRepo.deleteWaitList(msId);
//    }
//
//    public int deleteFormMsIdHash(int msId) {
//        return membershipRepo.deleteFormMsIdHash(msId);
//    }
//
//    public int deleteMembershipId(int msId) {
//        return membershipIdRepo.deleteMembershipId(msId);
//    }
//
//    public List<PersonDTO> getPeople(int msId) {
//        return peopleRepo.getPeople(msId);
//    }
//
//    public int deleteMembership(int msId) {
//        return membershipRepo.deleteMembership(msId);
//    }
//
//    public int deletePhones(int pId) {
//        return phoneRepo.deletePhones(pId);
//    }
//
//    public int deleteEmail(int pId) {
//        return emailRepo.deleteEmail(pId);
//    }
//
//    public int deleteOfficer(int pId) {
//        return officerRepo.deleteOfficer(pId);
//    }
//
//    public int deletePerson(int pId) {
//        return peopleRepo.deletePerson(pId);
//    }
//
//    public int deleteUserAuthRequest(int pId) {
//        return peopleRepo.deleteUserAuthRequest(pId);
//    }
//
//    public int deleteAwards(int pId) {
//        return awardRepo.deleteAwards(pId);
//    }
}
