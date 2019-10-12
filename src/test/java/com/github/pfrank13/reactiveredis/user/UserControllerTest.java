package com.github.pfrank13.reactiveredis.user;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern;
import com.sun.jna.platform.win32.OaIdl;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;


/**
 * @author pfrank
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {UserControllerTest.RandomPortInitailizer.class})
public class UserControllerTest {
  private static final Logger LOG = LoggerFactory.getLogger(UserControllerTest.class);

  @ClassRule
  public static GenericContainer redis = new GenericContainer("redis:5.0.5").withExposedPorts(6379).waitingFor(
      Wait.forLogMessage(".*Ready to accept connections.*\\n", 1));

  @ClassRule
  public static WireMockRule wireMockRule = new WireMockRule();


  @Autowired
  private TestRestTemplate testRestTemplate;

  @LocalServerPort
  private int port;

  @Test
  public void testUpdateUser() throws Exception {
    final User expected = new User(112, "myUser");
    stubFor(put("/user")
        .withRequestBody(new EqualToJsonPattern("{\"id\":112,\"name\":\"myUser\"}", true, false))
        .willReturn(aResponse()
                        .withHeader("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody("{\"id\":112,\"name\":\"myUser\"}")));

    testRestTemplate.put("http://localhost:" + port + "/user", expected);

    final User user = testRestTemplate.getForObject("http://localhost:" + port + "/user/" + expected.getId(), User.class);
    Assertions.assertThat(user).isEqualToComparingFieldByField(expected);

    LOG.info("####################### Assertions Done #######################");
  }

  public static class RandomPortInitailizer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                                                                "spring.redis.port=" + redis.getFirstMappedPort(), "wiremock.port=" + wireMockRule.port());
    }

  }
}
