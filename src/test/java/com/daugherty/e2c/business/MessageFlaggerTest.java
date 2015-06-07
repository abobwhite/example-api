package com.daugherty.e2c.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.MessageWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class MessageFlaggerTest {

	@Mock
	private MessageWriteDao messageDao;
	@Mock
	private Hashids hashids;

	@InjectMocks
	private final MessageFlagger flagger = new MessageFlagger();

	@Test
	public void flagMessageForPartyDelegatesToDao() {
	    when(hashids.decode("pBVQwo0b")).thenReturn(new long[]{666L});
	    when(hashids.decode("jKNz4P4q")).thenReturn(new long[]{42L});
		flagger.flagMessageForParty("jKNz4P4q", "pBVQwo0b");

		verify(messageDao).insertMessageFlag(42L, 666L);
	}

	@Test
	public void unflagMessageForPartyDelegatesToDao() {
	    when(hashids.decode("pBVQwo0b")).thenReturn(new long[]{666L});
	    when(hashids.decode("jKNz4P4q")).thenReturn(new long[]{42L});
		flagger.unflagMessageForParty("jKNz4P4q", "pBVQwo0b");

		verify(messageDao).deleteMessageFlag(42L, 666L);
	}

}
