package com.daugherty.e2c.mail.sender;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class NewEmployeeMailSenderTest {

    private Contact contact = new Contact(null, null, "First", "Name", null, null, "email@email.com", null, null, null,
            null, null, null, null);
    private Employee employee = new Employee("username", "password", Lists.newArrayList("role1", "role2"), contact);

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private EmailFactory newEmployeeEmail;

    @InjectMocks
    private final NewEmployeeMailSender newEmployeeMailSender = new NewEmployeeMailSender();

    @Captor
    private ArgumentCaptor<Object[]> bodyArgumentsCaptor;

    @Before
    public void setup() {
        when(emailUrlBuilder.buildMailUrl(Locale.ENGLISH)).thenReturn("englishPostUrl");
    }

    @Test
    public void testSendEmail() {
        newEmployeeMailSender.send(employee);

        verify(newEmployeeEmail).createEmail(eq(employee.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());

        Object[] bodyArguments = bodyArgumentsCaptor.getValue();

        assertThat(bodyArguments[0].toString(), is(employee.getContact().getFirstName()));
        assertThat(bodyArguments[1].toString(), is(employee.getUsername()));
        assertThat(bodyArguments[2].toString(), is("role1<br/>role2"));
    }
}
