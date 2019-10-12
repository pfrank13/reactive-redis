package com.github.pfrank13.reactiveredis.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * @author pfrank
 */
@RestController
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(final UserService userService){
    this.userService = userService;
    afterPropertiesSet();
  }

  private void afterPropertiesSet(){
    Assert.notNull(userService, "UserService cannot be null");
  }

  @GetMapping(path = "/user/{id}")
  public Mono<User> getUser(@PathVariable("id") final int id){
    return userService.getUser(id);
  }

  @PutMapping(path = "/user")
  public Mono<Void> updateUser(@RequestBody final Mono<User> requestUserMono){
    return userService.updateUser(requestUserMono);
  }
}
