import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.exception.NotFoundException;
import com.uxpsystems.assignment.exception.RequestNotValidException;
import com.uxpsystems.assignment.service.UserRepository;
import com.uxpsystems.assignment.service.UserService;
import com.uxpsystems.assignment.service.UserServiceImpl;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringRunner.class)
public class UserServiceTest {
    @TestConfiguration
    static class UserServiceTestConfig {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }
    @Autowired
    UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void whenUserDataIsAvailable_thenGetUsersShouldReturnUsers() {
        User user = new User();
        user.setUsername("Josh");
        user.setId(1L);
        user.setStatus("Activated");
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        List<User> actualUser = userService.getUsers();
        Long resultSize = Long.valueOf(actualUser.size());
        assertThat(1L, is(resultSize));
    }


    @Test
    public void whenIdGiven_thenUserShouldBeFound() {
        Long id =5L;
        User user = new User();
        user.setUsername("Josh");
        user.setId(5L);
        user.setStatus("Activated");
        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        User actualUser = userService.getUser(id);

        assertThat(actualUser.getId(),is(id));
    }

    @Test(expected = NotFoundException.class)
    public void whenIdNotGiven_thenGetUserShouldreturnError() {
        Long id = 0L;
        User user = new User();
        User actualUser = userService.getUser(id);
    }

    @Test
    public void whenIdNotGiven_thenDeleteUserShouldReturnError() {
        Long id = 1L;
        User user = new User();
        user.setUsername("Josh");
        user.setId(1L);
        user.setStatus("Activated");

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        User deletedUser = userService.deleteUser(id);

        assertThat(deletedUser.getId(),is(id));
    }

    @Test(expected = RequestNotValidException.class)
    public void whenIdGiven_thenUserShouldBeDeleted() {
        Long id = 0L;
        User deletedUser = userService.deleteUser(id);
    }

    @Test(expected = RequestNotValidException.class)
    public void whenUserNameIsEmptyUserReg_thenCreateUserShouldReturnError() {
        User newUser = new User();
        newUser.setUsername("");
        newUser.setId(1L);
        newUser.setStatus("Activated");
        newUser.setPassword("password");
        userService.createUser(newUser);
    }

    @Test(expected = RequestNotValidException.class)
    public void whenPasswordIsEmptyUserReg_thenCreateUserShouldReturnError() {
        User newUser = new User();
        newUser.setUsername("testUser");
        newUser.setId(1L);
        newUser.setStatus("Activated");
        newUser.setPassword("");
        userService.createUser(newUser);
    }

    @Test(expected = RequestNotValidException.class)
    public void whenStatusIsNotValidUserReg_thenCreateUserShouldReturnError() {
        User newUser = new User();
        newUser.setUsername("testUser");
        newUser.setId(1L);
        newUser.setStatus("ActivatedError");
        newUser.setPassword("password");
        userService.createUser(newUser);
    }

    @Test(expected = RequestNotValidException.class)
    public void whenIdDuplicateRequestForUserReg_thenCreateUserShouldReturnError() {

        User newUser = new User();
        newUser.setUsername("Josh");
        newUser.setId(1L);
        newUser.setStatus("Activated");
        newUser.setPassword("password");

        User regUser = new User();
        regUser.setUsername("Josh");
        regUser.setId(1L);
        regUser.setStatus("Activated");
        regUser.setPassword("password");

        Mockito.when(userRepository.findByUsername(newUser.getUsername())).thenReturn(regUser);

        User newRegUser = userService.createUser(newUser);
    }

    @Test
    public void whenValidUserRegRequest_thenCreateUserShouldReturnNewRegUser() {

        User newUser = new User();
        newUser.setUsername("Josh");
        newUser.setId(1L);
        newUser.setStatus("Activated");
        newUser.setPassword("password");

        User regUser = new User();
        regUser.setUsername("Josh");
        regUser.setId(1L);
        regUser.setStatus("Activated");
        regUser.setPassword("password");

        Mockito.when(userRepository.findByUsername(newUser.getUsername())).thenReturn(null);
        Mockito.when(userRepository.save(newUser)).thenReturn(regUser);
        User newRegUser = userService.createUser(newUser);

        assertThat(newUser.getUsername(), equalTo(newRegUser.getUsername()));
    }

    @Test
    public void whenValidUpdateUserRequest_thenUpdateUserShouldReturnUpdatedUser() {

        User newUserData = new User();
        newUserData.setUsername("Josh");
        newUserData.setId(1L);
        newUserData.setStatus("DeActivated");
        newUserData.setPassword("password");

        User regUser = new User();
        regUser.setUsername("Josh");
        regUser.setId(1L);
        regUser.setStatus("Activated");
        regUser.setPassword("password");

        Mockito.when(userRepository.findById(newUserData.getId())).thenReturn(Optional.of(regUser));
        Mockito.when(userRepository.save(newUserData)).thenReturn(newUserData);
        User updatedUser = userService.updateUser(newUserData);

        assertThat(newUserData.getStatus(), equalTo(updatedUser.getStatus()));
    }

    @Test(expected = RequestNotValidException.class)
    public void whenUpdateUserRequestIdNull_thenUpdateUserShouldReturnError() {
        User newUserData = new User();
        userService.updateUser(newUserData);
    }

    @Test(expected = RequestNotValidException.class)
    public void whenDuplicateUserNameUpdateUserRequest_thenUpdateUserShouldReturnError() {

        User newUserData = new User();
        newUserData.setUsername("Josh");
        newUserData.setId(1L);
        newUserData.setStatus("DeActivated");
        newUserData.setPassword("password");

        User regUser = new User();
        regUser.setUsername("Josh2");
        regUser.setId(1L);
        regUser.setStatus("Activated");
        regUser.setPassword("password");

        Mockito.when(userRepository.findById(newUserData.getId())).thenReturn(Optional.of(regUser));
        userService.updateUser(newUserData);
    }


    public static Matcher<Long> is(Long value) {
        return org.hamcrest.core.Is.is(value.longValue());
    }
}
