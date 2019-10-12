package com.github.pfrank13.reactiveredis.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * @author pfrank
 */
@Component
public class UserService {
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
  private final UserRepository userRepository;
  private final WebClient userWebClient;

  @Autowired
  public UserService(final UserRepository userRepository,
                        final WebClient userWebClient){
    this.userRepository = userRepository;
    this.userWebClient = userWebClient;
    afterPropertiesSet();
  }

  private void afterPropertiesSet(){
    Assert.notNull(userRepository, "UserRepository cannot be null");
    Assert.notNull(userWebClient, "WebClient cannot be null");
  }

  public Mono<User> getUser(final int id){
    return userRepository.findById(id);
  }

  public Mono<Void> updateUser(final Mono<User> requestUserMono){
    return userWebClient.put()
        .body(requestUserMono, User.class)
        .exchange()
        .flatMap(clientResponse -> clientResponse.bodyToMono(User.class))
        .flatMap(userRepository::save)
        //Not using the flatMap for the success Boolean that is the result of this as it seems it's always false
        .doOnError(t -> {
          LOG.error("Error", t);
        })
        .then(); //Seems the reactor way to cooerce to Mono<Void>

  }
}
