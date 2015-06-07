-- Supplier  99912 Product 2 Images
INSERT INTO product_audit_image_link (product_audit_image_link_id, product_audit_id, product_image_link, is_primary, last_modified_by, last_modified_date) 
    VALUES (66, 52, 'Product Image Link 1', true, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_audit_image_link (product_audit_image_link_id, product_audit_id, product_image_link, is_primary, last_modified_by, last_modified_date) 
    VALUES(67, 52, 'Product Image Link 2', false, 'UserDataLoad', CURRENT_TIMESTAMP); 

INSERT INTO product_audit_image_link (product_audit_image_link_id, product_audit_id, product_image_link, is_primary,  last_modified_by, last_modified_date) 
    VALUES(68, 52, 'Product Image Link 3', false, 'UserDataLoad',  CURRENT_TIMESTAMP);