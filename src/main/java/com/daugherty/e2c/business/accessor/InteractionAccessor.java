package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.InteractionFilter;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.persistence.data.InteractionReadDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Accessor for Interaction domain objects.
 */
@Service("interactionAccessor")
public class InteractionAccessor extends BaseAccessor<Interaction> {

    @Inject
    private InteractionReadDao interactionReadDao;

    @Override
    public List<Interaction> find(Filter<Interaction> filter) {
        QueryCriteria criteria = interactionReadDao.createQueryCriteria(
                decrypt(filter.getStringCriterion(InteractionFilter.PUBLIC_MESSAGE_ID)), filter.getSortBy(), filter.isSortDescending(),
                filter.getStartItem(), filter.getCount());
        return interactionReadDao.find(criteria);
    }

    @Override
    public Interaction load(Long id, Locale locale) {
        return interactionReadDao.load(id);
    }

}
