package com.github.pfrank13.reactiveredis.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * @author pfrank
 */
@RestController
public class UserController {
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
  private final UserRepository userRepository;
  private final WebClient userWebClient;

  @Autowired
  public UserController(final UserRepository userRepository,
                        final WebClient userWebClient){
    this.userRepository = userRepository;
    this.userWebClient = userWebClient;
    afterPropertiesSet();
  }

  private void afterPropertiesSet(){
    Assert.notNull(userRepository, "UserRepository cannot be null");
    Assert.notNull(userWebClient, "WebClient cannot be null");
  }

  @GetMapping(path = "/user/{id}")
  public Mono<User> getUser(@PathVariable("id") final int id){
    return userRepository.findById(id);
  }

  @PutMapping(path = "/user")
  public Mono<Void> updateUser(@RequestBody final Mono<User> requestUserMono){
    return userWebClient.put()
        .body(requestUserMono, User.class)
        .exchange()
        .flatMap(clientResponse -> clientResponse.bodyToMono(User.class))
        .flatMap(userRepository::save)
        .doOnError(t -> {
          LOG.error("Error", t);
        }).then();

  }
}
