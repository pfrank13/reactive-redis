package com.github.pfrank13.reactiveredis.config;

import com.github.pfrank13.reactiveredis.user.User;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author pfrank
 */
@Configuration
public class UserConfiguration {
  @Bean
  public WebClient userWebClient(){
    return WebClient.builder()
        .baseUrl("http://localhost:8081/user")
        .build();
  }

  @Bean
  public ReactiveRedisOperations<String, User> userRedisOperations(ReactiveRedisConnectionFactory factory) {
    final Jackson2JsonRedisSerializer<User> serializer = new Jackson2JsonRedisSerializer<>(User.class);

    RedisSerializationContext.RedisSerializationContextBuilder<String, User> builder =
        RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

    RedisSerializationContext<String, User> context = builder.value(serializer).build();

    return new ReactiveRedisTemplate<>(factory, context);
  }
}
