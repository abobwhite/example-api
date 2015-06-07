delete 
  from pending_inquiry_product 
 where product_id in (select p.product_id from product p where p.party_id = :partyId)
