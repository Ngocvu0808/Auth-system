package com.ttt.mar.config;

import com.ttt.rnd.common.CommonModule;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.utils.JsonUtils;
import com.ttt.rnd.common.utils.RequestUtils;
import com.ttt.rnd.lib.LibrariesModule;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EntityScan(basePackages = {"com.ttt.mar.config.entities"})
@EnableJpaRepositories(basePackages = {"com.ttt.mar.config.repositories"})
@EnableTransactionManagement
@Import({LibrariesModule.class, CommonModule.class})
public class ConfigServiceApplication {

  private static final Logger logger = LoggerFactory.getLogger(ConfigServiceApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(ConfigServiceApplication.class, args);
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
