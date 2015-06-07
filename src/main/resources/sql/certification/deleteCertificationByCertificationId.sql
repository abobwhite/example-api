delete from certification 
where
    certification_id in (:certificationIds);