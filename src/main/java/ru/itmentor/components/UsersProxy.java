package ru.itmentor.components;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.itmentor.model.User;


@Component
public class UsersProxy {

    private final RestTemplate rest;
    private final HttpHeaders headersForSession;
    private User user = new User(3L, "James", "Brown", (byte) 5);
    private String key;

    private String usersServiceUrl = "http://94.198.50.185:7081/api/users/";

    public UsersProxy(RestTemplate rest, HttpHeaders httpHeaders) {
        this.rest = rest;
        this.headersForSession = httpHeaders;
    }

    public void start() {
        getAllUsers();
        createUser();
        updateUser();
        deleteUser();
    }

    private void getAllUsers() {
        ResponseEntity<Object> response = rest.exchange(usersServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        HttpHeaders headers = response.getHeaders();
        String sessionId = headers.getFirst("Set-Cookie");

        headersForSession.setContentType(MediaType.APPLICATION_JSON);
        headersForSession.add("Cookie", sessionId);
        System.out.println("getAllUsers: " + response.getBody());
    }

    private void createUser() {
        HttpEntity<User> entityPost = new HttpEntity<>(user, headersForSession);
        ResponseEntity<String> response = rest.exchange(usersServiceUrl, HttpMethod.POST, entityPost, String.class);
        key = response.getBody();
    }

    private void updateUser() {
        ResponseEntity<Object> response1 = rest.exchange(usersServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        System.out.println("after create: " +response1.getBody());
        user.setName("Thomas");
        user.setLastName("Shelby");
        HttpEntity<User> entityPut = new HttpEntity<>(user, headersForSession);
        ResponseEntity<String> response = rest.exchange(usersServiceUrl, HttpMethod.PUT, entityPut, String.class);

        key += response.getBody();
    }

    private void deleteUser() {
        HttpEntity<Object> entityDelete = new HttpEntity<>(headersForSession);
        ResponseEntity<String> response = rest.exchange(
                usersServiceUrl + "/" + user.getId(), HttpMethod.DELETE, entityDelete, String.class);
        key += response.getBody();
    }

    public String getKey() {
        return key;
    }
}
