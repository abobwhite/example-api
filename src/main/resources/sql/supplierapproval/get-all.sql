  SELECT 
  supplier_approval.approval_id,
  supplier_approval.public_approval_id,
  supplier_approval.snapshot_id,
  supplier_approval.title,
  supplier_approval.party_id,
  supplier_approval.email_address,
  supplier_approval.approval_type,
  supplier_approval.event_type,
  supplier_approval.version_number,
  supplier_approval.last_modified_by,
  supplier_approval.event_datetime,
  supplier_approval.language_id,
  supplier_approval.active_membership
FROM (
SELECT 
  lsv.party_id AS approval_id,
  public_party_id AS public_approval_id,
  party_audit_id AS snapshot_id,
  company_name_english AS title,
  lsv.party_id AS party_id,
  email_address,
  'Profile' AS approval_type,
  lsv.event_type,
  lsv.version_number,
  lsv.last_modified_by,
  lsv.event_datetime,
  language_id,
  CASE WHEN apmv.membership_id IS NULL THEN FALSE ELSE TRUE END AS active_membership
FROM latest_supplier_view as lsv
LEFT OUTER JOIN active_paid_membership_view apmv on apmv.party_id = lsv.party_id 
WHERE lsv.event_type IN ('Pending Approval', 'Waiting for Information')
UNION ALL
SELECT 
  product_id AS approval_id,
  CAST(product_id AS CHAR(20)) AS public_approval_id,
  product_audit_id AS snapshot_id,
  product_name AS title,
  lpv.party_id AS party_id,
  lpv.email_address,
  'Product' AS approval_type,
  lpv.event_type,
  lpv.version_number,
  lpv.last_modified_by,
  lpv.event_datetime,
  NULL AS language_id,
  CASE WHEN apmv.membership_id IS NULL THEN FALSE ELSE TRUE END AS active_membership
FROM latest_product_view as lpv
INNER JOIN active_paid_membership_view apmv on apmv.party_id = lpv.party_id 
INNER JOIN latest_supplier_view as lsv on lsv.party_id = lpv.party_id
WHERE lpv.event_type IN ('Pending Approval', 'Waiting for Information')
  AND lsv.event_type != 'Disapproved'
UNION ALL
SELECT 
  certification_id AS approval_id,
  CAST(certification_id AS CHAR(20)) AS public_approval_id,
  certification_audit_id AS snapshot_id,
  certification_standard AS title,
  lsv.party_id AS party_id,
  lsv.email_address as email_address,
  'Certification' AS approval_type,
  lcv.event_type,
  lcv.version_number,
  lcv.last_modified_by,
  lcv.event_datetime,
  NULL AS  language_id,
  CASE WHEN apmv.membership_id IS NULL THEN FALSE ELSE TRUE END AS active_membership
FROM latest_certification_view lcv
INNER JOIN latest_supplier_view lsv ON lsv.party_id = lcv.party_id
LEFT OUTER JOIN active_paid_membership_view apmv on apmv.party_id = lsv.party_id 
WHERE lcv.event_type IN ('Pending Approval', 'Waiting for Information')
  AND lsv.event_type != 'Disapproved') AS supplier_approval
