package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.UserFilter;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.persistence.data.UserReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UserAccessorTest {

    private Contact contact = new Contact("first", "last", "country", null, "email@address.net", null, null, new Date());
    private Company company = new Company("company name", null, null, null, null, null, null, null, null, null, null);
    private Party party = new Party(586L, "jKNzKB04", contact, company, null, PartyType.BUYER, null, null);
    private User user = new User(23L, "username", "password", Lists.newArrayList("role"), 0, true, false, true, party,
            true, new Date());
    private List<User> users = Lists.newArrayList(user);

    @Mock
    private UserReadDao userReadDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final UserAccessor userAccessor = new UserAccessor();

    @Test(expected = UnsupportedOperationException.class)
    public void findWithoutFilterCriteriaThrowsException() throws Exception {
        UserFilter emptyFilter = new UserFilter(null, null, null, null, null, null, null, null, null, null, null, null,
                null);
        when(userReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, 250, Locale.ENGLISH)).thenReturn(
                queryCriteria);

        try {
            userAccessor.find(emptyFilter);
        } catch (UnsupportedOperationException e) {
            assertThat(e.getMessage(), is("ListableUserAccessor expects query criteria to have a role specified"));
            throw e;
        }
    }

    @Test
    public void loadCallsDaoFindMethod() throws Exception {
        when(userReadDao.find(23L)).thenReturn(user);

        User user = userAccessor.load(23L, Locale.ENGLISH);

        assertThat(user, is(user));
    }

    @Test
    public void findWithFilterCriteriaCallsDaoFindMethod() throws Exception {
        when(
                userReadDao.createQueryCriteria("prefix", "first", "last", "email", "company name",
                        ApprovalStatus.PENDING_APPROVAL.getName(), "role", true, null, null, "username", true, 26, 50))
                .thenReturn(queryCriteria);
        when(userReadDao.find(queryCriteria)).thenReturn(users);

        UserFilter filter = new UserFilter("prefix", "first", "last", "email", "company name",
                ApprovalStatus.PENDING_APPROVAL, "role", true, null, "username", true, 26, 50);
        List<User> approvals = userAccessor.find(filter);

        assertThat(approvals, is(users));
    }

    @Test
    public void findWithUnprofiledApprovalStatusCallsDaoFindPendingUsersMethod() throws Exception {
        when(
                userReadDao.createQueryCriteria("prefix", "first", "last", "email", "company name",
                        ApprovalStatus.UNPROFILED.getName(), "role", true, null, null, "username", true, 26, 50))
                .thenReturn(queryCriteria);
        when(userReadDao.find(queryCriteria)).thenReturn(users);
        UserFilter filter = new UserFilter("prefix", "first", "last", "email", "company name",
                ApprovalStatus.UNPROFILED, "role", true, null, "username", true, 26, 50);

        userAccessor.find(filter);

        verify(userReadDao).createQueryCriteria("prefix", "first", "last", "email", "company name",
                ApprovalStatus.UNPROFILED.getName(), "role", true, null, null, "username", true, 26, 50);
        verify(userReadDao).findPendingUsers(queryCriteria);
        verifyNoMoreInteractions(userReadDao);
    }
}
