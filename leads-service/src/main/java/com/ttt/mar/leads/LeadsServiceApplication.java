package com.ttt.mar.leads;

import com.ttt.rnd.common.CommonModule;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.lib.LibrariesModule;
import javax.servlet.http.HttpServletRequest;
import kong.unirest.Unirest;
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
@EntityScan(basePackages = {"com.ttt.mar.leads.entities"})
@EnableJpaRepositories(basePackages = {"com.ttt.mar.leads.repositories"})
@EnableTransactionManagement
@EnableJpaAuditing
@ConfigurationPropertiesScan
@Import({LibrariesModule.class, CommonModule.class})
public class LeadsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LeadsServiceApplication.class, args);
    Unirest.config().socketTimeout(0).connectTimeout(0).verifySsl(false);

  }

  @RestController
  static class ClientRestController {

    @RequestMapping("/service/id")
    public @ResponseBody
    String getID(HttpServletRequest request) {
      return ServiceInfo.getId();
    }

    @RequestMapping("/service/name")
    public @ResponseBody
    String getName() {
      return ServiceInfo.getName();
    }
  }
}
