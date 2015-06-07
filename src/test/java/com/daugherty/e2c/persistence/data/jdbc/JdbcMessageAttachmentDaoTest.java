package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.MessageAttachment;
import com.google.common.collect.Lists;

public class JdbcMessageAttachmentDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcMessageAttachmentDao messageAttachmentDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("message.sql");
    }

    @Test
    public void loadBySnapshotIdReturnsProductImages() throws Exception {
        List<MessageAttachment> messageAttachments = messageAttachmentDao.loadByMessageIds(Lists
                .newArrayList(2230242808L));

        assertThat(messageAttachments, is(notNullValue()));
        assertThat(messageAttachments.size(), is(1));
        assertThat(messageAttachments.get(0).getId(), is(121212L));
        assertThat(messageAttachments.get(0).getMessageId(), is(2230242808L));
        assertThat(messageAttachments.get(0).getAttachmentName(), is("Devilled Egg Price Chart"));
        assertThat(messageAttachments.get(0).getAttachmentLink(), is("price link"));
    }

    @Test
    public void inserMessageAttachment() throws Exception {
        MessageAttachment messageAttachement = new MessageAttachment(2230242808L, "attachment name", "attachment link");
        messageAttachmentDao.insertAttachment(messageAttachement);

        Map<String, Object> messageAttachmentRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM message_attachment WHERE message_attachment_id = ?", messageAttachement.getId());
        assertThat((Long) messageAttachmentRowMap.get("message_id"), is(messageAttachement.getMessageId()));
        assertThat((String) messageAttachmentRowMap.get("attachment_name"), is(messageAttachement.getAttachmentName()));
        assertThat((String) messageAttachmentRowMap.get("attachment_link"), is(messageAttachement.getAttachmentLink()));
        assertThat((Date) messageAttachmentRowMap.get("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

    @Test
    public void loadMessageAttachment() throws Exception {
        MessageAttachment messageAttachment = messageAttachmentDao.load(121212L);

        assertThat(messageAttachment.getId(), is(121212L));
        assertThat(messageAttachment.getMessageId(), is(2230242808L));
        assertThat(messageAttachment.getAttachmentName(), is("Devilled Egg Price Chart"));
        assertThat(messageAttachment.getAttachmentLink(), is("price link"));
    }

    @Test
    public void deleteMessageAttachment() throws Exception {

        assertThat(messageAttachmentDao.deleteAttachement(121212L), is(1));
    }
}
