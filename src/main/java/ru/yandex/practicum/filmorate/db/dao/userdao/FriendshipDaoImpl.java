package ru.yandex.practicum.filmorate.db.dao.userdao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FriendshipDaoImpl implements FriendshipDao {

    private final JdbcTemplate jdbcTemplate;

    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int requestId, int responseId) throws DuplicateKeyException {
        String sql = "INSERT INTO friendship (user_requester_id, user_responser_id) VALUES (?,?)";
        jdbcTemplate.update(sql, requestId, responseId);
    }

    @Override
    public void deleteFriend(int firstFriendId, int secondFriendId) {
        String sql = "DELETE FROM friendship WHERE user_requester_id =? AND  user_responser_id = ?";
        jdbcTemplate.update(sql, firstFriendId, secondFriendId);
    }

    @Override
    public List<Integer> getUserFriends(int userId) {
        String sql = "SELECT user_responser_id FROM friendship WHERE user_requester_id = ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) ->
                        rs.getInt("user_responser_id"),
                userId);
    }

}
