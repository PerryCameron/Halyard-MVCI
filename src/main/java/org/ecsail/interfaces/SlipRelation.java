package org.ecsail.interfaces;

public interface SlipRelation {
    enum slip {
        owner,
        subLeaser,
        ownAndSublease,
        noSlip
    }
}
