package org.ecsail.enums;



import javafx.collections.ObservableList;
import org.ecsail.dto.BoardPositionDTO;

import java.util.ArrayList;

/**
 * this used to be an ENUM but changed it to a class and pull info from
 * the database and kept it acting somewhat like an enum. This gives the
 * versatility to add, delete, and edit positions, and also allowed for
 * clearer code by enabling more columns of information
 */

public class Officer {
    /**
     * This is the most appropriate location to create an Array list of board positions
     * @return ArrayList<BoardPositionDTO>
     */
    public static ArrayList<BoardPositionDTO> getPositionList() {
        return null; // TODO see what I was doing here
//        return SqlBoardPositions.getPositions();
    }

    /**
     * Will return the full name of the position, when give a two character code.
     * This feature is for reducing database storage size needed
     * @param officerCode - two character code
     * @return String - full name of position
     */
    public static String getByCode(String officerCode, ObservableList<BoardPositionDTO> bp) {
        return bp.stream()
                .filter(p -> p.identifier().equals(officerCode))
                .map(p -> p.position()).findFirst().orElse("not found");
    }

    /**
     * Well return a two character code when given the full name of a position
     * @param name String the full name of a position
     * @return String the two character code that represents given position name
     */
    public static String getByName(String name, ObservableList<BoardPositionDTO> bp) {
        return bp.stream()
                .filter(p -> p.position().equals(name))
                .map(p -> p.identifier()).findFirst().orElse("not found");
    }

}
