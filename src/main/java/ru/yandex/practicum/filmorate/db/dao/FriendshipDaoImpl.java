package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FriendshipDaoImpl {

    private final JdbcTemplate db;

    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        db = jdbcTemplate;
    }

    public void addFriendship(int requestId, int responseId) {
        String sql = "SELECT * FROM friendship WHERE user_requester_id = ? AND user_responser_id = ?";

        Map<String, Object> map = db.queryForMap(sql, requestId, responseId);
        map.putAll( db.queryForMap(sql,responseId, requestId));

        if(map.isEmpty()){
         // вставить дружбу с вейтинг
        } else {

        }
//обработать дублирование DuplicateKeyException
    }

}
