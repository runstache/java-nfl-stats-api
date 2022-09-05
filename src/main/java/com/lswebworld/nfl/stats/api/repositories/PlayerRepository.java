package com.lswebworld.nfl.stats.api.repositories;

import com.lswebworld.nfl.stats.data.dataobjects.Player;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Player JPA Repository definition.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

  /**
   * Retrieves a Player by Url value.
   *
   * @param url Url
   * @return Player
   */
  Optional<Player> findByUrl(String url);

}
