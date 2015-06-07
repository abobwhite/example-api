package com.daugherty.e2c.business;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.MessageWriteDao;

/**
 * Toggles Message flags for specific user.
 */
@Service
@Transactional
public class MessageFlagger {

	@Inject
	private MessageWriteDao messageDao;
	@Inject
	private Hashids hashids;

	public void flagMessageForParty(String publicMessageId, String publicPartyId) {
	    Long partyId = hashids.decode(publicPartyId)[0];
	    Long messageId = hashids.decode(publicMessageId)[0];
		messageDao.insertMessageFlag(messageId, partyId);
	}

	public void unflagMessageForParty(String publicMessageId, String publicPartyId) {
	    Long partyId = hashids.decode(publicPartyId)[0];
	    Long messageId = hashids.decode(publicMessageId)[0];
		messageDao.deleteMessageFlag(messageId, partyId);
	}

}
