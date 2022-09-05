package com.lswebworld.nfl.stats.api.repositories;

import com.lswebworld.nfl.stats.data.dataobjects.StatisticCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Statistic Code JPA Repository.
 */
@Repository
public interface StatisticCodeRepository extends JpaRepository<StatisticCode, Long> {

  /**
   * Retrieves a Statistic Code by the Code value.
   *
   * @param code Statistic Code
   * @return Statistic Code
   */
  Optional<StatisticCode> findByCode(String code);

}
