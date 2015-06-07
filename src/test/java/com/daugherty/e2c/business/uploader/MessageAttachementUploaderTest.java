package com.daugherty.e2c.business.uploader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageAttachment;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.persistence.data.MessageAttachmentReadDao;
import com.daugherty.e2c.persistence.data.MessageAttachmentWriteDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcMessageDao;
import com.daugherty.e2c.persistence.document.DocumentDao;
import com.daugherty.e2c.persistence.document.DocumentType;
import com.google.common.collect.Lists;

;
@RunWith(MockitoJUnitRunner.class)
public class MessageAttachementUploaderTest {

    @Mock
    private DocumentDao documentDao;
    @Mock
    private InputStream inputStream;
    @Mock
    private JdbcMessageDao messageDao;
    @Mock
    private MessageAttachmentReadDao messageAttachmentReadDao;
    @Mock
    private MessageAttachmentWriteDao messageAttachmentWriteDao;

    @InjectMocks
    private MessageAttachementUploader attachementUploader = new MessageAttachementUploader();

    @Captor
    private ArgumentCaptor<MessageAttachment> messageAttachmentCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Message message;

    @Before
    public void setUpFixtures() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        message = new ProductMessage(42L, "jKNz4P4q", "subject", buyer, false, new Date(), false);
    }

    @Test
    public void uploadDocumentReturnsURL() throws IOException {
        Long id = 42L;
        String documentName = "documentName";
        String documentKey = DocumentType.MESSAGE_ATTACHEMNT.getType() + id;
        String contentType = "contentType";

        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(messageDao.load(id)).thenReturn(message);
        when(messageAttachmentReadDao.loadByMessageIds(Lists.newArrayList(42L))).thenReturn(
                new ArrayList<MessageAttachment>());
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        String uploadedDocumentKey = attachementUploader.uploadDocument(id, documentName, contentType, inputStream);

        verify(messageAttachmentWriteDao).insertAttachment(messageAttachmentCaptor.capture());

        MessageAttachment messageAttachement = messageAttachmentCaptor.getValue();

        assertThat(uploadedDocumentKey, is(documentKey));
        assertThat(messageAttachement.getMessageId(), is(42L));
        assertThat(messageAttachement.getAttachmentName(), is(documentName));
        assertThat(messageAttachement.getAttachmentLink(), is(documentKey));
    }

    @Test
    public void uploadDocumentReturnsValidationErrorWhenExceedsMaxAttachments() throws IOException {
        expectedException.expect(ValidationException.class);

        Long id = 42L;
        String documentName = "documentName";
        String documentKey = DocumentType.PRODUCT_IMAGE.getType() + id;
        String contentType = "contentType";

        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(messageDao.load(id)).thenReturn(message);
        when(messageAttachmentReadDao.loadByMessageIds(Lists.newArrayList(42L))).thenReturn(
                Lists.newArrayList(new MessageAttachment("1", "1"), new MessageAttachment("2", "2"),
                        new MessageAttachment("3", "3")));

        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        attachementUploader.uploadDocument(id, documentName, contentType, inputStream);

        verifyZeroInteractions(messageAttachmentWriteDao);
    }
}
