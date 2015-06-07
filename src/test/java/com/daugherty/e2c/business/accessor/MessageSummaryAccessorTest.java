package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.MessageSummary;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.ProductMessageReadDao;

@RunWith(MockitoJUnitRunner.class)
public class MessageSummaryAccessorTest {

    @Mock
    private ProductMessageReadDao messageReadDao;
    
    @Mock
    private Hashids hashids;
    
    @InjectMocks
    private final MessageSummaryAccessor accessor = new MessageSummaryAccessor();

    @Test
    public void loadDelegatesToDao() {
        MessageSummary daoSummary = new MessageSummary(42L, "jKNz4P4q", 13, 26);
        when(messageReadDao.loadUnreadMessagesForParty(daoSummary.getId())).thenReturn(13);
        when(messageReadDao.loadSentMessagesForParty(daoSummary.getId())).thenReturn(26);
        when(hashids.decode("jKNz4P4q")).thenReturn(new long[]{42L});
        when(hashids.encode(42L)).thenReturn("jKNz4P4q");

        MessageSummary summary = accessor.load(daoSummary.getPublicId(), Locale.ENGLISH);

        assertThat(summary, is(daoSummary));
    }

}
