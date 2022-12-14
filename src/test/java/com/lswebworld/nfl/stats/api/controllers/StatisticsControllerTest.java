package com.lswebworld.nfl.stats.api.controllers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lswebworld.nfl.stats.api.constants.ErrorConstants;
import com.lswebworld.nfl.stats.api.exceptions.StatisticProcessingFailure;
import com.lswebworld.nfl.stats.api.repositories.PlayerRepository;
import com.lswebworld.nfl.stats.api.repositories.ScheduleRepository;
import com.lswebworld.nfl.stats.api.repositories.StatisticCategoryRepository;
import com.lswebworld.nfl.stats.api.repositories.StatisticCodeRepository;
import com.lswebworld.nfl.stats.api.repositories.StatisticsRepository;
import com.lswebworld.nfl.stats.data.dataobjects.Player;
import com.lswebworld.nfl.stats.data.dataobjects.Schedule;
import com.lswebworld.nfl.stats.data.dataobjects.Statistic;
import com.lswebworld.nfl.stats.data.dataobjects.StatisticCategory;
import com.lswebworld.nfl.stats.data.dataobjects.StatisticCode;
import com.lswebworld.nfl.stats.data.models.StatisticModel;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

class StatisticsControllerTest {

  private Player player;
  private StatisticCode statCode;
  private StatisticCategory statCategory;
  private Schedule schedule;

  private Statistic stat;

  private StatisticsRepository statsRepo;
  private StatisticCategoryRepository statCatRepo;
  private StatisticCodeRepository statCodeRepo;
  private ScheduleRepository scheduleRepo;
  private PlayerRepository playerRepo;

  @Captor
  ArgumentCaptor<Statistic> captor;

  @BeforeEach
  void setup() {
    statsRepo = mock(StatisticsRepository.class);
    statCatRepo = mock(StatisticCategoryRepository.class);
    statCodeRepo = mock(StatisticCodeRepository.class);
    scheduleRepo = mock(ScheduleRepository.class);
    playerRepo = mock(PlayerRepository.class);

    player = new Player();
    player.setId(1);
    player.setName("Jim Smith");
    player.setUrl("www.google.com");
    player.setPositionId(1);

    statCode = new StatisticCode();
    statCode.setCode("PAVG");
    statCode.setId(1);
    statCode.setDescription("Passing Average");

    statCategory = new StatisticCategory();
    statCategory.setCode("O");
    statCategory.setDescription("Offense");
    statCategory.setId(1);
    schedule = new Schedule();
    schedule.setId(1);
    schedule.setTypeId(1);
    schedule.setOpponentId(2);
    schedule.setTeamId(1);
    schedule.setWeek(1);
    schedule.setUrl("www.google.com");
    schedule.setGameId(12345L);
    schedule.setYear(2022);
    schedule.setHomeGame(true);

    stat = new Statistic();
    stat.setId(25L);
    stat.setValue(25.0);
    stat.setTeamId(1);
    stat.setCategoryId(1);
    stat.setStatisticCodeId(1);
    stat.setScheduleId(1);

    MockitoAnnotations.openMocks(this);

  }

  @Test
  void testSavePlayerStatistic() {
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.empty());
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.empty());

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.of(statCategory));
    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.of(schedule));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.of(player));
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.of(statCode));

    var model = new StatisticModel();
    model.setGameId(12345L);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);
    model.setPlayerUrl("www.google.com");
    model.setTeamId((1));

    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);
    controller.post(model);

    verify(statsRepo).save(captor.capture());

    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue()).extracting(Statistic::getValue).isEqualTo(25.0);
    assertThat(captor.getValue()).extracting(Statistic::getStatisticCodeId).isEqualTo(1L);
    assertThat(captor.getValue()).extracting(Statistic::getCategoryId).isEqualTo(1);
    assertThat(captor.getValue()).extracting(Statistic::getPlayerId).isEqualTo(1L);
    assertThat(captor.getValue()).extracting(Statistic::getTeamId).isNull();

  }

  @Test
  void testSaveTeamStatistic() {
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.empty());
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.empty());

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.of(statCategory));
    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.of(schedule));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.of(player));
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.of(statCode));

    var model = new StatisticModel();
    model.setGameId(12345L);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);
    model.setTeamId(1);

    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);
    controller.post(model);

    verify(statsRepo).save(captor.capture());

    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue()).extracting(Statistic::getValue).isEqualTo(25.0);
    assertThat(captor.getValue()).extracting(Statistic::getStatisticCodeId).isEqualTo(1L);
    assertThat(captor.getValue()).extracting(Statistic::getCategoryId).isEqualTo(1);
    assertThat(captor.getValue()).extracting(Statistic::getPlayerId).isNull();
    assertThat(captor.getValue()).extracting(Statistic::getTeamId).isEqualTo(1);
  }

  @Test
  void testNoPlayerFound() {

    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.empty());
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.empty());

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.of(statCategory));
    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.of(schedule));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.empty());
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.of(statCode));

    var model = new StatisticModel();
    model.setGameId(12345L);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);
    model.setTeamId(1);
    model.setPlayerUrl("www.google.com");

    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);

    assertThatThrownBy(() -> controller.post(model))
            .isInstanceOf(StatisticProcessingFailure.class)
            .hasMessage(ErrorConstants.NO_PLAYER_FOUND);
  }

  @Test
  void testNoCategoryFound() {
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.empty());
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.empty());

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.empty());
    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.of(schedule));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.of(player));
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.of(statCode));

    var model = new StatisticModel();
    model.setGameId(12345L);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);
    model.setTeamId(1);

    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);

    assertThatThrownBy(() -> controller.post(model))
            .isInstanceOf(StatisticProcessingFailure.class)
            .hasMessage(ErrorConstants.NO_CATEGORY_FOUND);
  }

  @Test
  void testNoStatCodeFound() {

    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.empty());
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.empty());

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.of(statCategory));
    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.of(schedule));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.of(player));
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.empty());

    var model = new StatisticModel();
    model.setGameId(12345L);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);
    model.setTeamId(1);

    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);

    assertThatThrownBy(() -> controller.post(model))
            .isInstanceOf(StatisticProcessingFailure.class)
            .hasMessage(ErrorConstants.NO_CODE_FOUND);
  }

  @Test
  void testTeamStatisticExists() {
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.of(stat));
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.of(stat));

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.of(statCategory));
    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.of(schedule));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.empty());
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.of(statCode));

    var model = new StatisticModel();
    model.setGameId(12345L);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);
    model.setTeamId(1);


    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);
    controller.post(model);

    verify(statsRepo).save(captor.capture());

    assertThat(captor.getValue()).isNotNull().extracting(Statistic::getId).isEqualTo(25L);
  }

  @Test
  void testPlayerStatExists() {
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.of(stat));
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.of(stat));

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.of(statCategory));
    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.of(schedule));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.of(player));
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.of(statCode));

    var model = new StatisticModel();
    model.setGameId(12345L);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);
    model.setPlayerUrl("www.google.com");
    model.setTeamId(1);

    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);
    controller.post(model);

    verify(statsRepo).save(captor.capture());

    assertThat(captor.getValue()).isNotNull().extracting(Statistic::getId).isEqualTo(25L);
  }

  @Test
  void testNoPlayerOrTeam() {
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.empty());
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.empty());

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.of(statCategory));
    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.of(schedule));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.of(player));
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.of(statCode));

    var model = new StatisticModel();
    model.setGameId(12345L);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);

    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);

    assertThatThrownBy(() -> controller.post(model))
            .isInstanceOf(StatisticProcessingFailure.class)
            .hasMessage(ErrorConstants.MISSING_STAT_CONTEXT);
  }

  @Test
  void testPostStatTeamIdScheduleId() {
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.empty());
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.empty());

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.of(statCategory));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.of(player));
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.of(statCode));

    var model = new StatisticModel();
    model.setScheduleId(1);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);
    model.setGameId(12345L);
    model.setTeamId(1);

    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);
    controller.post(model);

    verify(statsRepo).save(captor.capture());
    verify(scheduleRepo, never()).findByGameIdAndTeamId(anyLong(), anyInt());

    assertThat(captor.getValue())
            .isNotNull()
            .extracting(Statistic::getTeamId)
            .isEqualTo(1);
    assertThat(captor.getValue())
            .isNotNull()
            .extracting(Statistic::getScheduleId)
            .isEqualTo(1L);
  }

  @Test
  void testPostPlayerStatTeamIdScheduleId() {
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndPlayerId(anyLong(), anyInt(), anyLong(), anyLong()))
            .thenReturn(Optional.empty());
    when(statsRepo.findByStatisticCodeIdAndCategoryIdAndScheduleIdAndTeamId(anyLong(), anyInt(),anyLong(), anyInt()))
            .thenReturn(Optional.empty());

    when(statCatRepo.findByCode(anyString())).thenReturn(Optional.of(statCategory));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.of(player));
    when(statCodeRepo.findByCode(anyString())).thenReturn(Optional.of(statCode));

    var model = new StatisticModel();
    model.setScheduleId(1);
    model.setStatisticCode("PAVG");
    model.setCategoryCode("O");
    model.setValue(25.0);
    model.setPlayerUrl("www.google.com");
    model.setGameId(12345L);
    model.setTeamId(1);

    var controller = new StatisticsController(statsRepo, playerRepo, scheduleRepo, statCatRepo, statCodeRepo);
    controller.post(model);

    verify(statsRepo).save(captor.capture());
    verify(scheduleRepo, never()).findByGameIdAndTeamId(anyLong(), anyInt());

    assertThat(captor.getValue())
            .isNotNull()
            .extracting(Statistic::getScheduleId)
            .isEqualTo(1L);
    assertThat(captor.getValue())
            .extracting(Statistic::getPlayerId)
            .isEqualTo(1L);
    assertThat(captor.getValue())
            .extracting(Statistic::getTeamId)
            .isNull();
  }

}
