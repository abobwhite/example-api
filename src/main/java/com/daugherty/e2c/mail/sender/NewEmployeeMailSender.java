package com.daugherty.e2c.mail.sender;

import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.google.common.base.Joiner;

@Component
public class NewEmployeeMailSender {
    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory newEmployeeEmail;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public void send(Employee employee) {
        try {

            Email email = newEmployeeEmail.createEmail(employee.getEmailWithPersonalName(),
                    new Object[] { employee.getContact().getFirstName(), employee.getUsername(),
                            Joiner.on("<br/>").join(employee.getRoles()) });
            restTemplate.postForObject(emailUrlBuilder.buildMailUrl(Locale.ENGLISH), email, Email.class);
            LOG.info("Sent notice for new employee " + employee.getUsername() + " to "
                    + employee.getEmailWithPersonalName());
        } catch (RuntimeException e) {
            LOG.info(
                    "Unable to send mail for new employee " + employee.getUsername() + " to "
                            + employee.getEmailWithPersonalName(), e);
        }
    }

}
