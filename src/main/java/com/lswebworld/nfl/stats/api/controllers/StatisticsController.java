package com.lswebworld.nfl.stats.api.controllers;

import com.lswebworld.nfl.stats.api.constants.ErrorConstants;
import com.lswebworld.nfl.stats.api.exceptions.StatisticProcessingFailure;
import com.lswebworld.nfl.stats.api.repositories.PlayerRepository;
import com.lswebworld.nfl.stats.api.repositories.ScheduleRepository;
import com.lswebworld.nfl.stats.api.repositories.StatisticCategoryRepository;
import com.lswebworld.nfl.stats.api.repositories.StatisticCodeRepository;
import com.lswebworld.nfl.stats.api.repositories.StatisticsRepository;
import com.lswebworld.nfl.stats.api.repositories.TeamRepository;
import com.lswebworld.nfl.stats.data.dataobjects.Statistic;
import com.lswebworld.nfl.stats.data.models.StatisticModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Statistic REST Controller.
 */

@RestController
public class StatisticsController {

  private final StatisticsRepository statsRepo;
  private final StatisticCategoryRepository statCatRepo;
  private final StatisticCodeRepository statCodeRepo;
  private final ScheduleRepository scheduleRepo;
  private final PlayerRepository playerRepo;
  private final TeamRepository teamRepo;

  /**
   * Controller Constructor.
   *
   * @param statsRepo Statistics Repository
   * @param playerRepo Player Repository
   * @param scheduleRepo Schedule Repository
   * @param teamRepo Team Repository
   * @param statsCatRepo Stats Category Repository
   * @param statsCodeRepo Stats Code Repository
   */
  @Autowired
  public StatisticsController(StatisticsRepository statsRepo,
                              PlayerRepository playerRepo,
                              ScheduleRepository scheduleRepo,
                              TeamRepository teamRepo,
                              StatisticCategoryRepository statsCatRepo,
                              StatisticCodeRepository statsCodeRepo) {
    this.statsRepo = statsRepo;
    this.scheduleRepo = scheduleRepo;
    this.playerRepo = playerRepo;
    this.teamRepo = teamRepo;
    this.statCatRepo = statsCatRepo;
    this.statCodeRepo = statsCodeRepo;
  }

  /**
   * Post Operation for saving Statistics.
   *
   * @param model Statistic Model
   */
  @PostMapping("/api/statistics")
  public void post(@RequestBody StatisticModel model) {

    var stat = new Statistic();
    stat.setValue(model.getValue());

    var catCode = statCatRepo.findByCode(model.getCategoryCode());
    if (catCode.isPresent()) {
      stat.setCategoryId(catCode.get().getId());
    } else {
      throw new StatisticProcessingFailure(ErrorConstants.NO_CATEGORY_FOUND);
    }

    var statCode =
            statCodeRepo.findByCategoryIdAndCode(catCode.get().getId(), model.getStatisticCode());
    if (statCode.isPresent()) {
      stat.setStatisticCodeId(statCode.get().getId());
    } else {
      throw new StatisticProcessingFailure(ErrorConstants.NO_CODE_FOUND);
    }

    hydrateContext(stat, model.getGameId(), model.getPlayerUrl(), model.getTeamCode());
    hydrateStatistic(stat);
    statsRepo.save(stat);
  }

  private void hydrateContext(Statistic stat, long gameId, String playerUrl, String teamCode) {

    if (StringUtils.isAllEmpty(playerUrl, teamCode)) {
      throw new StatisticProcessingFailure(ErrorConstants.MISSING_STAT_CONTEXT);
    }

    var team = teamRepo.findByCode(teamCode);

    if (team.isPresent()) {
      hydrateSchedule(stat, gameId, team.get().getId());

      if (StringUtils.isEmpty(playerUrl)) {
        stat.setTeamId(team.get().getId());
      }
    } else {
      throw new StatisticProcessingFailure(ErrorConstants.NO_TEAM_FOUND);
    }

    if (StringUtils.isNotEmpty(playerUrl)) {
      var player = playerRepo.findByUrl(playerUrl);
      if (player.isPresent()) {
        stat.setPlayerId(player.get().getId());
      } else {
        throw new StatisticProcessingFailure(ErrorConstants.NO_PLAYER_FOUND);
      }
    }
  }

  private void hydrateSchedule(Statistic stat, long gameId, int teamId) {
    var schedule = scheduleRepo.findByGameIdAndTeamId(gameId, teamId);
    if (schedule.isPresent()) {
      stat.setScheduleId(schedule.get().getId());
    } else {
      throw new StatisticProcessingFailure(ErrorConstants.NO_SCHEDULE_FOUND);
    }
  }

  private void hydrateStatistic(Statistic stat) {
    if (stat.getPlayerId() > 0) {
      var result =
              statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(
                      stat.getStatisticCodeId(),
                      stat.getCategoryId(),
                      stat.getScheduleId(),
                      stat.getPlayerId());
      result.ifPresent(s -> stat.setId(s.getId()));
    } else {
      if (stat.getTeamId() > 0) {
        var result =
                statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(
                        stat.getStatisticCodeId(),
                        stat.getCategoryId(),
                        stat.getScheduleId(),
                        stat.getTeamId());
        result.ifPresent(s -> stat.setId(s.getId()));
      }
    }
  }
}