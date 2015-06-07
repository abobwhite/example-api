package com.daugherty.e2c.business.validator;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.persistence.data.MembershipReadDao;

@RunWith(MockitoJUnitRunner.class)
public class MessageValidatorTest {

    private static final MembershipLevel MEMBERSHIP_LEVEL_GOOD = new MembershipLevel(3L, 3, null, 12, null, null, null,
            null, null, null, true, null, null, null, null, null, true, false, false, false);
    private static final MembershipLevel MEMBERSHIP_LEVEL_BAD = new MembershipLevel(2L, 2, null, 12, null, null, null,
            null, null, null, false, null, null, null, null, null, true, false, false, false);

    @Mock
    private MembershipReadDao membershipReadDao;
    @Mock
    private MembershipLevel membershipLevel;

    @InjectMocks
    private final MessageValidator validator = new MessageValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void throwValidationExceptionWhenMessageValidationFailed() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Message.OTHER_PARTY_SERIAL_PROPERTY,
                Validatable.MESSAGE_ORIGINATOR_REQUIRED));

        validator.validate(new Message(42L));
    }

    @Test
    public void doNotThrowValidationExceptionWhenMessageOriginatorIsABuyer() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party originator = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        validator.validate(new ProductMessage("subject", originator, 242L, null));

        verifyZeroInteractions(membershipReadDao);
    }

    @Test
    public void doNotThrowValidationExceptionWhenMessageOriginatorIsASupplierWithSupplierMessagingEnabled() {
        Company company = new Company("supplier", null, null, null, null, null, null, null, null, null, null);
        Party originator = new Party(666L, "pBVQwo0b", null, company, null, PartyType.SUPPLIER, null, null);

        when(membershipReadDao.loadBySupplierId(originator.getId())).thenReturn(
                new Membership(originator.getId(), MEMBERSHIP_LEVEL_GOOD));

        validator.validate(new ProductMessage("subject", originator, 242L, null));
    }

    @Test
    public void throwValidationExceptionWhenMessageOriginatorIsASupplierWithoutSupplierMessagingEnabled() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Message.OTHER_PARTY_SERIAL_PROPERTY,
                Validatable.MESSAGE_ORIGINATOR_NEEDS_UPGRADE));

        Company company = new Company("supplier", null, null, null, null, null, null, null, null, null, null);
        Party originator = new Party(666L, "pBVQwo0b", null, company, null, PartyType.SUPPLIER, null, null);

        when(membershipReadDao.loadBySupplierId(originator.getId())).thenReturn(
                new Membership(originator.getId(), MEMBERSHIP_LEVEL_BAD));

        validator.validate(new ProductMessage("subject", originator, 242L, null));
    }

}
