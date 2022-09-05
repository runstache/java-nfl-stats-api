package com.lswebworld.nfl.stats.api.repositories;

import com.lswebworld.nfl.stats.data.dataobjects.StatisticCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Statistic Category JPA Respoitory.
 */
@Repository
public interface StatisticCategoryRepository extends JpaRepository<StatisticCategory, Integer> {

  /**
   * Retrieves a Statistic Category by Code Value.
   *
   * @param code Category Code
   * @return Statistic Category
   */
  Optional<StatisticCategory> findByCode(String code);

}
