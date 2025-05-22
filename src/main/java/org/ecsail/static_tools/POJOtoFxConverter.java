package org.ecsail.static_tools;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.fx.*;
import org.ecsail.pojo.*;

import java.util.*;
import java.util.stream.Collectors;

public class POJOtoFxConverter {

    /**
     * Converts a list of {@link Roster} objects to an {@link ObservableList} of {@link RosterFx}
     * objects, sorted by ID in ascending order. The resulting list is suitable for JavaFX TableView binding.
     *
     * @param roster the list of {@link Roster} objects to convert, typically deserialized from JSON
     * @return an {@link ObservableList} of {@link RosterFx} objects, sorted by ID, or an empty list if the input is null or empty
     * @throws NullPointerException if any {@link Roster} in the input list is null (filtered out internally)
     */
    public static ObservableList<RosterFx> copyRoster(List<Roster> roster) {
        if (roster == null || roster.isEmpty()) {
            return FXCollections.observableArrayList();
        }
        List<RosterFx> rosterFxList = roster.stream()
                .filter(Objects::nonNull)
                .map(RosterFx::new)
                .sorted(Comparator.comparingInt(RosterFx::getId))
                .collect(Collectors.toList());
        return FXCollections.observableArrayList(rosterFxList);
    }

    /**
     * Converts a list of {@link Person} objects to an {@link ObservableList} of {@link PersonFx}
     * objects, including their nested collections (phones, emails, awards, officers). Awards are sorted
     * by award year, and officers are sorted by fiscal year, both in descending order (newest first).
     * The resulting list is suitable for JavaFX TableView binding.
     *
     * @param people the list of {@link Person} objects to convert, typically deserialized from JSON
     * @return an {@link ObservableList} of {@link PersonFx} objects with sorted nested collections,
     *         or an empty list if the input is null or empty
     * @throws NullPointerException if any {@link Person} or nested collection element is null (filtered out internally)
     * @throws NumberFormatException if awardYear or fiscalYear strings cannot be parsed to integers (handled internally)
     */
    public static ObservableList<PersonFx> copyPeople(List<Person> people) {
        if (people == null || people.isEmpty()) {
            return FXCollections.observableArrayList();
        }
        return FXCollections.observableArrayList(people.stream().filter(Objects::nonNull)
                        .map(person -> {
                            PersonFx personDTOFx = new PersonFx(person);
                            // Convert and add phones
                            if (person.getPhones() != null) {
                                personDTOFx.getPhones().addAll(person.getPhones().stream()
                                        .filter(Objects::nonNull)
                                        .map(PhoneFx::new)
                                        .collect(Collectors.toList())
                                );
                            }
                            // Convert and add emails
                            if (person.getEmails() != null) {
                                personDTOFx.getEmail().addAll(person.getEmails().stream()
                                        .filter(Objects::nonNull)
                                        .map(EmailDTOFx::new)
                                        .collect(Collectors.toList())
                                );
                            }
                            // Convert, sort, and add awards by awardYear (String, treated as integer, newest first)
                            if (person.getAwards() != null) {
                                personDTOFx.getAwards().addAll(person.getAwards().stream()
                                        .filter(Objects::nonNull)
                                        .map(AwardDTOFx::new)
                                        .sorted((a, b) -> {
                                            try {
                                                int yearA = Integer.parseInt(a.getAwardYear()); // TODO may be better to change this field to integer
                                                int yearB = Integer.parseInt(b.getAwardYear());
                                                return Integer.compare(yearB, yearA); // Descending order
                                            } catch (NumberFormatException e) {
                                                // Handle invalid years (e.g., non-numeric strings)
                                                return 0; // Treat as equal, or customize as needed
                                            }
                                        })
                                        .collect(Collectors.toList())
                                );
                            }
                            // Convert, sort, and add officers by fiscalYear (Integer, newest first)
                            if (person.getOfficers() != null) {
                                personDTOFx.getOfficers().addAll(
                                        person.getOfficers().stream()
                                                .filter(Objects::nonNull)
                                                .map(OfficerFx::new)
                                                .sorted((a, b) -> Integer.compare(b.getFiscalYear(), a.getFiscalYear()))
                                                .collect(Collectors.toList())
                                );
                            }
                            return personDTOFx;
                        }).collect(Collectors.toList())
        );
    }

    /**
     * Converts a list of {@link MembershipId} objects to an {@link ObservableList} of
     * {@link MembershipIdDTOFx} objects, sorted by fiscal year in descending order (newest first).
     * The resulting list is suitable for JavaFX TableView binding.
     *
     * @param membershipIds the list of {@link MembershipId} objects to convert, typically deserialized from JSON
     * @return an {@link ObservableList} of {@link MembershipIdDTOFx} objects, sorted by fiscal year,
     *         or an empty list if the input is null or empty
     * @throws NullPointerException if any {@link MembershipId} in the input list is null (filtered out internally)
     */
    public static ObservableList<MembershipIdDTOFx> copyMembershipIds(List<MembershipId> membershipIds) {
        if (membershipIds == null || membershipIds.isEmpty()) {
            return FXCollections.observableArrayList();
        }
        return FXCollections.observableArrayList(membershipIds.stream()
                .filter(Objects::nonNull)
                .map(MembershipIdDTOFx::new)
                .sorted((a, b) -> Integer.compare(b.getFiscalYear(), a.getFiscalYear())) // Descending order
                .collect(Collectors.toList())
        );
    }

    /**
     * Converts a list of {@link Invoice} objects to an {@link ObservableList} of {@link InvoiceDTOFx}
     * objects, sorted by year in descending order (newest first). The resulting list is suitable for
     * JavaFX TableView binding.
     *
     * @param invoices the list of {@link Invoice} objects to convert, typically deserialized from JSON
     * @return an {@link ObservableList} of {@link InvoiceDTOFx} objects, sorted by year,
     *         or an empty list if the input is null or empty
     * @throws NullPointerException if any {@link Invoice} in the input list is null (filtered out internally)
     */
    public static ObservableList<InvoiceDTOFx> copyInvoices(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            return FXCollections.observableArrayList();
        }
        return FXCollections.observableArrayList(
                invoices.stream()
                        .filter(Objects::nonNull)
                        .map(InvoiceDTOFx::new)
                        .sorted((a, b) -> Integer.compare(b.getYear(), a.getYear())) // Descending order
                        .collect(Collectors.toList())
        );
    }

    /**
     * Converts a list of {@link Boat} objects to an {@link ObservableList} of {@link BoatDTOFx}
     * objects. The resulting list is suitable for JavaFX TableView binding and maintains the input order
     * without sorting.
     *
     * @param boats the list of {@link Boat} objects to convert, typically deserialized from JSON
     * @return an {@link ObservableList} of {@link BoatDTOFx} objects, or an empty list if the input is null or empty
     * @throws NullPointerException if any {@link Boat} in the input list is null (filtered out internally)
     */
    public static ObservableList<BoatDTOFx> copyBoats(List<Boat> boats) {
        if (boats == null || boats.isEmpty()) {
            return FXCollections.observableArrayList();
        }
        return FXCollections.observableArrayList(
                boats.stream()
                        .filter(Objects::nonNull)
                        .map(BoatDTOFx::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Converts a list of {@link Note} objects to an {@link ObservableList} of {@link NotesDTOFx}
     * objects, sorted by memo date in descending order (newest first). The resulting list is suitable
     * for JavaFX TableView binding.
     *
     * @param memos the list of {@link Note} objects to convert, typically deserialized from JSON
     * @return an {@link ObservableList} of {@link NotesDTOFx} objects, sorted by memo date,
     *         or an empty list if the input is null or empty
     * @throws NullPointerException if any {@link Note} or its memo date in the input list is null
     *         (filtered out internally)
     */
    public static ObservableList<NotesDTOFx> copyNotes(List<Note> memos) {
        if (memos == null || memos.isEmpty()) {
            return FXCollections.observableArrayList();
        }
        return FXCollections.observableArrayList(
                memos.stream()
                        .filter(Objects::nonNull)
                        .map(NotesDTOFx::new)
                        .filter(note -> note.getMemoDate() != null) // Ensure memoDate is not null
                        .sorted((a, b) -> b.getMemoDate().compareTo(a.getMemoDate())) // Descending order
                        .collect(Collectors.toList())
        );
    }
}
