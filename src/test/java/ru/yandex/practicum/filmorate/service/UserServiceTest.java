//package ru.yandex.practicum.filmorate.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class UserServiceTest {
//
//    @Autowired
//    @Qualifier("") //todo
//    UserService userService;
//    User user1;
//    User user2;
//
//    @BeforeEach
//    void dataPreparation() {
//        user1 = new User();
//        user1.setId(1);
//        user1.setEmail("ms.qw@yandex.ru");
//        user1.setLogin("ms");
//        user1.setBirthday(LocalDate.of(1991, 12, 12));
//
//        user2 = new User();
//        user2.setId(2);
//        user2.setEmail("ads@ya.ru");
//        user2.setLogin("ads");
//        user2.setBirthday(LocalDate.of(1990, 11, 11));
//
//    }
//
//    @Test
//    void makeFriend() {
//        userService.makeFriend(user1, user2);
//        assertEquals(new HashSet<Integer>(List.of(2)), user1.getFriends());
//        assertEquals(new HashSet<Integer>(List.of(1)), user2.getFriends());
//    }
//
//    @Test
//    void deleteFriend() {
//        userService.makeFriend(user1, user2);
//        assertEquals(new HashSet<Integer>(List.of(2)), user1.getFriends());
//
//        userService.deleteFriend(user1, user2);
//        assertTrue(user1.getFriends().isEmpty());
//        assertTrue(user2.getFriends().isEmpty());
//
//    }
//
//    @Test
//    void getCommonFriend() {
//        User user3 = new User();
//        user3.setId(3);
//        user3.setEmail("m33s.qw@yandex.ru");
//        user3.setLogin("ms33");
//        user3.setBirthday(LocalDate.of(1991, 12, 12));
//
//        User user4 = new User();
//        user4.setId(4);
//        user4.setEmail("ad444s@ya.ru");
//        user4.setLogin("ads44");
//        user4.setBirthday(LocalDate.of(1990, 11, 11));
//
//        userService.makeFriend(user1, user3);
//        userService.makeFriend(user1, user4);
//        userService.makeFriend(user2, user3);
//
//        assertEquals(new HashSet<Integer>(List.of(3)), userService.getCommonFriend(user1, user2));
//        assertEquals(Collections.emptySet(), userService.getCommonFriend(user1, user4));
//    }
//}