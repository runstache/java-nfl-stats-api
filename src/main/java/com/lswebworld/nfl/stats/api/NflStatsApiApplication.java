package com.lswebworld.nfl.stats.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Spring Application.
 */
@SpringBootApplication
@EntityScan("com.lswebworld")
public class NflStatsApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(NflStatsApiApplication.class, args);
  }

}
