package com.github.pfrank13.reactiveredis.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

/**
 * @author pfrank
 */
@Component
public class UserRepository {
  private final ReactiveRedisOperations<String, User> userRedisOperations;

  @Autowired
  public UserRepository(final ReactiveRedisOperations<String, User> userRedisOperations){
    this.userRedisOperations = userRedisOperations;
    afterPropertiesSet();
  }

  private void afterPropertiesSet(){
    Assert.notNull(userRedisOperations, "UserRedisOperation cannot be null");
  }

  public Mono<User> findById(final Integer id){
    return userRedisOperations.<Integer, User>opsForHash().get("user", id);
  }

  public Mono<Boolean> save(final User user){
    return userRedisOperations.<Integer, User>opsForHash().put("user", user.getId(), user);
  }
}
