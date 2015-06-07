package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.UserFilter;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.persistence.data.UserReadDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Accessor for User domain objects.
 */
@Service("userAccessor")
public class UserAccessor extends BaseAccessor<User> {

    private static final String NO_CRITERIA_MESSAGE = "ListableUserAccessor expects query criteria to have a role specified";

    @Inject
    private UserReadDao userReadDao;

    @Override
    public List<User> find(Filter<User> filter) {
        if (filter.hasNoCriteria()) {
            throw new UnsupportedOperationException(NO_CRITERIA_MESSAGE);
        } else {
            String approvalStatus = filter.getStringCriterion(UserFilter.APPROVAL_STATUS);
            QueryCriteria criteria = userReadDao.createQueryCriteria(
                    filter.getStringCriterion(UserFilter.USERNAME_PREFIX),
                    filter.getStringCriterion(UserFilter.FIRST_NAME_PREFIX),
                    filter.getStringCriterion(UserFilter.LAST_NAME_PREFIX),
                    filter.getStringCriterion(UserFilter.EMAIL_PREFIX),
                    filter.getStringCriterion(UserFilter.COMPANY_NAME_PREFIX), approvalStatus,
                    filter.getStringCriterion(UserFilter.ROLE), filter.getBooleanCriterion(UserFilter.PRESENCE),
                    filter.getBooleanCriterion(User.LOCKED_SERIAL_PROPERTY),
                    filter.getBooleanCriterion(User.BLOCKED_SERIAL_PROPERTY), filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount());

            if (approvalStatus != null && approvalStatus.equals("Unprofiled")) {
                criteria.getParameterMap().remove(UserFilter.ROLE);
                return userReadDao.findPendingUsers(criteria);
            } else {
                return userReadDao.find(criteria);
            }
        }
    }

    @Override
    public User load(Long id, Locale locale) {
        return userReadDao.find(id);
    }
}
