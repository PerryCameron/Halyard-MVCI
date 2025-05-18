package org.ecsail.static_tools;

import org.ecsail.dto.*;
import org.ecsail.pojo.*;

import java.util.ArrayList;
import java.util.List;

public class CopyPOJOtoFx {
    // unfortunately to use jackson to deserialize the object must be a POJO, for JFX it must be converted.
    public static List<RosterDTOFx> copyRoster(List<RosterDTO> roster) {
        List<RosterDTOFx> rosterFxList = new ArrayList<>();
        for (RosterDTO rosterDTO : roster) {
            rosterFxList.add(new RosterDTOFx(rosterDTO));
        }
        return rosterFxList;
    }

    public static List<PersonDTOFx> copyPeople(List<Person> people) {
        List<PersonDTOFx> peopleFxList = new ArrayList<>();
        for (Person person : people) {
            PersonDTOFx personDTOFx = new PersonDTOFx(person);
            if(person.getPhones() != null) {
                for (Phone phone : person.getPhones()) {
                    personDTOFx.getPhones().add(new PhoneDTOFx(phone));
                }
            }
            if(person.getEmails() != null) {
                for (Email email : person.getEmails()) {
                    personDTOFx.getEmail().add(new EmailDTOFx(email));
                }
            }
            if (person.getAwards() != null) {
                for (Award award : person.getAwards()) {
                    personDTOFx.getAwards().add(new AwardDTOFx(award));
                }
            }
            if (person.getOfficers() != null) {
                for (Officer officer : person.getOfficers()) {
                    personDTOFx.getOfficers().add(new OfficerDTOFx(officer));
                }
            }
            peopleFxList.add(personDTOFx);
        }
        return peopleFxList;
    }
}
