package com.lswebworld.nfl.stats.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lswebworld.nfl.stats.api.constants.ErrorConstants;
import com.lswebworld.nfl.stats.api.exceptions.PlayerProcessingFailure;
import com.lswebworld.nfl.stats.api.repositories.PlayerRepository;
import com.lswebworld.nfl.stats.api.repositories.PositionCodeRepository;
import com.lswebworld.nfl.stats.data.dataobjects.Player;
import com.lswebworld.nfl.stats.data.dataobjects.PositionCode;
import com.lswebworld.nfl.stats.data.models.PlayerModel;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

class PlayerControllerTest {

  @Captor
  ArgumentCaptor<Player> captor;

  PositionCode code;

  private PlayerRepository playerRepo;
  private PositionCodeRepository positionRepo;

  @BeforeEach
  void setup() {
    playerRepo = mock(PlayerRepository.class);
    positionRepo = mock(PositionCodeRepository.class);

    code = new PositionCode();
    code.setId(1);
    code.setCode("QB");
    code.setDescription("Quarterback");

    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testConvertPlayer() {

    when(positionRepo.findByCode(anyString())).thenReturn(Optional.of(code));
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.empty());

    var controller = new PlayerController(playerRepo, positionRepo);

    var model = new PlayerModel();
    model.setName("Jim Smith");
    model.setUrl("www.google.com");
    model.setPositionCode("QB");

    controller.post(model);

    verify(playerRepo).save(captor.capture());

    assertThat(captor.getValue()).isNotNull();

    assertThat(captor.getValue())
            .extracting(Player::getName)
            .isNotNull()
            .isEqualTo("Jim Smith");
    assertThat(captor.getValue())
            .extracting(Player::getUrl)
            .isNotNull()
            .isEqualTo("www.google.com");
    assertThat(captor.getValue())
            .extracting(Player::getPositionId)
            .isNotNull()
            .isEqualTo(1);
  }

  @Test
  void testNoPositionReturned() {
    when(positionRepo.findByCode(anyString())).thenReturn(Optional.empty());
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.empty());
    var controller = new PlayerController(playerRepo, positionRepo);

    var model = new PlayerModel();
    model.setName("Jim Smith");
    model.setUrl("www.google.com");
    model.setPositionCode("QB");

    assertThatThrownBy(() -> controller.post(model))
            .isInstanceOf(PlayerProcessingFailure.class)
            .hasMessage(ErrorConstants.NO_POSITION_FOUND);

  }

  @Test
  void testPlayerExists() {
    when(positionRepo.findByCode(anyString())).thenReturn(Optional.of(code));

    var controller = new PlayerController(playerRepo, positionRepo);

    var model = new PlayerModel();
    model.setName("Jim Smith");
    model.setUrl("www.google.com");
    model.setPositionCode("QB");

    var response = new Player();
    response.setPositionId(1);
    response.setUrl("www.google.com");
    response.setName("Jim Smith");
    response.setId(25L);
    when(playerRepo.findByUrl(anyString())).thenReturn(Optional.of(response));
    controller.post(model);

    verify(playerRepo).save(captor.capture());

    assertThat(captor.getValue()).isNotNull();

    assertThat(captor.getValue())
            .extracting(Player::getName)
            .isNotNull()
            .isEqualTo("Jim Smith");
    assertThat(captor.getValue())
            .extracting(Player::getUrl)
            .isNotNull()
            .isEqualTo("www.google.com");
    assertThat(captor.getValue())
            .extracting(Player::getPositionId)
            .isNotNull()
            .isEqualTo(1);
    assertThat(captor.getValue())
            .extracting(Player::getId)
            .isEqualTo(25L);
  }
}
