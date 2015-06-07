package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.UserSwitcher;
import com.daugherty.e2c.business.accessor.filter.UserFilter;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.security.PendingUserUpdater;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.security.UserConfirmer;
import com.daugherty.e2c.security.UserDeleter;
import com.daugherty.e2c.security.UserUpdater;
import com.daugherty.e2c.service.json.JsonUser;
import com.daugherty.e2c.service.json.JsonUserConfirmation;
import com.daugherty.e2c.service.json.JsonUserReset;
import com.daugherty.e2c.service.json.JsonUsers;
import com.daugherty.e2c.service.security.UserGatekeeper;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private Contact contact = new Contact("first", "last", "country", null, "email@address.net", null, null, new Date());
    private Company company = new Company("company name", null, null, null, null, null, null, null, null, null, null);
    private Party party = new Party(586L, "jKNzKB04", contact, company, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);
    private User user = new User(23L, "username", "password", Lists.newArrayList("role"), 0, true, false, true, party,
            true, new Date());

    @Mock
    private UserDeleter userDeleter;
    @Mock
    private UserUpdater userResetPasswordUpdater;
    @Mock
    private UserUpdater userChangePasswordUpdater;
    @Mock
    private UserConfirmer userConfirmer;
    @Mock
    private UserGatekeeper userGatekeeper;
    @Mock
    private UserSwitcher userSwitcher;
    @Mock
    private Accessor<User> userAccessor;
    @Mock
    private PendingUserReadDao pendingUserReadDao;
    @Mock
    private PendingUserUpdater pendingUserUpdater;
    @Mock
    private DocumentUrlFactory documentUrlFactory;

    @InjectMocks
    private UserService userService = new UserService();

    @Captor
    private ArgumentCaptor<Filter<User>> filterCaptor;

    @Test
    public void confirmUserDelegatesToUserConfirmer() throws Exception {
        JsonUserConfirmation requestConfirmation = new JsonUserConfirmation();
        requestConfirmation.setConfirmationToken("myToken");
        requestConfirmation.setRole(Role.BUYER);

        JsonUserConfirmation responseConfirmation = userService.confirmUser(requestConfirmation);

        assertThat(responseConfirmation, is(requestConfirmation));
        verify(userConfirmer).confirmUser(requestConfirmation.getConfirmationToken(),
                requestConfirmation.getUsername(), requestConfirmation.getPassword(), requestConfirmation.getRole());
    }

    @Test
    public void forgotPasswordDelgatesToUserDeleter() {
        JsonUserReset user = new JsonUserReset("username", "oldPassword", "newPassword", "newPasswordConfirmation",
                "emailAdress");
        userService.forgotPassword(user, Locale.ENGLISH);

        verify(userDeleter).deleteUser("emailAdress", Locale.ENGLISH);
    }

    @Test
    public void resetPasswordDelegatesPendingUserUpdater() {
        PendingUser pendingUser = new PendingUser(1L, "username", "password", 100L, "confirmationToken");

        when(pendingUserReadDao.loadPendingUserByPassword("oldPassword")).thenReturn(pendingUser);

        JsonUserReset user = new JsonUserReset(null, "oldPassword", "newPassword", "newPasswordConfirmation", null);
        userService.resetPassword(user, Locale.ENGLISH);

        verify(pendingUserUpdater).updatePassword("username", "oldPassword", "newPassword", "newPasswordConfirmation",
                Locale.ENGLISH);
    }

    @Test
    public void resetPasswordDelegatesResetPasswordUserUpdater() {

        when(pendingUserReadDao.loadPendingUserByPassword("oldPassword")).thenReturn(null);

        JsonUserReset user = new JsonUserReset(null, "oldPassword", "newPassword", "newPasswordConfirmation", null);
        userService.resetPassword(user, Locale.ENGLISH);

        verify(userResetPasswordUpdater).updatePassword(null, "oldPassword", "newPassword", "newPasswordConfirmation",
                Locale.ENGLISH);
    }

    @Test
    public void changePasswordDelegatesChangePasswordUserUpdater() {
        JsonUserReset user = new JsonUserReset("username", "oldPassword", "newPassword", "newPasswordConfirmation",
                null);
        userService.changePassword(user, Locale.ENGLISH);

        verify(userChangePasswordUpdater).updatePassword("username", "oldPassword", "newPassword",
                "newPasswordConfirmation", Locale.ENGLISH);
    }

    @Test
    public void unlockUserDelegatesToUserGatekeeper() {
        JsonUser user = Mockito.mock(JsonUser.class);

        when(user.getUsername()).thenReturn("username");

        userService.unlockUser(user);

        verify(userGatekeeper).unlockUser("username");
    }

    @Test
    public void blockUserDelegatesToUserGatekeeper() {
        JsonUser user = Mockito.mock(JsonUser.class);

        when(user.getUsername()).thenReturn("username");

        userService.blockUser(user);

        verify(userGatekeeper).blockUser("username");
    }

    @Test
    public void unblockUserDelegatesToUserGatekeeper() {
        JsonUser user = Mockito.mock(JsonUser.class);

        when(user.getUsername()).thenReturn("username");

        userService.unblockUser(user);

        verify(userGatekeeper).unblockUser("username");
    }

    @Test
    public void retrieveBuyerApprovalsWithQueryStringArgumentsDelegatesToAccessor() {
        when(userAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(user));

        JsonUsers jsonListableUsers = userService.retrieveUsers("user", "first", "last", "email", "company",
                "Approved", "role", Boolean.TRUE, null, "username", Boolean.FALSE, 26, 50, Locale.ENGLISH);

        verify(userAccessor).find(filterCaptor.capture());
        Filter<User> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(UserFilter.class)));
        assertThat(filter.getStringCriterion(UserFilter.USERNAME_PREFIX), is("user"));
        assertThat(filter.getStringCriterion(UserFilter.FIRST_NAME_PREFIX), is("first"));
        assertThat(filter.getStringCriterion(UserFilter.LAST_NAME_PREFIX), is("last"));
        assertThat(filter.getStringCriterion(UserFilter.EMAIL_PREFIX), is("email"));
        assertThat(filter.getStringCriterion(UserFilter.COMPANY_NAME_PREFIX), is("company"));
        assertThat(filter.getStringCriterion(UserFilter.APPROVAL_STATUS), is("Approved"));
        assertThat(filter.getStringCriterion(UserFilter.ROLE), is("role"));
        assertThat(filter.getSortBy(), is("username"));
        assertThat(filter.isSortDescending(), is(false));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonListableUsers, is(notNullValue()));
        assertThat(jsonListableUsers.size(), is(1));
        assertThatJsonListableUserHasExpectedValues(jsonListableUsers.get(0), user);

    }

    @Test
    public void retrieveUserDelegatesToAccessor() {
        when(userAccessor.load(23L, Locale.ENGLISH)).thenReturn(user);

        JsonUser jsonUser = userService.retrieveUser(23L, Locale.ENGLISH);

        assertThat(jsonUser, is(notNullValue()));
        assertThatJsonListableUserHasExpectedValues(jsonUser, user);
    }

    @Test
    public void retrieveUsersWithEmptyArgumentsDelegatesToAccessor() {
        when(userAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(user));

        JsonUsers jsonListableUsers = userService.retrieveUsers("", "", "", "", "", "", "", Boolean.TRUE, null, "",
                Boolean.FALSE, 1, 250, Locale.ENGLISH);

        verify(userAccessor).find(filterCaptor.capture());
        Filter<User> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(UserFilter.class)));
        assertThat(filter.getStringCriterion(UserFilter.USERNAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.FIRST_NAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.LAST_NAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.COMPANY_NAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.APPROVAL_STATUS), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.ROLE), is(nullValue()));
        assertThat(filter.getSortBy(), is(""));
        assertThat(filter.isSortDescending(), is(false));
        assertThat(filter.getStartItem(), is(1));
        assertThat(filter.getCount(), is(250));

        assertThat(jsonListableUsers, is(notNullValue()));
        assertThat(jsonListableUsers.size(), is(1));
        assertThatJsonListableUserHasExpectedValues(jsonListableUsers.get(0), user);
    }

    @Test
    public void switchDelegatesToPartySwitcher() {

        Contact contact = new Contact("first", "last", "country", null, "email@address.net", null, null, new Date());
        Company company = new Company("company name", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(1L, "vXP9l0Or", contact, company, ApprovalStatus.APPROVED, PartyType.BUYER, 1, null);
        User user = new User(2L, "username", "password", Lists.newArrayList(Role.BUYER), 0, true, false, true, party,
                true, new Date());

        JsonUser jsonUser = Mockito.mock(JsonUser.class);

        when(jsonUser.getId()).thenReturn(2L);
        when(userSwitcher.switchUser(2L)).thenReturn(user);

        userService.switchUser(2L, jsonUser);

        verify(userSwitcher).switchUser(2L);
    }

    private void assertThatJsonListableUserHasExpectedValues(JsonUser jsonListableUser, User listableUser) {
        assertThat(jsonListableUser.getId(), is(listableUser.getId()));
        assertThat(jsonListableUser.getUsername(), is(listableUser.getUsername()));
        assertThat(jsonListableUser.getParty().getFirstName(), is(listableUser.getParty().getContact().getFirstName()));
        assertThat(jsonListableUser.getParty().getEmail(), is(listableUser.getParty().getContact().getEmailAddress()));
        assertThat(jsonListableUser.getParty().getEnglishCompanyName(), is(listableUser.getParty().getCompany()
                .getEnglishName()));
        assertThat(jsonListableUser.getParty().getApprovalStatus(), is(listableUser.getParty().getApprovalStatus()
                .getName()));
        assertThat(jsonListableUser.isLocked(), is(listableUser.isLocked()));
        assertThat(jsonListableUser.isBlocked(), is(listableUser.isBlocked()));
    }
}
