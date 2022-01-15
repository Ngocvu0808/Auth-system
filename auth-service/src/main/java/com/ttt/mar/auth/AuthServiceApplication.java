package com.ttt.mar.auth;

import com.ttt.rnd.cache.CacheModule;
import com.ttt.rnd.common.CommonModule;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.utils.RequestUtils;
import com.ttt.rnd.lib.LibrariesModule;
import com.ttt.rnd.common.utils.JsonUtils;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableTransactionManagement
@Slf4j
@EnableJpaAuditing
@Import({LibrariesModule.class, CacheModule.class, CommonModule.class})
@ConfigurationPropertiesScan
@EntityScan(basePackages = {"com.ttt.mar.auth.entities"})
@EnableJpaRepositories(basePackages = {"com.ttt.mar.auth.repositories"})
public class AuthServiceApplication {

  private static final Logger logger = LoggerFactory.getLogger(AuthServiceApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(AuthServiceApplication.class, args);
  }

  @RestController
  static class ClientRestController {

    @RequestMapping("/service/id")
    public @ResponseBody
    String getID(HttpServletRequest request) {
      logger.info(JsonUtils.toJson(RequestUtils.getRequestHeadersInMap(request)));
      return ServiceInfo.getId();
    }

    @RequestMapping("/service/name")
    public @ResponseBody
    String getName() {
      return ServiceInfo.getName();
    }
  }
}
