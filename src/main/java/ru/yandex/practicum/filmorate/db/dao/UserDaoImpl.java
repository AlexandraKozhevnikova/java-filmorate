package ru.yandex.practicum.filmorate.db.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate db;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        db = jdbcTemplate;
    }

    @Override
    public int add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(db)
                .withTableName("user_filmorate")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(mapUserToMap(user)).intValue();
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE user_filmorate SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        int affectRows = db.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        log.info("[" + this.getClass().getSimpleName() + "] " + "Update User affected " + affectRows +  " row(s)");
    }

    @Override
    public Optional<User> getUserById(int id) {
        SqlRowSet userRow = db.queryForRowSet(
                " SELECT id, email, login, name, birthday  FROM USER_FILMORATE WHERE id = ?",
                id
        );

        if (userRow.next()) {
            User user = User.builder()
                    .id(userRow.getInt("id"))
                    .email(userRow.getString("email"))
                    .login(userRow.getString("login"))
                    .name(userRow.getString("name"))
                    .birthday(userRow.getDate("birthday").toLocalDate())
                    .build();

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT id, email, login, name, birthday FROM user_filmorate";
        List<User>  userList = db.query(sql, this::mapRowToUser);
        return userList;
    }

    private User mapRowToUser(ResultSet rs, int rowNumber) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    private Map<String, Object> mapUserToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("email", user.getEmail());
        map.put("login", user.getLogin());
        map.put("name", user.getName());
        map.put("birthday", user.getBirthday());

        return map;
    }
}
