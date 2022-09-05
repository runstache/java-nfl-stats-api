package com.lswebworld.nfl.stats.api.repositories;

import com.lswebworld.nfl.stats.data.dataobjects.Statistic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Statistics Repository.
 */
@Repository
public interface StatisticsRepository extends JpaRepository<Statistic, Long> {

  /**
   * Finds a Statistic based on Code, Category, Schedule and Team.
   *
   * @param statisticCodeId Statistic Code id
   * @param categoryId Category Code id
   * @param scheduleId Schedule Id
   * @param teamId Team Id
   * @return Statistic Entry
   */
  Optional<Statistic> findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(
          long statisticCodeId,
          int categoryId,
          long scheduleId,
          int teamId);

  /**
   * Finds a Statistic based on Code, Category, Schedule and Player.
   *
   * @param statisticCodeId Statistic Code Id
   * @param categoryId Category Code Id
   * @param scheduleId Schedule Id
   * @param playerId Player Id
   * @return Statistic Entry
   */
  Optional<Statistic> findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(
          long statisticCodeId,
          int categoryId,
          long scheduleId,
          long playerId);
}
