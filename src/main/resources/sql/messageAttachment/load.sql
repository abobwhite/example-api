SELECT message_attachment_id,
       message_id,
       attachment_name,
       attachment_link
  FROM message_attachment
 WHERE message_attachment_id = :messageAttachmentId;