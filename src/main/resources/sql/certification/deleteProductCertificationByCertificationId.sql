delete from product_certification 
where
    certification_id in (:certificationIds);