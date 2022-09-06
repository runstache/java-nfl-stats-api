package com.lswebworld.nfl.stats.api.repositories;

import com.lswebworld.nfl.stats.data.dataobjects.Schedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Schedule JPA Repository.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  List<Schedule> findAllByGameId(long gameId);

  /**
   * Retrieves a Schedule based on the Game id and Team id.
   *
   * @param gameId GameId
   * @param teamId TeamId
   * @return Schedule
   */
  Optional<Schedule> findByGameIdAndTeamId(long gameId, int teamId);

  /**
   * Retrieves Schedule Entries by the Week, Year and Type.
   *
   * @param week Week Number
   * @param year Year Number
   * @param typeId TypeId
   * @return List of Schedule Entries
   */
  List<Schedule> findAllByWeekAndYearAndTypeId(int week, int year, int typeId);
}
