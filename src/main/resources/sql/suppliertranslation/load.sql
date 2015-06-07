SELECT
  ptv.approval_id,
  ptv.public_approval_id,
  ptv.snapshot_id,
  ptv.title,
  ptv.approval_type,
  ptv.event_type,
  ptv.version_number,
  ptv.event_datetime,
  ptv.last_modified_by,
  ptv.translated
FROM pending_translation_view ptv
WHERE ptv.approval_id = :id
