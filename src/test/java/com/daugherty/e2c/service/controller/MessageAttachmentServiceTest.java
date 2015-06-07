package com.daugherty.e2c.service.controller;

import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.daugherty.e2c.business.mutator.MessageAttachmentMutator;

@RunWith(MockitoJUnitRunner.class)
public class MessageAttachmentServiceTest {

    @Mock
    private MessageAttachmentMutator messageAttachmentMutator;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private final MessageAttachmentService service = new MessageAttachmentService();

    @Test
    public void deleteMessageAttachmentDelegateToMutator() {

        service.deleteMessageAttachment(1L, response);

        verify(messageAttachmentMutator).delete(1L);
        verify(response).setStatus(HttpStatus.NO_CONTENT.value());
    }

}
