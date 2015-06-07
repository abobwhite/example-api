INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (42, 25, 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Inquiry basket for party 21
INSERT INTO inquiry_basket (party_id, product_id, last_modified_by, last_modified_date)
  VALUES (21, 41, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO inquiry_basket (party_id, product_id, last_modified_by, last_modified_date)
  VALUES (21, 42, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO inquiry_basket (party_id, product_id, last_modified_by, last_modified_date)
  VALUES (21, 51, 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Inquiry basket for party 30
INSERT INTO inquiry_basket (party_id, product_id, last_modified_by, last_modified_date)
  VALUES (30, 41, 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Pending inquiry for party 30
INSERT INTO pending_inquiry (pending_inquiry_id, party_id, subject, body, submission_timestamp, is_disapproved, last_modified_by, last_modified_date)
  VALUES (301, 30, 'Cherry Pie Anyone', 'What is all this cherry stuff about anyway?', '2013-12-17 16:13:00', true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO pending_inquiry_product (pending_inquiry_id, product_id, last_modified_by, last_modified_date)
  VALUES (301, 42, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO pending_inquiry_product (pending_inquiry_id, product_id, last_modified_by, last_modified_date)
  VALUES (301, 51, 'UnitTestLoad', CURRENT_TIMESTAMP);
