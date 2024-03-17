package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ValidatorFactory validatorFactory;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        validate(user);
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
                ps.setString(2, userRoles.get(i).toString());
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
                "SELECT * FROM users LEFT JOIN user_role ON user_role.user_id = users.id WHERE ? ORDER BY name, email",
                true);
    }

    private <T> List<User> getUserListWithRolesByParameter(String sql, T parameter) {
        return jdbcTemplate.query(sql, rs -> {
            List<User> users = new ArrayList<>();
            User user = new User();
            Set<Role> roles = new HashSet<>();
            while (rs.next()) {
                int userId = rs.getInt("id");
                if (user.getId() != null) {
                    if (user.getId() != userId) {
                        savingUserWithRolesInList(users, user, roles);
                        user = new User();
                        roles = new HashSet<>();
                    } else {
                        roles.add(Role.valueOf(rs.getString("role")));
                        continue;
                    }
                }
                user.setId(userId);
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRegistered(rs.getDate("registered"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                String roleName = rs.getString("role");
                if (roleName != null) {
                    roles.add(Role.valueOf(roleName));
                }
            }
            if (user.getId() != null) {
                savingUserWithRolesInList(users, user, roles);
            }
            return users;
        }, parameter);
    }

    private void savingUserWithRolesInList(List<User> list, User user, Set<Role> set) {
        user.setRoles(set);
        list.add(user);
    }


    private void validate(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new RuntimeException(violations.toString());
        }
    }
}
