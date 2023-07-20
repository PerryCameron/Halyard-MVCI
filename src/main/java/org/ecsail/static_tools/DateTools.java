package org.ecsail.static_tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTools {
    public static String getDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;
    }


}
