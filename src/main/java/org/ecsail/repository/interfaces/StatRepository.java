package org.ecsail.repository.interfaces;


import org.ecsail.dto.StatsDTO;

import java.util.List;

public interface StatRepository {

    List<StatsDTO> getStatistics(int startYear , int stopYear);
    List<StatsDTO> createStatDTO(int year, int statID);
    String getStatQuery(int year);
    int getNumberOfStatYears();
}
