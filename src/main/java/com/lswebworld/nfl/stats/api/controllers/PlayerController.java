package com.lswebworld.nfl.stats.api.controllers;

import com.lswebworld.nfl.stats.api.constants.ErrorConstants;
import com.lswebworld.nfl.stats.api.exceptions.PlayerProcessingFailure;
import com.lswebworld.nfl.stats.api.repositories.PlayerRepository;
import com.lswebworld.nfl.stats.api.repositories.PositionCodeRepository;
import com.lswebworld.nfl.stats.data.dataobjects.Player;
import com.lswebworld.nfl.stats.data.models.PlayerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Player Rest Controller.
 */

@RestController
public class PlayerController {

  private final PlayerRepository playerRepo;
  private final PositionCodeRepository positionRepo;

  @Autowired
  public PlayerController(PlayerRepository playerRepo, PositionCodeRepository positionRepo) {
    this.playerRepo = playerRepo;
    this.positionRepo = positionRepo;
  }

  /**
   * Post Methode for storing players.
   *
   * @param model Player Model.
   */

  @PostMapping("/api/player")
  public void post(@RequestBody PlayerModel model) {

    var position = positionRepo.findByCode(model.getPositionCode());

    if (position.isPresent()) {

      var player = new Player();
      player.setDob(model.getDob());
      player.setName(model.getName());
      player.setUrl(model.getUrl());
      player.setPositionId(position.get().getId());

      hydratePlayer(player);

      playerRepo.save(player);

    } else {
      throw new PlayerProcessingFailure(ErrorConstants.NO_POSITION_FOUND);
    }

  }

  /**
   * Retrieves the Id from the Database if it exists.
   *
   * @param player Player
   */
  private void hydratePlayer(Player player) {
    var response = playerRepo.findByUrl(player.getUrl());
    response.ifPresent(value -> player.setId(value.getId()));
  }

}
