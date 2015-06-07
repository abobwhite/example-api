package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.mapper.AnonymousMapper;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.service.json.JsonAnonymous;

@RunWith(MockitoJUnitRunner.class)
public class AnonymousServiceTest {

    @Mock
    private Mutator<Anonymous> anonymousMutator;
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private AnonymousMapper anonymousMapper;

    @InjectMocks
    private final AnonymousService service = new AnonymousService();

    @Test
    public void createAnonymousDelegatesToAnonymousMutator() {
        JsonAnonymous requestAnonymous = new JsonAnonymous();
        requestAnonymous.setFirstName("first");
        requestAnonymous.setLastName("last");
        requestAnonymous.setEmail("email");
        requestAnonymous.setCountry("counrty");

        Anonymous persistedAnonymous = new Anonymous(42L, "jKNz4P4q", new Contact(requestAnonymous.getFirstName(),
                requestAnonymous.getLastName(), requestAnonymous.getCountry(), null, requestAnonymous.getEmail(), null,
                null, new Date()), null, ApprovalStatus.UNPROFILED, null, null);
        when(anonymousMapper.toNewDomainObject(requestAnonymous, Locale.ENGLISH)).thenReturn(persistedAnonymous);
        when(anonymousMutator.create(anonymousMapper.toNewDomainObject(requestAnonymous, Locale.ENGLISH))).thenReturn(persistedAnonymous);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        JsonAnonymous responseAnonymous = service.createAnonymous(requestAnonymous, request, Locale.ENGLISH);

        assertThat(responseAnonymous.getId(), is("jKNz4P4q"));
    }
}