package com.lswebworld.nfl.stats.api.repositories;

import com.lswebworld.nfl.stats.data.dataobjects.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Team Repository.
 */

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

  /**
   * Retrieves the Team by Code.
   *
   * @param code Team Code
   * @return Team
   */
  Optional<Team> findByCode(String code);

  /**
   * Retrieves a Team by Url.
   *
   * @param url Url
   * @return Team
   */
  Optional<Team> findByUrl(String url);

}
