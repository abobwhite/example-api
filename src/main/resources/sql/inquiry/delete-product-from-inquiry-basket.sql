DELETE FROM inquiry_basket
WHERE party_id = :partyId
  AND product_id = :productId
