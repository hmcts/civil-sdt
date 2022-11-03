--------------------------------------------------------------
-- Drop each of the schemas used by SDT and all their contents
--------------------------------------------------------------

SET ECHO ON

-- non schema owners first

DROP USER SDT_USER CASCADE;
DROP USER SDT_BATCH_USER CASCADE;

-- now schema owners

DROP USER SDT_OWNER CASCADE;
