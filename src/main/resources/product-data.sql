--Green Beans
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (2101, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (21011, 2101, true, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111, 2101, true, '2013-09-12 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2101111, 'payPal', '1000 +', 
          'United States', 'USD 800', 'Mexico', '3 + Weeks', 
          'JKSG7843', 'Home grown green and tasty.', 'good for the heart, beans', 'Green Beans', 
          'ABOB Green Beans are made from the finest beans.  We promise you will love every spoonful of our delicious beans.  We gaurantee it.', 'Green Beans, Yummy in My Tummy', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (21011111, 2101111, 2,
          'Home grown green and tasty', 'Translation of payPal', 'Translation of Green Beans (ABOB)',
          'Translation of green beans description', 'Translation of Beans, Yummy in My Tummy', 'Translation of mexican food, beans', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (1101, 2101111, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (1107, 2101111, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111111, 2101, 2101111, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
--Soy Beans
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (2102, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (21012, 2102, true, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (210112, 2102, true, '2013-09-12 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2101112, 'payPal', '1000 +', 
          'United States', 'USD 800', 'Angola', '3 + Weeks', 
          'SYSJSLSJHAIUY', 'Home grown green and tasty.  ', 'grown locally, good for the heart, beans', 'Soy Beans (ABOB)', 
          'ABOB Soys Beans are made from the finest beans.  We promise you will love every spoonful of our delicious beans.  We gaurantee it.', 'Soy Beans, Yummy Beans in My Tummy', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (21011112, 2101112, 2,
          'Home grown green and tasty', 'Translation of payPal', 'Translation of Soys Beans (ABOB)',
          'Translation of soy beans description', 'Translation of Beans, Yummy in My Tummy', 'Translation of finest green beans, beans', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (1107, 2101112, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111112, 2102, 2101112, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
--Hamburger Buns
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (2103, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (21013, 2103, false, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (210113, 2103, true, '2013-09-12 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2101113, 'Credit Card', '1000 +', 
          'United States', 'USD 800', 'Canada', '3 + Weeks', 
          'SYSJSLSJHAIUY', 'Home grown green and tasty.  ', 'grown locally, good for the heart, beans', 'Hamburger Buns', 
          'Buns are made from flour.  We promise you will love every bite of out soft buns.  We gaurantee it.', 'Hamburger Buns, Tasty and Soft', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (21011113, 2101113, 2,
          'Home grown bun and tasty', 'Translation of payPal', 'Translation of Hamburger Buns',
          'Translation of hamburger bun description', 'Translation of Hamburger Buns, Tasty and Soft', 'Translation of finest buns, seeds', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (1105, 2101113, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111113, 2103, 2101113, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
--Candy Canes
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (2104, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (21014, 2104, false, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (210114, 2104, true, '2013-09-13 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2101114, 'Credit Card', '1000 +', 
          'United States', 'USD 800', 'Australia', '3 + Weeks', 
          'SYSJSLSJHAIUY', 'Sweet and Sour.  ', 'made in our candy worksop', 'Candy Cane', 
          'Candy Canes are mode of 100% Sugar.  We promise you will love every bite of out candy.  We gaurantee it.', 'Candy, Christmas', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (21011114, 2101114, 2,
          'Translation Home Sweet and Sour', 'Translation of payPal', 'Translation of Candy Canes',
          'Translation of candy cane description', 'Translation of Candy, Christmas, Tasty and Soft', 'Translation of finest candy canes, sugar', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (1105, 2101114, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111114, 2104, 2101114, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
--Popcorn
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (2105, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (21015, 2105, false, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (210115, 2105, true, '2013-09-13 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2101115, 'Credit Card', '1000 +', 
          'United States', 'USD 800', 'China', '3 + Weeks', 
          'SYSJSLSJHAIUY', 'Kette Corn, Lite Corn, Extra Butter.  ', 'made in our mill', 'Popcorn', 
          'Popcorn is made  of 100% Corn.  We promise you will love every bite of out out corn.  We gaurantee it.', 'Movie, Corn, Popcorn', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (21011115, 2101115, 2,
          'Translation Popcorn', 'Translation of Credit Card', 'Translation of Popcorn',
          'Translation of popcorn description', 'Translation of Corn, Movie, Tasty and Crunchy', 'Translation of finest corn', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (1105, 2101115, 'UserDataLoad', CURRENT_TIMESTAMP)

INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111115, 2105, 2101115, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
--Hot Wheels
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (2106, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (21016, 2106, true, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (210116, 2106, true, '2013-09-14 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2101116, 'Credit Card', '1000 +', 
          'United States', 'USD 800', 'China', '3 + Weeks', 
          'SYHOT', 'Hot Wheel Cars, Shiny and New.  ', 'made in our factory with special care', 'Hot Wheels', 
          'Hot Wheel cars used for childrens toys. Comes in may models and colors.  We promise you will out wheels.  We gaurantee it.', 'Wheel, Car, Hot', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (21011116, 2101116, 2,
          'Translation Hot Wheels', 'Translation of Credit Card', 'Translation of Hot Wheels',
          'Translation of hot wheel description', 'Translation of Wheel, Corn, Movie', 'Translation of finest wheel', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (2703, 2101116, 'UserDataLoad', CURRENT_TIMESTAMP)

INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111116, 2106, 2101116, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
--Crayons
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (2107, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (21017, 2107, true, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (210117, 2107, true, '2013-09-14 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2101117, 'Credit Card', '1000 +', 
          'United States', 'USD 800', 'Greece', '3 + Weeks', 
          'CRYSYOLOKST', '20 crayons, All Colors, Do Not Eat Not Food.', 'made in our factory with special care', 'Crayons',
          'Crayons for the little artist in the family. Comes in many models colors.  We promise you love these crayons.  Who needs the name brands.  We gaurantee it.', 'Color, ART, Crayons', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (21011117, 2101117, 2,
          'Translation Crayons', 'Translation of Credit Card', 'Translation of Crayons',
          'Translation of crayon description', 'Translation of Crayon, Color', 'Translation of finest color', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (2702, 2101117, 'UserDataLoad', CURRENT_TIMESTAMP)

INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111117, 2107, 2101117, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
--Yogurt
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (2108, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (21018, 2108, true, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (210118, 2108, true, '2013-09-12 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2101118, 'Cash', '1000 +', 
          'United States', 'USD 800', 'Greece', '3 + Weeks', 
          'YOGURT', 'Greek, Vanilla.', 'Made from Dairy', 'Greek Yougurt',
          'Yougurt for snacking  throughout the day  We promise you love out yogurt.  Who needs the name brands.  We gaurantee it.', 'Yougurt, Dairy', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (21011118, 2101118, 2,
          'Translation Yogurt', 'Translation of Cash', 'Translation of Yogurt Name',
          'Translation of yougurt description', 'Translation of Yogurt, Color', 'Translation of finest yogurt', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (1106, 2101118, 'UserDataLoad', CURRENT_TIMESTAMP)
 
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111118, 2108, 2101118, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
--Hotel
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (2109, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (21019, 2109, true, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (210119, 2109, true, '2013-09-12 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2101119, 'Cash', '1000 +', 
          'United States', 'USD 800', 'Ireland', '3 + Weeks', 
          'HOT923023', 'Hotel, Hotel.', 'Made from Finest Material', 'Hotel',
          'Best luxury Resort in Ireland.  Who needs the name brands.  We gaurantee it.', 'Hotel, Vacation, Luxury', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (21011119, 2101119, 2,
          'Translation Hotel', 'Translation of Cash', 'Translation of Hotel Name',
          'Translation of hotal description', 'Translation of Hotel, Material', 'Translation of finest hotel', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (2806, 2101119, 'UserDataLoad', CURRENT_TIMESTAMP)
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (210111119, 2109, 2101119, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
--Carrots
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (21091, 21, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (210191, 21091, true, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (2101191, 21091, true, '2013-09-12 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (21011191, 'Cash', '1000 +', 
          'United States', 'USD 800', 'Iraq', '3 + Weeks', 
          'HOT923023', 'Carrots, Orange.', 'Made from Carrots', 'Carrots',
          'Best carrots in Iraq.  Who needs the name brands.  We gaurantee the tatse of our carrots.', 'Carrots, Vegatable', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (210111191, 21011191, 2,
          'Translation Carrot', 'Translation of Cash', 'Translation of Carrot Name',
          'Translation of Carrot description', 'Translation of Carrots, Veggies', 'Translation of finest carrot', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (1107, 21011191, 'UserDataLoad', CURRENT_TIMESTAMP)
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (2101111191, 21091, 21011191, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
