package com.daugherty.e2c.service.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.domain.MessageAttachment;
import com.daugherty.e2c.security.Role;

/**
 * REST resource for Message Attachments
 */
@Controller
public class MessageAttachmentService {
    @Inject
    private Mutator<MessageAttachment> messageAttachmentMutator;

    @RequestMapping(value = "/messageAttachments/{messageAttachmentId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER, Role.BUYER, Role.ADMIN, Role.SUPPLIER_MODERATOR })
    public void deleteMessageAttachment(@PathVariable Long messageAttachmentId, HttpServletResponse response) {

        messageAttachmentMutator.delete(messageAttachmentId);

        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
