package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Interaction;

/**
 * Defines database change operations for Interaction domain objects.
 */
public interface InteractionWriteDao {

    Interaction insert(Interaction interaction);

    void updateReadIndicator(Interaction interaction);

    int deleteInteractionsByPartyId(Long partyId);

}
