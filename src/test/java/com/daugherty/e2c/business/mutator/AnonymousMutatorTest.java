package com.daugherty.e2c.business.mutator;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.AnonymousPartyExistsException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.persistence.data.AnonymousWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class AnonymousMutatorTest {

    @Mock
    private Validator<Anonymous> anonymousValidator;
    @Mock
    private AnonymousWriteDao anonymousWriteDao;

    @InjectMocks
    private AnonymousMutator anonymousMutator = new AnonymousMutator();

    @Test
    public void insertInsertsWhenAnonymousPartyDoesNotExist() {
        Contact contact = new Contact(null, null, "First", "Name", null, null, "email@email.com", null, null, null,
                null, null, null, null);
        Company company = new Company("English Company Name", null, null, null, null, "website@website.com", null,
                null, null, null, null);
        Anonymous anonymous = new Anonymous(contact, company);

        anonymousMutator.create(anonymous);

        verify(anonymousWriteDao).insert(anonymous);
        verify(anonymousWriteDao, never()).update(anonymous);
    }

    @Test
    public void insertUpdatesWhenAnonymousPartyExists() {
        Contact contact = new Contact(null, null, "First", "Name", null, null, "email@email.com", null, null, null,
                null, null, null, null);
        Company company = new Company("English Company Name", null, null, null, null, "website@website.com", null,
                null, null, null, null);
        Anonymous anonymous = new Anonymous(contact, company);

        doThrow(new AnonymousPartyExistsException(anonymous)).when(anonymousValidator).validate(anonymous);

        anonymousMutator.create(anonymous);

        verify(anonymousWriteDao, never()).insert(anonymous);
        verify(anonymousWriteDao).update(anonymous);
    }

}
