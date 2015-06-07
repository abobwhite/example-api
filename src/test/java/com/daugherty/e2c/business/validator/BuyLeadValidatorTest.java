package com.daugherty.e2c.business.validator;

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
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.BuyLeadReadDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;

@RunWith(MockitoJUnitRunner.class)
public class BuyLeadValidatorTest {

    @Mock
    private BuyLead buyLead;
    @Mock
    private Party party;
    @Mock
    private ProductCategory productCategory;
    @Mock
    private BuyLeadReadDao buyLeadReadDao;
    @Mock
    private JdbcPartyDao partyDao;

    @InjectMocks
    private final BuyLeadValidator validator = new BuyLeadValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void throwsValidationExceptionWhenBuyLeadExistsValidationFailed() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("buyLead", Validatable.BUY_LEAD_ALREADY_EXISTS));
        
        when(buyLead.getId()).thenReturn(null);
        when(party.getId()).thenReturn(1L);
        when(productCategory.getId()).thenReturn(10L);
        when(partyDao.loadById(1L)).thenReturn(party);
        when(buyLead.getRequester()).thenReturn(party);
        when(buyLead.getProductCategory()).thenReturn(productCategory);
        when(buyLead.validate()).thenReturn(new ValidationError());
        when(buyLeadReadDao.findByPartyAndCategory(1L, 10L)).thenReturn(buyLead);

        validator.validate(buyLead);
    }

}
