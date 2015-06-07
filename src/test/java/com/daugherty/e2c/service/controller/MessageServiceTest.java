package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.Principal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.jasypt.util.numeric.IntegerNumberEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.MessageFlagger;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.MessageAccessor;
import com.daugherty.e2c.business.accessor.filter.InteractionFilter;
import com.daugherty.e2c.business.accessor.filter.MessageFilter;
import com.daugherty.e2c.business.mapper.InteractionMapper;
import com.daugherty.e2c.business.uploader.MessageAttachementUploader;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageSummary;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonInteraction;
import com.daugherty.e2c.service.json.JsonInteractions;
import com.daugherty.e2c.service.json.JsonMessageSummary;
import com.daugherty.e2c.service.json.JsonMessages;
import com.daugherty.e2c.service.json.JsonParty;
import com.daugherty.e2c.service.json.JsonPublicId;
import com.daugherty.e2c.service.json.JsonSupplier;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    private static final Company BUYER_COMPANY = new Company("East India Tea Company", null, null, null, null, null,
            null, null, null, null, null);
    private static final Company SUPPLIER_COMPANY = new Company("Megadodo Publications", null, null, null, null, null,
            null, null, null, null, null);
    private static final Party BUYER = new Party(586L, "jKNzKB04", null, BUYER_COMPANY, ApprovalStatus.APPROVED,
            PartyType.BUYER, null, null);
    private static final Party SUPPLIER = new Party(666L, "pBVQwo0b", null, SUPPLIER_COMPANY, ApprovalStatus.APPROVED,
            PartyType.SUPPLIER, null, null);

    private static final Message FLAGGED_MESSAGE = new ProductMessage(1L, "vXP9l0Or", "flagged", BUYER, true,
            new Date(), false);
    private static final Message UNFLAGGED_MESSAGE = new ProductMessage(2L, "GpP8xPem", "unflagged", SUPPLIER, false,
            new Date(), false);
    private static final Interaction INITIAL_INQUIRY = new Interaction(new Message(42L), "initial", BUYER, SUPPLIER);
    private static final Interaction REPLY = new Interaction(new Message(42L), "reply", SUPPLIER, BUYER);

    @Mock
    private MessageAccessor messageAccessor;
    @Mock
    private Accessor<MessageSummary> messageSummaryAccessor;
    @Mock
    private MessageFlagger messageFlagger;
    @Mock
    private Accessor<Interaction> interactionAccessor;
    @Mock
    private Mutator<Interaction> interactionMutator;
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private IntegerNumberEncryptor entityIdNumberEncryptor;
    @Mock
    private Principal principal;
    @Mock
    private InteractionMapper interactionMapper;
    @Mock
    private MessageAttachementUploader messageAttachementUploader;
    @Mock
    private MultipartHttpServletRequest multipartHttpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private final MessageService service = new MessageService();

    @Captor
    private ArgumentCaptor<Filter<Message>> messageFilterCaptor;
    @Captor
    private ArgumentCaptor<Filter<Interaction>> interactionFilterCaptor;

    @Before
    public void setUp() {
        Iterator<String> filenames = mock(Iterator.class);
        when(filenames.next()).thenReturn("filename");
        when(multipartHttpServletRequest.getFileNames()).thenReturn(filenames);
        when(multipartHttpServletRequest.getFile("filename")).thenReturn(multipartFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveMessagesForReceiverDelegatesToMessageAccessor() throws Exception {
        when(messageAccessor.find(any(Filter.class)))
                .thenReturn(Lists.newArrayList(FLAGGED_MESSAGE, UNFLAGGED_MESSAGE));

        JsonMessages jsonMessages = service.retrieveMessages("jKNz4P4q", null, null, "lastInteractionDate",
                Boolean.TRUE, 26, 50, Locale.ENGLISH);

        verify(messageAccessor).find(messageFilterCaptor.capture());
        Filter<Message> filter = messageFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(MessageFilter.class)));
        assertThat(filter.getStringCriterion(MessageFilter.RECEIVER_ID), is("jKNz4P4q"));
        assertThat(filter.getBooleanCriterion(MessageFilter.FLAGGED), is(nullValue()));
        assertThat(filter.getStringCriterion(MessageFilter.SENDER_ID), is(nullValue()));
        assertThat(filter.getSortBy(), is("lastInteractionDate"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonMessages, is(notNullValue()));
        assertThat(jsonMessages.size(), is(2));
        assertThat(jsonMessages.get(0).getId(), is(FLAGGED_MESSAGE.getPublicId()));
        assertThat(jsonMessages.get(1).getId(), is(UNFLAGGED_MESSAGE.getPublicId()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveFlaggedMessagesForReceiverDelegatesToMessageAccessor() throws Exception {
        when(messageAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(FLAGGED_MESSAGE));

        JsonMessages jsonMessages = service.retrieveMessages("jKNz4P4q", null, true, "lastInteractionDate",
                Boolean.TRUE, 26, 50, Locale.ENGLISH);

        verify(messageAccessor).find(messageFilterCaptor.capture());
        Filter<Message> filter = messageFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(MessageFilter.class)));
        assertThat(filter.getStringCriterion(MessageFilter.RECEIVER_ID), is("jKNz4P4q"));
        assertThat(filter.getBooleanCriterion(MessageFilter.FLAGGED), is(true));
        assertThat(filter.getStringCriterion(MessageFilter.SENDER_ID), is(nullValue()));
        assertThat(filter.getSortBy(), is("lastInteractionDate"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonMessages, is(notNullValue()));
        assertThat(jsonMessages.size(), is(1));
        assertThat(jsonMessages.get(0).getId(), is(FLAGGED_MESSAGE.getPublicId()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveMessagesForSenderDelegatesToMessageAccessor() throws Exception {
        when(messageAccessor.find(any(Filter.class)))
                .thenReturn(Lists.newArrayList(FLAGGED_MESSAGE, UNFLAGGED_MESSAGE));

        JsonMessages jsonMessages = service.retrieveMessages(null, "jKNz4P4q", null, "lastInteractionDate",
                Boolean.TRUE, 26, 50, Locale.ENGLISH);

        verify(messageAccessor).find(messageFilterCaptor.capture());
        Filter<Message> filter = messageFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(MessageFilter.class)));
        assertThat(filter.getStringCriterion(MessageFilter.RECEIVER_ID), is(nullValue()));
        assertThat(filter.getBooleanCriterion(MessageFilter.FLAGGED), is(nullValue()));
        assertThat(filter.getStringCriterion(MessageFilter.SENDER_ID), is("jKNz4P4q"));
        assertThat(filter.getSortBy(), is("lastInteractionDate"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonMessages, is(notNullValue()));
        assertThat(jsonMessages.size(), is(2));
        assertThat(jsonMessages.get(0).getId(), is(FLAGGED_MESSAGE.getPublicId()));
        assertThat(jsonMessages.get(1).getId(), is(UNFLAGGED_MESSAGE.getPublicId()));
    }

    @Test
    public void retrieveBuyerMessageSummary() {
        MessageSummary messageSummary = new MessageSummary(42L, "jKNz4P4q", 5, 10);
        when(messageSummaryAccessor.load(messageSummary.getPublicId(), Locale.ENGLISH)).thenReturn(messageSummary);

        JsonMessageSummary jsonMessageSummary = service.retrieveBuyerMessageSummary(messageSummary.getPublicId());

        verify(messageSummaryAccessor).load(messageSummary.getPublicId(), Locale.ENGLISH);

        assertThat(jsonMessageSummary.getId(), is(messageSummary.getId()));
        assertThat(jsonMessageSummary.getUnread(), is(messageSummary.getUnread()));
        assertThat(jsonMessageSummary.getSentMessageCount(), is(messageSummary.getSentMessageCount()));
    }

    @Test
    public void retrieveSupplierMessageSummary() {
        MessageSummary messageSummary = new MessageSummary(42L, "jKNz4P4q", 5, 10);
        when(messageSummaryAccessor.load(messageSummary.getPublicId(), Locale.ENGLISH)).thenReturn(messageSummary);

        JsonMessageSummary jsonMessageSummary = service.retrieveSupplierMessageSummary(messageSummary.getPublicId());

        verify(messageSummaryAccessor).load(messageSummary.getPublicId(), Locale.ENGLISH);

        assertThat(jsonMessageSummary.getId(), is(messageSummary.getId()));
        assertThat(jsonMessageSummary.getUnread(), is(messageSummary.getUnread()));
        assertThat(jsonMessageSummary.getSentMessageCount(), is(messageSummary.getSentMessageCount()));
    }

    @Test
    public void flagMessageDelegateToMessageFlagger() throws Exception {
        service.flagMessage(UNFLAGGED_MESSAGE.getPublicId(), new JsonPublicId("pBVQwo0b"));

        verify(messageFlagger).flagMessageForParty(UNFLAGGED_MESSAGE.getPublicId(), "pBVQwo0b");
    }

    @Test
    public void unflagMessageDelegateToMessageFlagger() throws Exception {
        service.unflagMessage(FLAGGED_MESSAGE.getPublicId(), "jKNzKB04");

        verify(messageFlagger).unflagMessageForParty(FLAGGED_MESSAGE.getPublicId(), "jKNzKB04");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveInteractionsForMessageDelegatesToInteractionAccessor() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(1L));
        when(interactionAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(INITIAL_INQUIRY, REPLY));

        JsonInteractions jsonInteractions = service.retrieveInteractions("jKNz4P4q", null, "sentTime", Boolean.TRUE,
                26, 50, principal, Locale.ENGLISH);

        verify(interactionAccessor).find(interactionFilterCaptor.capture());
        Filter<Interaction> filter = interactionFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(InteractionFilter.class)));
        assertThat(filter.getStringCriterion(InteractionFilter.PUBLIC_MESSAGE_ID), is("jKNz4P4q"));
        assertThat(filter.getSortBy(), is("sentTime"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonInteractions, is(notNullValue()));
        assertThat(jsonInteractions.size(), is(2));
        assertThat(jsonInteractions.get(0).getId(), is(INITIAL_INQUIRY.getId()));
        assertThat(jsonInteractions.get(1).getId(), is(REPLY.getId()));

    }

    @Test
    public void replyToMessageDelegatesToInteractionMutator() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(1L));
        JsonInteraction requestInteraction = new JsonInteraction();
        requestInteraction.setBody("body");
        JsonParty sender = new JsonParty();
        sender.setId("jKNzKB04");
        sender.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        sender.setPartyType(PartyType.BUYER.toString());
        requestInteraction.setSender(sender);
        JsonSupplier receiver = new JsonSupplier();
        receiver.setId("pBVQwo0b");
        receiver.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        receiver.setPartyType(PartyType.SUPPLIER.toString());
        requestInteraction.setReceiver(receiver);

        Interaction persistedInteraction = new Interaction(new Message(42L), "body", BUYER, SUPPLIER);
        persistedInteraction.setId(421L);
        when(interactionMapper.toNewDomainObject("jKNz4P4q", requestInteraction, Locale.ENGLISH)).thenReturn(
                persistedInteraction);
        when(
                interactionMutator.create(interactionMapper.toNewDomainObject("jKNz4P4q", requestInteraction,
                        Locale.ENGLISH))).thenReturn(persistedInteraction);

        JsonInteraction responseInteraction = service.replyToMessage("jKNz4P4q", null, requestInteraction,
                Locale.ENGLISH);

        assertThat(responseInteraction.getId(), is(421L));
    }

    @Test
    public void replyToMessageWithNoSenderRetrievesMessageAndThenDelegatesToInteractionMutator() throws Exception {
        JsonInteraction requestInteraction = new JsonInteraction();
        requestInteraction.setBody("body");

        JsonSupplier receiver = new JsonSupplier();
        receiver.setId("pBVQwo0b");
        receiver.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        receiver.setPartyType(PartyType.SUPPLIER.toString());
        requestInteraction.setReceiver(receiver);

        Party receiverEntity = new Party(666L, receiver.getId(), null, null, ApprovalStatus.findByName(receiver
                .getApprovalStatus()), PartyType.findByType(receiver.getPartyType()), receiver.getVersion(), null);

        Interaction requestInteractionWithSender = new Interaction(new Message(42L), "body", BUYER, receiverEntity);

        ProductMessage persistedMessage = new ProductMessage(42L, "jKNz4P4q", "subject", BUYER, false, new Date(),
                false);
        when(messageAccessor.load("jKNz4P4q", Locale.ENGLISH, "pBVQwo0b")).thenReturn(persistedMessage);

        Interaction persistedInteraction = new Interaction(new Message(42L), "body", BUYER, SUPPLIER);
        persistedInteraction.setId(421L);
        when(interactionMapper.toNewDomainObject("jKNz4P4q", requestInteraction, Locale.ENGLISH)).thenReturn(
                requestInteractionWithSender);
        when(
                interactionMutator.create(interactionMapper.toNewDomainObject("jKNz4P4q", requestInteraction,
                        Locale.ENGLISH))).thenReturn(persistedInteraction);

        when(entityIdNumberEncryptor.decrypt(BigInteger.valueOf(12345L))).thenReturn(BigInteger.valueOf(42L));

        JsonInteraction responseInteraction = service.replyToMessage("jKNz4P4q", BigInteger.valueOf(12345L),
                requestInteraction, Locale.ENGLISH);

        assertThat(responseInteraction.getId(), is(421L));
        assertThat(persistedInteraction.getSender(), is(notNullValue()));
    }

    @Test
    public void markInteractionAsReadDelegatesToInteractionMutator() throws Exception {
        Interaction readInteraction = new Interaction(421L, "body", BUYER, SUPPLIER, new Date(), false);
        when(interactionAccessor.load(421L, Locale.ENGLISH)).thenReturn(readInteraction);

        service.markInteractionAsRead(421L);

        assertThat(readInteraction.isRead(), is(true));
        verify(interactionMutator).update(readInteraction);
    }

    @Test
    public void addImageReturnJsonDocument() throws IOException {
        InputStream inputStream = mock(InputStream.class);

        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getContentType()).thenReturn("content type");
        when(messageAttachementUploader.uploadDocument("publicId", "documentName", "content type", inputStream))
                .thenReturn("document");
        when(documentUrlFactory.createDocumentUrl("document", Locale.ENGLISH)).thenReturn("documentUrl");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        service.addAttachemnt("publicId", "documentName", multipartHttpServletRequest, httpServletResponse,
                Locale.ENGLISH);

        verify(printWriter).write("{\"documentUrl\":{\"url\":\"documentUrl\"}}");
    }

    private RememberMeAuthenticationToken buildAuthenticationToken(Long partyId) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.SUPPLIER, Role.BUYER);
        E2CUser user = new E2CUser("username", "password", true, 0, authorities, new Party(partyId), true);

        return new RememberMeAuthenticationToken("e2c", user, authorities);
    }

}
