package com.daugherty.e2c.business.validator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.BuyLeadReadDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;

/**
 * Validates the Buy Lead domain object.
 */
@Component("buyLeadValidator")
public class BuyLeadValidator implements Validator<BuyLead> {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
	
    @Inject
    private BuyLeadReadDao buyLeadReadDao;
    @Inject
    private JdbcPartyDao partyDao;

    @Override
    public void validate(BuyLead buyLead) {
        ValidationError errors = new ValidationError();
        
        errors.add(validateIsExistingParty(buyLead));
        
        if(buyLead.getId() == null) {
        	errors.add(validateIsAlreadyExistingBuyLead(buyLead.getRequester(), buyLead.getProductCategory()));
        }
        
        errors.add(buyLead.validate());

        if (errors.hasErrors()) {
            ValidationException validationException = new ValidationException(errors);
            throw validationException;
        }
    }

    private ValidationError validateIsAlreadyExistingBuyLead(Party party, ProductCategory productCategory) {
        ValidationError errors = new ValidationError();

        try {
            buyLeadReadDao.findByPartyAndCategory(party.getId(), productCategory.getId());
            errors.add("buyLead", Validatable.BUY_LEAD_ALREADY_EXISTS);
        } catch (EmptyResultDataAccessException e) {
        	LOG.info("Buy Lead does not exist.  I can create.");
        }

        return errors;
    }
    
    private ValidationError validateIsExistingParty(BuyLead buyLead) {
        ValidationError errors = new ValidationError();

        try {
            buyLead.setRequester(partyDao.loadById(buyLead.getRequester().getId()));
        } catch (EmptyResultDataAccessException e) {
            errors.add(BuyLead.PARTY_SERIAL_PROPERTY, Validatable.BUY_LEAD_PARTY_REQUIRED);
        }

        return errors;
    }
}
