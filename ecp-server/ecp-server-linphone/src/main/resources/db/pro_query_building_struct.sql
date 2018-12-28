DROP PROCEDURE IF EXISTS pro_query_building_struct;
CREATE PROCEDURE pro_query_building_struct(IN gatingId VARCHAR(36))
  BEGIN #一个门控机可以对应多个单元
    SET @result = CONCAT('SELECT
                        f.buildingId,
                        f.projectId,
                        f.buildingCode,
                        f.buildingName,
                        f.buildingFullName,
                        f.doorPassword,
                        c.id storeyId,
                        c.building_name storeyName,
                        dy.id apartmentId,
                        dy.building_name apartmentName,
                        d.id ridgepoleId,
                        d.building_name ridgepoleName
                      FROM
                        (
                          SELECT
                            b.id buildingId,
                            b.project_id projectId,
                            b.building_code buildingCode,
                            b.building_name buildingName,
                            b.building_full_name buildingFullName,
                            b.`password` doorPassword,
                            b.building_type,
                            b.company_id,
                            b.pid
                          FROM
                            building_gate bg,
                            tc_building b
                          WHERE
                            bg.gate_id = \'', gatingId, ' \'
                          AND bg.building_id = b.id
                          AND b.building_type <> \'ceng\'
                          AND b.building_type <> \'danyuanrukou\'
                          AND b.building_type <> \'store\'
                          AND b.building_type <> \'dongzuo\'
                          AND b.company_id IS NOT NULL
                          ) f
                          LEFT JOIN tc_building c ON f.pid = c.building_code
                          AND c.company_id = f.company_id
                          LEFT JOIN tc_building dy ON c.pid = dy.building_code
                          AND c.company_id = dy.company_id
                          LEFT JOIN tc_building d ON dy.pid = d.building_code
                          AND d.company_id = dy.company_id');
    PREPARE resultStmt FROM @result;
    EXECUTE resultStmt;
  END