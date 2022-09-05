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
   * @param gameId Game Id
   * @param teamId Team Id
   * @return Schedule
   */
  Optional<Schedule> findByGameIdAndTeamId(long gameId, int teamId);
}
