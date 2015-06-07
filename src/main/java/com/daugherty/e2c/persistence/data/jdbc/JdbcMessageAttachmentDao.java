package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.MessageAttachment;
import com.daugherty.e2c.persistence.data.MessageAttachmentReadDao;
import com.daugherty.e2c.persistence.data.MessageAttachmentWriteDao;
import com.google.common.collect.Lists;

/**
 * Spring JDBC implementation of the Message Attachment database operations.
 */
@Repository
public class JdbcMessageAttachmentDao extends JdbcDao implements MessageAttachmentReadDao, MessageAttachmentWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    static final String ATTACHMENT_ID_COLUMN_NAME = "message_attachment_id";
    static final String ATTACHMENT_NAME_COLUMN_NAME = "attachment_name";
    static final String ATTACHMENT_LINK_COLUMN_NAME = "attachment_link";
    static final String MESSAGE_ID_COLUMN_NAME = "message_id";

    private SimpleJdbcInsert messageAttachementInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        messageAttachementInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("message_attachment")
                .usingGeneratedKeyColumns(ATTACHMENT_ID_COLUMN_NAME)
                .usingColumns(ATTACHMENT_NAME_COLUMN_NAME, ATTACHMENT_LINK_COLUMN_NAME, MESSAGE_ID_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public List<MessageAttachment> loadByMessageIds(List<Long> messageIds) {
        List<MessageAttachment> messageAttachments = Lists.newArrayList();

        LOGGER.debug("Looking up message attachemnts with message ids " + messageIds);
        for (List<Long> partitionedIds : Lists.partition(messageIds, 1000)) {

            String sql = getSql("messageAttachment/loadByMessageIds.sql");
            SqlParameterSource parameterSource = new MapSqlParameterSource("messageIds", partitionedIds);
            RowMapper<MessageAttachment> rowMapper = new RowMapper<MessageAttachment>() {
                @Override
                public MessageAttachment mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new MessageAttachment(rs.getLong(ATTACHMENT_ID_COLUMN_NAME),
                            rs.getLong(MESSAGE_ID_COLUMN_NAME), rs.getString(ATTACHMENT_NAME_COLUMN_NAME),
                            rs.getString(ATTACHMENT_LINK_COLUMN_NAME));
                }
            };

            messageAttachments.addAll(jdbcTemplate.query(sql, parameterSource, rowMapper));
        }

        return messageAttachments;
    }

    @Override
    public MessageAttachment load(Long messageAttachmentId) {
        String sql = getSql("messageAttachment/load.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("messageAttachmentId", messageAttachmentId);
        RowMapper<MessageAttachment> rowMapper = new RowMapper<MessageAttachment>() {
            @Override
            public MessageAttachment mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MessageAttachment(rs.getLong(ATTACHMENT_ID_COLUMN_NAME), rs.getLong(MESSAGE_ID_COLUMN_NAME),
                        rs.getString(ATTACHMENT_NAME_COLUMN_NAME), rs.getString(ATTACHMENT_LINK_COLUMN_NAME));
            }
        };

        return jdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    @Override
    public void insertAttachment(MessageAttachment messageAttachement) {
        LOGGER.debug("Creating Message Atachment with message Id " + messageAttachement.getMessageId());

        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(ATTACHMENT_NAME_COLUMN_NAME, messageAttachement.getAttachmentName())
                .addValue(ATTACHMENT_LINK_COLUMN_NAME, messageAttachement.getAttachmentLink())
                .addValue(MESSAGE_ID_COLUMN_NAME, messageAttachement.getMessageId());
        Number key = messageAttachementInsert.executeAndReturnKey(parameterSource);

        messageAttachement.setId(key.longValue());

    }

    @Override
    public int deleteAttachement(Long messageAttachmentId) {
        String sql = getSql("messageAttachment/delete.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("messageAttachmentId", messageAttachmentId);

        return jdbcTemplate.update(sql, parameterSource);
    }

}
