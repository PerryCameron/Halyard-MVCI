package org.ecsail.repository.temp;


import org.ecsail.dto.StatsDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlStats {


    public static StatsDTO createStatDTO(int year, DataSource dataSource) {
        StatsDTO stat = null;
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(getStatQuery(year));
            while (rs.next()) {
                stat = new StatsDTO(
                        0,
                        rs.getInt("YEAR"),
                        rs.getInt("ACTIVE_MEMBERSHIPS"), // active membership
                        rs.getInt("NON_RENEW"),
                        rs.getInt("RETURN_MEMBERS"),
                        rs.getInt("NEW_MEMBERS"),
                        0,
                        0,
                        0,
                        rs.getInt("FAMILY"),
                        rs.getInt("REGULAR"),
                        rs.getInt("SOCIAL"),
                        rs.getInt("LAKE_ASSOCIATES"),
                        rs.getInt("LIFE_MEMBERS"),
                        rs.getInt("RACE_FELLOWS"),
                        rs.getInt("STUDENT"),
                        0,
                        0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // handle exceptions here
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // handle exceptions here
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // handle exceptions here
                }
            }

            return stat;
        }
    }

        public static String getStatQuery ( int year){
            int lastYear = year - 1;
            return
                    "SELECT \n" +
                            "id.fiscal_year AS 'YEAR',\n" +
                            "COUNT(DISTINCT IF(id.mem_type = 'RM' AND id.RENEW=true,id.membership_id , NULL)) AS 'REGULAR',\n" +
                            "COUNT(DISTINCT IF(id.mem_type = 'FM' AND id.RENEW=true,id.membership_id , NULL)) AS 'FAMILY',\n" +
                            "COUNT(DISTINCT IF(id.mem_type = 'SO' AND id.RENEW=true,id.membership_id , NULL)) AS 'SOCIAL',\n" +
                            "COUNT(DISTINCT IF(id.mem_type = 'LA' AND id.RENEW=true,id.membership_id , NULL)) AS 'LAKE_ASSOCIATES',\n" +
                            "COUNT(DISTINCT IF(id.mem_type = 'LM' AND id.RENEW=true,id.membership_id , NULL)) AS 'LIFE_MEMBERS',\n" +
                            "COUNT(DISTINCT IF(id.mem_type = 'SM' AND id.RENEW=true,id.membership_id , NULL)) AS 'STUDENT',\n" +
                            "COUNT(DISTINCT IF(id.mem_type = 'RF' AND id.RENEW=true,id.membership_id , NULL)) AS 'RACE_FELLOWS',\n" +
                            "COUNT(DISTINCT IF(YEAR(m.JOIN_DATE)='" + year + "',id.membership_id, NULL)) AS 'NEW_MEMBERS',\n" +
                            "COUNT(DISTINCT IF(id.membership_id > \n" +
                            "  (\n" +
                            "  SELECT membership_id \n" +
                            "  FROM membership_id \n" +
                            "  WHERE fiscal_year=" + year + " AND MS_ID=\n" +
                            "     (\n" +
                            "     SELECT MS_ID \n" +
                            "     FROM membership_id \n" +
                            "     WHERE membership_id=\n" +
                            "        (\n" +
                            "        SELECT max(membership_id) \n" +
                            "        FROM membership_id \n" +
                            "        WHERE fiscal_year=" + lastYear +
                            "        AND membership_id < 500 \n" +
                            "        AND renew=1\n" +
                            "        ) \n" +
                            "     AND fiscal_year=" + lastYear +
                            "     )\n" +
                            "   ) \n" +
                            "AND id.membership_id < 500 \n" +
                            "AND YEAR(m.JOIN_DATE)!='" + year + "' \n" +
                            "AND (SELECT NOT EXISTS(SELECT mid FROM membership_id WHERE fiscal_year=" + lastYear + " AND RENEW=1 AND MS_ID=id.MS_ID)), id.membership_id, NULL)) AS 'RETURN_MEMBERS', \n" +
                            "SUM(NOT RENEW) as 'NON_RENEW',\n" +
                            "SUM(RENEW) as 'ACTIVE_MEMBERSHIPS'\n" +
                            "FROM membership_id id\n" +
                            "LEFT JOIN membership m on id.MS_ID=m.MS_ID \n" +
                            "WHERE fiscal_year=" + year;
        }
    }
