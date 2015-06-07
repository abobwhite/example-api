-- Buy Lead for Apdent Travel Packages
INSERT INTO buy_lead (buy_lead_id, party_id, product_category_id, 
                        effective_date, expiration_date, last_modified_by, last_modified_date) 
  VALUES (1, 22, 2810,
          CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 'UserDataLoad', CURRENT_TIMESTAMP);
          
-- Buy Lead for Apdent Resorts
INSERT INTO buy_lead (buy_lead_id, party_id, product_category_id, 
                        effective_date, expiration_date, last_modified_by, last_modified_date) 
  VALUES (2, 22, 2808,
          CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 'UserDataLoad', CURRENT_TIMESTAMP);
          
-- Buy Lead for Apdent Cruises EXPIRED
INSERT INTO buy_lead (buy_lead_id, party_id, product_category_id, 
                        effective_date, expiration_date, last_modified_by, last_modified_date) 
  VALUES (3, 22, 2803,
          DATE_SUB(CURRENT_DATE, INTERVAL 2 MONTH), DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), 'UserDataLoad', CURRENT_TIMESTAMP);
