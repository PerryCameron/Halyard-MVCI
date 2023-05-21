package org.ecsail.interfaces;

public interface SlipUser {
    enum slip {
        owner,
        subLeaser,
        ownAndSublease,
        noSlip
    }
}
