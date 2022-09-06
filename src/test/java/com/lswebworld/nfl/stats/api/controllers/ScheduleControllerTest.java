package com.lswebworld.nfl.stats.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lswebworld.nfl.stats.api.constants.ErrorConstants;
import com.lswebworld.nfl.stats.api.exceptions.ScheduleProcessingFailure;
import com.lswebworld.nfl.stats.api.repositories.ScheduleRepository;
import com.lswebworld.nfl.stats.api.repositories.TeamRepository;
import com.lswebworld.nfl.stats.api.repositories.TypeCodeRepository;
import com.lswebworld.nfl.stats.data.dataobjects.Schedule;
import com.lswebworld.nfl.stats.data.dataobjects.Team;
import com.lswebworld.nfl.stats.data.dataobjects.TypeCode;
import com.lswebworld.nfl.stats.data.models.ScheduleModel;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

class ScheduleControllerTest {

  private TeamRepository teamRepo;
  private ScheduleRepository scheduleRepo;
  private TypeCodeRepository typeRepo;

  private Schedule schedule;
  private Team team;

  private Team opponent;

  private TypeCode typeCode;

  @Captor
  ArgumentCaptor<Schedule> captor;

  @BeforeEach
  void setup() {
    teamRepo = mock(TeamRepository.class);
    scheduleRepo = mock(ScheduleRepository.class);
    typeRepo = mock(TypeCodeRepository.class);

    MockitoAnnotations.openMocks(this);

    schedule = new Schedule();
    schedule.setGameId(300494857L);
    schedule.setId(25L);
    schedule.setHomeGame(false);
    schedule.setUrl("www.google.com");
    schedule.setWeek(1);
    schedule.setOpponentId(2);
    schedule.setTeamId(1);
    schedule.setTypeId(1);
    schedule.setYear(2022);

    typeCode = new TypeCode();
    typeCode.setCode("R");
    typeCode.setId(1);
    typeCode.setDescription("Regular Season");

    team = new Team();
    team.setCode("WSH");
    team.setId(1);
    team.setUrl("www.google.com");
    team.setName("Washington");

    opponent = new Team();
    opponent.setName("Oakland");
    opponent.setUrl("www.google.com");
    opponent.setId(2);
    opponent.setCode("OAK");
  }

  @Test
  void testPostSchedule() {

    when(typeRepo.findByCode(anyString())).thenReturn(Optional.of(typeCode));
    when(teamRepo.findByUrl("WSH")).thenReturn(Optional.of(team));
    when(teamRepo.findByUrl("OAK")).thenReturn(Optional.of(opponent));

    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.empty());

    var model = new ScheduleModel();
    model.setGameId(300494857L);
    model.setUrl("www.google.com");
    model.setHomeGame(false);
    model.setWeek(1);
    model.setYear(2022);
    model.setTeamUrl("WSH");
    model.setOpponentUrl("OAK");
    model.setTypeCode("R");


    var controller = new ScheduleController(scheduleRepo, teamRepo, typeRepo);
    controller.post(model);

    verify(scheduleRepo).save(captor.capture());

    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue())
            .extracting(Schedule::getGameId)
            .isEqualTo(300494857L);
    assertThat(captor.getValue())
            .extracting(Schedule::getUrl)
            .isEqualTo("www.google.com");
    assertThat(captor.getValue())
            .extracting(Schedule::getWeek)
            .isEqualTo(1);
    assertThat(captor.getValue())
            .extracting(Schedule::getYear)
            .isEqualTo(2022);
    assertThat(captor.getValue())
            .extracting(Schedule::getOpponentId)
            .isEqualTo(2);
    assertThat(captor.getValue())
            .extracting(Schedule::getTeamId)
            .isEqualTo(1);
    assertThat(captor.getValue())
            .extracting(Schedule::getTypeId)
            .isEqualTo(1);
  }

  @Test
  void testNoTypeCode() {
    when(typeRepo.findByCode(anyString())).thenReturn(Optional.empty());

    when(teamRepo.findByUrl("WSH")).thenReturn(Optional.of(team));
    when(teamRepo.findByUrl("OAK")).thenReturn(Optional.of(opponent));

    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.empty());

    var model = new ScheduleModel();
    model.setGameId(300494857L);
    model.setUrl("www.google.com");
    model.setHomeGame(false);
    model.setWeek(1);
    model.setYear(2022);
    model.setTeamUrl("WSH");
    model.setOpponentUrl("OAK");
    model.setTypeCode("R");

    var controller = new ScheduleController(scheduleRepo, teamRepo, typeRepo);
    assertThatThrownBy(() -> controller.post(model))
            .isInstanceOf(ScheduleProcessingFailure.class)
            .hasMessage(ErrorConstants.NO_TYPE_CODE);
  }

  @Test
  void testNoTeamReturned() {
    when(typeRepo.findByCode(anyString())).thenReturn(Optional.of(typeCode));

    when(teamRepo.findByUrl("WSH")).thenReturn(Optional.empty());
    when(teamRepo.findByUrl("OAK")).thenReturn(Optional.of(opponent));

    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.empty());

    var model = new ScheduleModel();
    model.setGameId(300494857L);
    model.setUrl("www.google.com");
    model.setHomeGame(false);
    model.setWeek(1);
    model.setYear(2022);
    model.setTeamUrl("WSH");
    model.setOpponentUrl("OAK");
    model.setTypeCode("R");

    var controller = new ScheduleController(scheduleRepo, teamRepo, typeRepo);
    assertThatThrownBy(() -> controller.post(model))
            .isInstanceOf(ScheduleProcessingFailure.class)
            .hasMessage(ErrorConstants.NO_TEAM_FOUND);
  }

  @Test
  void testNoOpponentReturned() {
    when(typeRepo.findByCode(anyString())).thenReturn(Optional.of(typeCode));

    when(teamRepo.findByUrl("WSH")).thenReturn(Optional.of(team));
    when(teamRepo.findByUrl("OAK")).thenReturn(Optional.empty());

    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.empty());

    var model = new ScheduleModel();
    model.setGameId(300494857L);
    model.setUrl("www.google.com");
    model.setHomeGame(false);
    model.setWeek(1);
    model.setYear(2022);
    model.setTeamUrl("WSH");
    model.setOpponentUrl("OAK");
    model.setTypeCode("R");

    var controller = new ScheduleController(scheduleRepo, teamRepo, typeRepo);
    assertThatThrownBy(() -> controller.post(model))
            .isInstanceOf(ScheduleProcessingFailure.class)
            .hasMessage(ErrorConstants.NO_TEAM_FOUND);
  }

  @Test
  void testScheduleExists() {
    when(typeRepo.findByCode(anyString())).thenReturn(Optional.of(typeCode));
    when(teamRepo.findByUrl("WSH")).thenReturn(Optional.of(team));
    when(teamRepo.findByUrl("OAK")).thenReturn(Optional.of(opponent));

    when(scheduleRepo.findByGameIdAndTeamId(anyLong(), anyInt())).thenReturn(Optional.of(schedule));

    var model = new ScheduleModel();
    model.setGameId(300494857L);
    model.setUrl("www.google.com");
    model.setHomeGame(false);
    model.setWeek(1);
    model.setYear(2022);
    model.setTeamUrl("WSH");
    model.setOpponentUrl("OAK");
    model.setTypeCode("R");

    var controller = new ScheduleController(scheduleRepo, teamRepo, typeRepo);
    controller.post(model);
    verify(scheduleRepo).save(captor.capture());

    assertThat(captor.getValue())
            .isNotNull()
            .extracting(Schedule::getId)
            .isEqualTo(25L);
  }
}
