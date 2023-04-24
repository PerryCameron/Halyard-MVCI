package org.ecsail.static_calls;

import java.time.LocalDate;
import java.time.Period;

public class MathTools {

    public static int calculateAge(String dateOfBirthStr) {
        LocalDate dob = LocalDate.parse(dateOfBirthStr);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dob, currentDate);
        return period.getYears();
    }

}
