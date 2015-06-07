UPDATE pending_inquiry SET
  is_disapproved = true
WHERE pending_inquiry_id = :inquiryId
