delete from party_certification 
where
    certification_id in (:certificationIds);