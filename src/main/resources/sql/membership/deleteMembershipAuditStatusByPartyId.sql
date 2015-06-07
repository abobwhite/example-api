delete from membership_audit_status 
where
    membership_id in (select 
        membership_id
    from
        membership
    
    where
        party_id = :partyId);