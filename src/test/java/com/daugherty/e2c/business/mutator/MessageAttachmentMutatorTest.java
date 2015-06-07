package com.daugherty.e2c.business.mutator;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.MessageAttachment;
import com.daugherty.e2c.persistence.data.MessageAttachmentReadDao;
import com.daugherty.e2c.persistence.data.MessageAttachmentWriteDao;
import com.daugherty.e2c.persistence.document.DocumentDao;

@RunWith(MockitoJUnitRunner.class)
public class MessageAttachmentMutatorTest {
    @Mock
    private MessageAttachmentReadDao messageAttachmentReadDao;
    @Mock
    private MessageAttachmentWriteDao messageAttachmentWriteDao;
    @Mock
    private DocumentDao documentDao;

    @InjectMocks
    private MessageAttachmentMutator attachmentMutator = new MessageAttachmentMutator();

    @Test
    public void deleteDelgatesToDaos() {
        MessageAttachment messageAttachement = new MessageAttachment(1L, 2L, "name", "link");

        when(messageAttachmentReadDao.load(1L)).thenReturn(messageAttachement);

        attachmentMutator.delete(1L);

        verify(documentDao).delete("link");
        verify(messageAttachmentWriteDao).deleteAttachement(1L);
    }

}
