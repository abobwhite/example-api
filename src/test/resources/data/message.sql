-- Message from Buyer 22 to Supplier 30 about Product 242
INSERT INTO message (message_id, from_party_id, to_party_id, subject, message_type, last_modified_by, last_modified_date)
  VALUES (2230242808, 22, 30, 'Inquiry about Devilled Eggs', 'Product', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO message_product (product_id, message_id, last_modified_by, last_modified_date)
  VALUES (242, 2230242808, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO message_product (product_id, message_id, last_modified_by, last_modified_date)
  VALUES (808, 2230242808, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (22302428081, 2230242808, 22, 30, 'Are your eggs really from Hell?',
          '2013-11-22 15:53:00', false, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (22302428082, 2230242808, 30, 22, 'Is this a serious question? Really???',
          '2013-12-04 13:32:00', false, 'UnitTestLoad', CURRENT_TIMESTAMP);
          
INSERT into message_attachment(message_attachment_id, message_id, attachment_name, attachment_link, last_modified_by, last_modified_date)
  VALUES (121212, 2230242808, 'Devilled Egg Price Chart', 'price link', 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Flagged Message from Buyer 22 to Supplier 21 about Product 911          
INSERT INTO message (message_id, from_party_id, to_party_id, subject, message_type, last_modified_by, last_modified_date)
  VALUES (2221911, 22, 21, 'Inquiry about Hitch-Hikers Guide to the Galaxy', 'Product', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO message_product (product_id, message_id, last_modified_by, last_modified_date)
  VALUES (911, 2221911, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (22219111, 2221911, 22, 21, 'My planet has been demolished and I am panicking!',
          '2013-11-25 09:39:00', true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO message_flag (message_id, party_id, last_modified_by, last_modified_date)
  VALUES (2221911, 21, 'UnitTestLoad', CURRENT_TIMESTAMP);
 
  
-- Buy Lead for Pending Approval Buyer Passenger Transportation Services
INSERT INTO buy_lead (buy_lead_id, party_id, product_category_id, 
                        effective_date, expiration_date, last_modified_by, last_modified_date) 
VALUES (3, 22, 2807,
          DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 'UserDataLoad', CURRENT_TIMESTAMP);
    
-- Message from Buyer 22 to Supplier 21 about Product Category Resorts
INSERT INTO message (message_id, from_party_id, to_party_id, subject, message_type, last_modified_by, last_modified_date)
  VALUES (222143, 22, 21, 'Inquiry about Passenger Transportation Services', 'Buy Lead', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO message_buy_lead (buy_lead_id, message_id, last_modified_by, last_modified_date)
  VALUES (3, 222143, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (2221421, 222143, 22, 21, 'Do you know where I can find a nice Resort? I cannot find any.',
          '2013-11-22 15:38:00', true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (2221422, 222143, 21, 22, 'I have many to choose form.  What is is your desired destination.',
          '2013-11-22 16:43:00', true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (2221423, 222143, 22, 21, 'I would like to go to ABOB castle in Lake St. Louis.',
          '2013-11-22 16:45:00', false, 'UnitTestLoad', CURRENT_TIMESTAMP);
  