SELECT
  mbl.buy_lead_id,
  slpv.party_id
  FROM message m 
  INNER JOIN message_buy_lead mbl on mbl.message_id = m.message_id
  INNER JOIN latest_party_view slpv ON m.from_party_id = slpv.party_id
WHERE mbl.buy_lead_id in (:buyLeadIds)
               