package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.MessageCollectionHydrator;
import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.persistence.data.BuyLeadMessageReadDao;
import com.google.common.collect.Lists;

/**
 * Accessor for Buy Lead Message domain objects.
 */
@Service("buyLeadMessageAccessor")
public class BuyLeadMessageAccessor extends BaseAccessor<BuyLeadMessage> {

    @Inject
    private BuyLeadMessageReadDao buyLeadMessageReadDao;
    @Inject
    private MessageCollectionHydrator messageCollectionHydrator;

    @Override
    public List<BuyLeadMessage> find(Filter<BuyLeadMessage> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BuyLeadMessage load(Long id, Locale locale) {
        BuyLeadMessage message = buyLeadMessageReadDao.load(id);

        messageCollectionHydrator.hydrate(message);

        return message;
    }
}
