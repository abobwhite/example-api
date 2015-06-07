SELECT p.was_approved_once
  FROM latest_party_view p 
 WHERE p.party_id = :partyId