package com.daugherty.e2c.mail;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EmailFactoryTest {

    private final EmailFactory factory = new EmailFactory("from address", "email subject",
            "Username: %s Link: %s Username Again: %s");

    @Test
    public void sendWithoutFromAddressOverrideCreatesEmailUsingConstructorFromAddress() {
        Email email = factory.createEmail("to address", new Object[] { "username", "confirmation token text",
                "username" });

        assertThat(email.getFrom(), is("from address"));
        assertThat(email.getTo(), is("to address"));
        assertThat(email.getSubject(), is("email subject"));
        assertThat(email.getBody(), is("Username: username Link: confirmation token text Username Again: username"));
        assertThat(email.getContent(), is("text/html; charset=utf-8"));
    }

    @Test
    public void sendWithFromAddressCreatesEmailUsingThatFromAddressInsteadOfOneProdivedToConstructor() {
        Email email = factory.createEmail("another from address", "to address", new Object[] { "username",
                "confirmation token text", "username" });

        assertThat(email.getFrom(), is("another from address"));
        assertThat(email.getTo(), is("to address"));
        assertThat(email.getSubject(), is("email subject"));
        assertThat(email.getBody(), is("Username: username Link: confirmation token text Username Again: username"));
        assertThat(email.getContent(), is("text/html; charset=utf-8"));
    }

}
