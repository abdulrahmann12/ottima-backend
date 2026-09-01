-- تعديل الـ Check Constraint المطبق على عمود overall_status في جدول projects ليقبل PENDING
ALTER TABLE projects DROP CONSTRAINT IF EXISTS UK_projects_overall_status;
ALTER TABLE projects DROP CONSTRAINT IF EXISTS projects_overall_status_check;

-- لو الـ H2 مش معترفة بالـ Drop القديم، بنعدل الجدول مباشرة أو نضيف الـ Check الجديد بالقيم كلها
ALTER TABLE projects ADD CONSTRAINT chk_projects_status
    CHECK (overall_status IN ('PENDING', 'ACTIVE', 'DELIVERED', 'PAUSED', 'COMPLETED'));