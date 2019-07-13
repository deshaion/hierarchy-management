package com.personio.hierarchymanager.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Repository
public class RelationshipRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public RelationshipRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public int[] saveRelationships(Map<String, String> relationships) {

        //TODO treat the case if this employee is already in database
        final Iterator<Map.Entry<String, String>> iter = relationships.entrySet().iterator();
        int[] updateCounts = jdbcTemplate
                .batchUpdate(
                        "insert into employee_relationships (employee, supervisor) values(?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                Map.Entry<String, String> entry = iter.next();
                                ps.setString(1, entry.getKey());
                                ps.setString(2, entry.getValue());
                            }

                            @Override
                            public int getBatchSize() {
                                return relationships.size();
                            }
                        });
        return updateCounts;
    }

    public Map<String, String> findEmployee(String employee) {
        Map<String, Object> params = new HashMap<>();
        params.put("employee", employee);

        String sql = "" +
                "SELECT " +
                "   first.supervisor AS supervisor, " +
                "   second.supervisor AS supervisor2Level " +
                "FROM " +
                "   employee_relationships first " +
                "LEFT JOIN" +
                "   employee_relationships second " +
                "ON " +
                "   second.employee = first.supervisor " +
                "WHERE " +
                "   first.employee = :employee ";

        return namedJdbcTemplate.query(sql, params, (ResultSet rs) -> {
                    HashMap<String,String> result = new HashMap<>();
                    if (rs.next() == false) {
                        return result;
                    }

                    result.put("supervisor", rs.getString("supervisor"));
                    if (rs.getString("supervisor2Level") != null) {
                        result.put("supervisor2Level", rs.getString("supervisor2Level"));
                    }
                    return result;
                });
    }

    public void clearRelationships() {
        jdbcTemplate.execute("DELETE FROM employee_relationships");
    }
}
