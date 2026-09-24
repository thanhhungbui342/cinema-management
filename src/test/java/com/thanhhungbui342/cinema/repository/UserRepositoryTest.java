package com.thanhhungbui342.cinema.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import com.thanhhungbui342.cinema.config.TestJpaConfig;
import com.thanhhungbui342.cinema.entity.User;

@DataJpaTest 
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestJpaConfig.class)
@Rollback(true)
public class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    private User sampleUser;

    @BeforeEach 
    void setUp(){
        User testUser = new User();
        testUser.setEmail("thanhhungbui342@gmail.com");
        testUser.setUsername("thanhhungbui342");
        testUser.setPassword("password123");
        testUser.setFullname("Bùi Thanh Hùng");

        sampleUser = userRepository.saveAndFlush(testUser);
    }

    @Test 
    @DisplayName("Add new User")
    void testSaveUser(){
        User user = new User();
        user.setEmail("testuser@gmail.com");
        user.setUsername("newuser");
        user.setPassword("password123");
        user.setFullname("New User");

        User savedUser = userRepository.saveAndFlush(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isBeforeOrEqualTo(Instant.now());

        Optional<User> found = userRepository.findByEmail("thanhhungbui342@gmail.com");
        assertThat(found).isPresent();
    }

    @Nested 
    @DisplayName("Custom queries test")
    class TestQuery {

        @Test 
        @DisplayName("Should find user by email")
        void testFindUserByEmail(){
            Optional<User> found = userRepository.findByEmail("thanhhungbui342@gmail.com");

            assertThat(found).isPresent();
            assertThat(found.get().getFullname()).isEqualTo("Bùi Thanh Hùng");
        }

        @Test 
        @DisplayName("Should find user by UUID")
        void testFindUserByUuid(){
            Optional<User> found = userRepository.findByUuid(sampleUser.getUuid());

            assertThat(found).isPresent();
            assertThat(found.get().getUuid()).isEqualTo(sampleUser.getUuid());
        }

        @Test
        @DisplayName("Test query existByEmail") 
        void testExistByEmail(){
            assertThat(userRepository.existsByEmail("thanhhungbui342@gmail.com")).isTrue();
            assertThat(userRepository.existsByEmail("notfound@gmail.com")).isFalse();
        }
    }

}
