package com.daugherty.e2c.service.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.UserSwitcher;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.UserFilter;
import com.daugherty.e2c.business.mapper.PartyMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.mail.sender.RegistrationConfirmationMailSender;
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

/**
 * REST resource for User.
 */
@Controller
@RequestMapping("/users")
public class UserService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private UserDeleter userDeleter;
    @Inject
    private UserUpdater userChangePasswordUpdater;
    @Inject
    private UserUpdater userResetPasswordUpdater;
    @Inject
    private UserConfirmer userConfirmer;
    @Inject
    private UserGatekeeper userGatekeeper;
    @Inject
    private UserSwitcher userSwitcher;
    @Inject
    private Accessor<User> userAccessor;
    @Inject
    private PendingUserReadDao pendingUserReadDao;
    @Inject
    private PendingUserUpdater pendingUserUpdater;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private RegistrationConfirmationMailSender registrationConfirmationMailSender;
    @Inject
    private PartyMapper partyMapper;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonUsers retrieveUsers(@RequestParam(value = UserFilter.USERNAME_PREFIX, required = false) String username,
            @RequestParam(value = UserFilter.FIRST_NAME_PREFIX, required = false) String firstName, @RequestParam(
                    value = UserFilter.LAST_NAME_PREFIX, required = false) String lastName, @RequestParam(
                    value = UserFilter.EMAIL_PREFIX, required = false) String email, @RequestParam(
                    value = UserFilter.COMPANY_NAME_PREFIX, required = false) String companyName, @RequestParam(
                    value = UserFilter.APPROVAL_STATUS, required = false) String approvalStatus, @RequestParam(
                    value = UserFilter.ROLE, required = false) String role, @RequestParam(value = UserFilter.PRESENCE,
                    required = false) Boolean presence,
            @RequestParam(value = UserFilter.ACCESS, required = false) String access, @RequestParam(
                    value = BaseFilter.SORT_BY, required = false, defaultValue = "lastUpdated") String sortBy,
            @RequestParam(value = BaseFilter.SORT_DESC, defaultValue = "true") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count, Locale locale) {

        UserFilter filter = new UserFilter(username, firstName, lastName, email, companyName,
                verifyApprovalStatusParameter(approvalStatus), role, presence, access, sortBy, sortDesc, startItem,
                count);
        List<User> listableUsers = userAccessor.find(filter);

        JsonUsers jsonListableUsers = new JsonUsers();
        for (User listableUser : listableUsers) {
            jsonListableUsers.add(new JsonUser(listableUser, documentUrlFactory, locale));
        }

        return jsonListableUsers;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonUser retrieveUser(@PathVariable Long id, Locale locale) {

        User user = userAccessor.load(id, locale);

        return new JsonUser(user, documentUrlFactory, locale);
    }

    @RequestMapping(value = "/confirmation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonUserConfirmation confirmUser(@RequestBody JsonUserConfirmation jsonUserConfirmation) {
        LOG.info("Confirming User with username " + jsonUserConfirmation.getUsername());
        userConfirmer.confirmUser(jsonUserConfirmation.getConfirmationToken(), jsonUserConfirmation.getUsername(),
                jsonUserConfirmation.getPassword(), jsonUserConfirmation.getRole());
        return jsonUserConfirmation;
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonUserReset forgotPassword(@RequestBody JsonUserReset jsonUser, Locale locale) {
        LOG.info("Forgot Password  with email address " + jsonUser.getEmailAddress());

        userDeleter.deleteUser(jsonUser.getEmailAddress(), locale);
        return jsonUser;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.SUPPLIER_MODERATOR, Role.ADMIN, Role.BUYER_MODERATOR, Role.TRANSLATOR })
    @ResponseBody
    public JsonUserReset changePassword(@RequestBody JsonUserReset jsonUser, Locale locale) {
        LOG.info("Change Password  with username " + jsonUser.getUsername());

        userChangePasswordUpdater.updatePassword(jsonUser.getUsername(), jsonUser.getOldPassword(),
                jsonUser.getNewPassword(), jsonUser.getNewPasswordConfirmation(), locale);
        return jsonUser;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonUserReset resetPassword(@RequestBody JsonUserReset jsonUser, Locale locale) {
        LOG.info("Reset Password  with username " + jsonUser.getUsername());

        PendingUser pendingUser = getPendingUser(jsonUser);
        if (pendingUser != null) {
            pendingUserUpdater.updatePassword(pendingUser.getUsername(), jsonUser.getOldPassword(),
                    jsonUser.getNewPassword(), jsonUser.getNewPasswordConfirmation(), locale);

            jsonUser.setIsPending(true);
            jsonUser.setLink(pendingUser.getConfirmationToken());
        } else {
            userResetPasswordUpdater.updatePassword(null, jsonUser.getOldPassword(), jsonUser.getNewPassword(),
                    jsonUser.getNewPasswordConfirmation(), locale);
            jsonUser.setIsPending(false);
            jsonUser.setLink(null);
        }

        return jsonUser;
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonUser unlockUser(@RequestBody JsonUser jsonUser) {
        LOG.info("Unlock User with username " + jsonUser.getUsername());
        userGatekeeper.unlockUser(jsonUser.getUsername());
        return jsonUser;
    }

    @RequestMapping(value = "/block", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonUser blockUser(@RequestBody JsonUser jsonUser) {
        LOG.info("Block User with username " + jsonUser.getUsername());
        userGatekeeper.blockUser(jsonUser.getUsername());
        return jsonUser;
    }

    @RequestMapping(value = "/unblock", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonUser unblockUser(@RequestBody JsonUser jsonUser) {
        LOG.info("Unblock User with username " + jsonUser.getUsername());
        userGatekeeper.unblockUser(jsonUser.getUsername());
        return jsonUser;
    }

    @RequestMapping(value = "/resendConfirmation", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonUser resendConfirmation(@RequestBody JsonUser jsonUser) {
        registrationConfirmationMailSender.resend(partyMapper.toExistingDomainObject(jsonUser.getParty().getId(),
                jsonUser.getParty(), Locale.ENGLISH).getId());

        return jsonUser;
    }

    @RequestMapping(value = "/{id}/switch", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonUser switchUser(@PathVariable Long id, @RequestBody JsonUser jsonUser) {
        User user = userSwitcher.switchUser(id);

        return new JsonUser(user, documentUrlFactory, Locale.ENGLISH);
    }

    private ApprovalStatus verifyApprovalStatusParameter(String approvalStatus) {
        return StringUtils.isBlank(approvalStatus) ? null : ApprovalStatus.findByName(approvalStatus);
    }

    private PendingUser getPendingUser(JsonUserReset jsonUser) {

        try {
            PendingUser pendingUser = pendingUserReadDao.loadPendingUserByPassword(jsonUser.getOldPassword());
            return pendingUser;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
