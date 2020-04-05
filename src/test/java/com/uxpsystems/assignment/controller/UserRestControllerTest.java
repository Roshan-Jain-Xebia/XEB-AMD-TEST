package com.uxpsystems.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.exception.NotFoundException;
import com.uxpsystems.assignment.exception.RequestNotAllowedException;
import com.uxpsystems.assignment.exception.RequestNotValidException;
import com.uxpsystems.assignment.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @WithMockUser(username="admin")
    @Test
    public void testGetAllUsers_Success()
            throws Exception {
        User user = new User();
        user.setUsername("guest");
        user.setPassword("Guest@1234");
        user.setStatus("Activated");
        List<User> allusers = Arrays.asList(user);

        given(userService.getUsers()).willReturn(allusers);

        mvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())));

        verify(userService, times(1)).getUsers();
        verifyNoMoreInteractions(userService);
    }


    @WithMockUser(username="admin")
    @Test
    public void testGetUser_NotFoundError()
            throws Exception {
        User user = new User();
        user.setUsername("guest");
        user.setPassword("Guest@1234");
        user.setStatus("Activated");
        List<User> allusers = Arrays.asList(user);

        given(userService.getUser(7)).willThrow(new NotFoundException());

        mvc.perform(get("/user/7")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(7L);
        verifyNoMoreInteractions(userService);
    }

    @WithMockUser(username="admin")
    @Test
    public void testGetUserById_success()
            throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("guest");
        user.setPassword("Guest@1234");
        user.setStatus("Activated");
        List<User> allusers = Arrays.asList(user);

        given(userService.getUser(1L)).willReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/user/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())));

        verify(userService, times(1)).getUser(1L);
        verifyNoMoreInteractions(userService);
    }

    @WithMockUser(username="admin")
    @Test
    public void testCreateUser_success()
            throws Exception {
        User newUser = new User();
        newUser.setUsername("guest");
        newUser.setPassword("Guest@1234");
        newUser.setStatus("Activated");

        given(userService.createUser(Mockito.any(User.class))).willReturn(newUser);

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .content(asJsonString(newUser))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(newUser.getUsername())));
    }

    @WithMockUser(username="user", roles = {"USER"})
    @Test
    public void testCreateUser_AuthError()
            throws Exception {
        User newUser = new User();
        newUser.setUsername("guest");
        newUser.setPassword("Guest@1234");
        newUser.setStatus("Activated");

        given(userService.createUser(Mockito.any(User.class))).willThrow(new AccessDeniedException("Forbidden"));

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .content(asJsonString(newUser))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="admin")
    @Test
    public void testCreateUserWithDuplicateUsername_usernameNotAvailableError()
            throws Exception {
        User newUser = new User();
        newUser.setUsername("guest");
        newUser.setPassword("Guest@1234");
        newUser.setStatus("Activated");

        given(userService.createUser(Mockito.any(User.class))).willThrow(new RequestNotAllowedException("conflict"));

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .content(asJsonString(newUser))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("conflict")));
    }

    @WithMockUser(username="admin")
    @Test
    public void testDeleteUser_success()
            throws Exception {
        User deletedUser = new User();
        deletedUser.setId(1L);
        deletedUser.setUsername("guest");
        deletedUser.setPassword("Guest@1234");
        deletedUser.setStatus("Activated");

        given(userService.deleteUser(1L)).willReturn(deletedUser);

        mvc.perform(MockMvcRequestBuilders
                .delete("/user/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username="admin")
    @Test
    public void testDeleteUser_NotFoundError()
            throws Exception {

        given(userService.deleteUser(1L)).willThrow(new NotFoundException());

        mvc.perform(MockMvcRequestBuilders
                .delete("/user/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="admin")
    @Test
    public void testUpdateUser_success()
            throws Exception {
        User updateUser = new User();
        updateUser.setUsername("guest");
        updateUser.setPassword("Guest@1234");
        updateUser.setStatus("Activated");

        given(userService.updateUser(updateUser)).willReturn(updateUser);

        mvc.perform(MockMvcRequestBuilders
                .put("/user/{id}", "1")
                .content(asJsonString(updateUser))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @WithMockUser(username="admin")
    @Test
    public void testUpdateUserWithDuplicateUserName_thenConflictError()
            throws Exception {
        User updateUser = new User();
        updateUser.setUsername("guest");
        updateUser.setPassword("Guest@1234");
        updateUser.setStatus("Activated");

        given(userService.updateUser(Mockito.any(User.class))).willThrow(new RequestNotAllowedException());

        mvc.perform(MockMvcRequestBuilders
                .put("/user/{id}", "1")
                .content(asJsonString(updateUser))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @WithMockUser(username="admin")
    @Test
    public void testUpdateUserWithIncorrectStatus_thenReturnError()
            throws Exception {
        User updateUser = new User();
        updateUser.setUsername("guest");
        updateUser.setPassword("Guest@1234");
        updateUser.setStatus("Activatedlklj");

        given(userService.updateUser(Mockito.any(User.class))).willThrow(new RequestNotValidException());

        mvc.perform(MockMvcRequestBuilders
                .put("/user/{id}", "1")
                .content(asJsonString(updateUser))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username="admin")
    @Test
    public void testUpdateUserWithIncorrectIdFormat_thenReturnBadRequest()
            throws Exception {
        User updateUser = new User();
        updateUser.setUsername("guest");
        updateUser.setPassword("Guest@1234");
        updateUser.setStatus("Activated");

        mvc.perform(MockMvcRequestBuilders
                .put("/user/{id}", "1L")
                .content(asJsonString(updateUser))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @WithMockUser(username="admin")
    @Test
    public void testUpdateUserWithoutId_MethodNotAllowedError()
            throws Exception {
        User updateUser = new User();
        updateUser.setUsername("guest");
        updateUser.setPassword("Guest@1234");
        updateUser.setStatus("Activated");

        mvc.perform(MockMvcRequestBuilders
                .put("/user")
                .content(asJsonString(updateUser))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
