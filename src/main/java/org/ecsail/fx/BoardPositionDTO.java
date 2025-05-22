package org.ecsail.fx;

public record BoardPositionDTO(long id, String position, String identifier, int order, boolean isOfficer, boolean isChair, boolean isAssist) { }
