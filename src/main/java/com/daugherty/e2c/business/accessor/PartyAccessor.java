package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.PartyFilter;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Implementation for Party Accessor
 */
@Service("partyAccessor")
public class PartyAccessor extends BaseAccessor<Party> {
    @Inject
    private JdbcPartyDao partyDao;

    @Override
    public List<Party> find(Filter<Party> filter) {

        QueryCriteria criteria = partyDao.createQueryCriteria(filter.getStringCriterion(PartyFilter.USERNAME_PREFIX),
                filter.getStringCriterion(PartyFilter.PARTY_TYPE), filter.getSortBy(), filter.isSortDescending(),
                filter.getStartItem(), filter.getCount(), filter.getLocale());
        return partyDao.getAll(criteria);
    }

    @Override
    public Party load(Long id, Locale locale) {
        return partyDao.loadById(id);
    }

}
