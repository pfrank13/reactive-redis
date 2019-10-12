package com.github.pfrank13.reactiveredis.config;

import com.github.pfrank13.reactiveredis.user.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author pfrank
 */
@Configuration
public class UserConfiguration {
  @Value("${wiremock.port}")
  private Integer wiremockPort;

  @Bean
  public WebClient userWebClient(){
    return WebClient.builder()
        .baseUrl("http://localhost:" + wiremockPort + "/user")
        .build();
  }

  @Bean
  //I don't believe this is idiomatic but maybe it is, seems very verbose for config given it's tied to User but maybe you can use <String, ?> dunno
  public ReactiveRedisOperations<String, User> userRedisOperations(ReactiveRedisConnectionFactory factory) {
    final Jackson2JsonRedisSerializer<User> serializer = new Jackson2JsonRedisSerializer<>(User.class);

    RedisSerializationContext.RedisSerializationContextBuilder<String, User> builder =
        RedisSerializationContext.newSerializationContext(serializer);

    RedisSerializationContext<String, User> context = builder.build();

    return new ReactiveRedisTemplate<>(factory, context);
  }
}
