package com.lswebworld.nfl.stats.api.controllers;

import com.lswebworld.nfl.stats.api.constants.ErrorConstants;
import com.lswebworld.nfl.stats.api.exceptions.ScheduleProcessingFailure;
import com.lswebworld.nfl.stats.api.repositories.ScheduleRepository;
import com.lswebworld.nfl.stats.api.repositories.TeamRepository;
import com.lswebworld.nfl.stats.api.repositories.TypeCodeRepository;
import com.lswebworld.nfl.stats.data.dataobjects.Schedule;
import com.lswebworld.nfl.stats.data.models.ScheduleModel;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Saving Schedule Entries.
 */

@RestController
public class ScheduleController {

  private final ScheduleRepository scheduleRepo;
  private final TeamRepository teamRepo;
  private final TypeCodeRepository typeRepo;

  /**
   * Controller Constructor.
   *
   * @param scheduleRepo Schedule Repository
   * @param teamRepo Team repository
   * @param typeRepo Type Repository
   */
  @Autowired
  public ScheduleController(ScheduleRepository scheduleRepo,
                            TeamRepository teamRepo,
                            TypeCodeRepository typeRepo) {
    this.scheduleRepo = scheduleRepo;
    this.teamRepo = teamRepo;
    this.typeRepo = typeRepo;
  }

  /**
   * Retrieves the Schedules based on Week, Year and Type.
   *
   * @param params Parameters
   * @return List of Schedules
   */
  @GetMapping("/api/schedules")
  public List<Schedule> getSchedules(@RequestParam Map<String, String> params) {

    if (params.containsKey("typeCode")
            && params.containsKey("week")
            && params.containsKey("year")) {
      var typeCode = typeRepo.findByCode(params.get("typeCode"));
      if (typeCode.isPresent()) {
        return scheduleRepo.findAllByWeekAndYearAndTypeId(
                Integer.parseInt(params.get("week")),
                Integer.parseInt(params.get("year")),
                typeCode.get().getId());
      }
    }
    return Collections.emptyList();
  }

  /**
   * Post Method for saving Schedules.
   *
   * @param model Schedule Model.
   */
  @PostMapping("/api/schedule")
  public void post(@RequestBody ScheduleModel model) {

    var schedule = new Schedule();
    schedule.setYear(model.getYear());
    schedule.setWeek(model.getWeek());
    schedule.setUrl(model.getUrl());
    schedule.setHomeGame(model.isHomeGame());
    schedule.setGameId(model.getGameId());

    var opponent = teamRepo.findByUrl(model.getOpponentUrl());
    var team = teamRepo.findByUrl(model.getTeamUrl());
    if (team.isPresent() && opponent.isPresent()) {
      schedule.setTeamId(team.get().getId());
      schedule.setOpponentId(opponent.get().getId());
    } else {
      throw new ScheduleProcessingFailure(ErrorConstants.NO_TEAM_FOUND);
    }

    var type = typeRepo.findByCode(model.getTypeCode());
    if (type.isPresent()) {
      schedule.setTypeId(type.get().getId());
    } else {
      throw new ScheduleProcessingFailure(ErrorConstants.NO_TYPE_CODE);
    }

    hydrateSchedule(schedule);
    scheduleRepo.save(schedule);

  }

  private void hydrateSchedule(Schedule schedule) {
    var result = scheduleRepo.findByGameIdAndTeamId(schedule.getGameId(), schedule.getTeamId());
    result.ifPresent(r -> schedule.setId(r.getId()));
  }

}
