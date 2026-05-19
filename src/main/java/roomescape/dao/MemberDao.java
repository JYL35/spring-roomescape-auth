package roomescape.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

import java.util.List;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> Member.from(
            resultSet.getLong("id"),
            resultSet.getString("login_id"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("role")
    );

    @Autowired
    public MemberDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Member findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
    }

    public List<Member> findAllByIds(List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            return List.of();
        }
        String sql = "SELECT * FROM member WHERE id IN (:memberIds)";
        MapSqlParameterSource parameters = new MapSqlParameterSource("memberIds", memberIds);
        return namedParameterJdbcTemplate.query(sql, parameters, memberRowMapper);
    }
}
