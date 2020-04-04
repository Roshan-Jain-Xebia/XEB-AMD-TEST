package com.uxpsystems.assignment.repository;

import com.uxpsystems.assignment.dao.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenSave_thenReturnUser() {
        // given
        User user = new User();
        user.setUsername("jhon");
        user.setPassword("Password");
        user.setStatus("Activated");
        entityManager.persist(user);
        entityManager.flush();

        // when
        List<User> users = userRepository.findAll();

        // then
        assertThat(users.get(0).getUsername())
                .isEqualTo(user.getUsername());
    }

    @Test
    public void whenDelete_thenReturnEmpty() {
        // set user data
        User user = new User();
        user.setUsername("jhon");
        user.setPassword("Password");
        user.setStatus("Activated");
        entityManager.persist(user);
        entityManager.flush();

        // get all user
        List<User> users = userRepository.findAll();

        // find there is user present
        assertThat(users.get(0).getUsername())
                .isEqualTo(user.getUsername());
        //remove user
        entityManager.remove(user);
        // get users
         users = userRepository.findAll();
        // assert on non empty result
        assertThat(users.isEmpty())
                .isEqualTo(true);
    }

    @Test
    public void whenValidUsernameSearch_thenReturnMatchUser() {
        // set user data
        User user = new User();
        user.setUsername("jhon");
        user.setPassword("Password");
        user.setStatus("Activated");
        entityManager.persist(user);
        entityManager.flush();

        // get users
        User resultUser = userRepository.findByUsername("jhon");
        // assert on non empty result
        assertThat(user.getUsername())
                .isEqualTo(resultUser.getUsername());
    }

}