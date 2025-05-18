package org.ecsail.static_tools;

import org.ecsail.dto.PersonDTOFx;
import org.ecsail.dto.RosterDTO;
import org.ecsail.dto.RosterDTOFx;
import org.ecsail.pojo.Person;

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
            peopleFxList.add(new PersonDTOFx(person));
        }
        return peopleFxList;
    }
}
