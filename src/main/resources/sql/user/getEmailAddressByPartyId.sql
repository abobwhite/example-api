SELECT first_name,
       last_name,
       email_address
FROM latest_party_view
WHERE party_id = :partyId
