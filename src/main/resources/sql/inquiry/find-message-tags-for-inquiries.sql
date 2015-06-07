SELECT pending_inquiry_id,
       message_tag_id
FROM pending_inquiry_message_tag
WHERE pending_inquiry_id IN (:inquiryIds)
