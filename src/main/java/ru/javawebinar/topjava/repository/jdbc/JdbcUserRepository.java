package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = rs -> {
        Map<Integer, User> userMap = new LinkedHashMap<>();
        Set<Role> roles = EnumSet.noneOf(Role.class);
        int rowCounter = 0;
        while (rs.next()) {
            int userId = rs.getInt("id");
            if (!userMap.containsKey(userId)) {
                User user = ROW_MAPPER.mapRow(rs, rowCounter);
                user.setRoles(roles);
                roles = EnumSet.noneOf(Role.class);
                userMap.put(userId, user);
            }
            String roleName = rs.getString("role");
            if (roleName != null) {
                userMap.get(userId).getRoles().add(Role.valueOf(rs.getString("role")));
            }
            rowCounter++;
        }
        return new ArrayList<>(userMap.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
        }
        List<Role> userRoles = new ArrayList<>(user.getRoles());
        jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                ps.setString(2, userRoles.get(i).name());
            }

            @Override
            public int getBatchSize() {
                return userRoles.size();
            }
        });
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return DataAccessUtils.singleResult(getUserListWithRolesByParameter(
                "SELECT * FROM users LEFT JOIN user_role ON user_role.user_id = users.id WHERE users.id=?", id));
    }

    @Override
    public User getByEmail(String email) {
        return DataAccessUtils.singleResult(getUserListWithRolesByParameter(
                "SELECT * FROM users LEFT JOIN user_role ON user_role.user_id = users.id WHERE email=?", email));
    }

    @Override
    public List<User> getAll() {
        return getUserListWithRolesByParameter(
                "SELECT * FROM users LEFT JOIN user_role ON user_role.user_id = users.id ORDER BY name, email");
    }

    private <T> List<User> getUserListWithRolesByParameter(String sql, T... parameter) {
        return jdbcTemplate.query(sql, RESULT_SET_EXTRACTOR, parameter);
    }
}
