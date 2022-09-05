package com.lswebworld.nfl.stats.api.repositories;

import com.lswebworld.nfl.stats.data.dataobjects.PositionCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Position Codes Repository.
 */
@Repository
public interface PositionCodeRepository extends JpaRepository<PositionCode, Integer> {

  /**
   * Retrieves a Position Code by the Code Value.
   *
   * @param code Position Code value.
   * @return Position Code
   */
  Optional<PositionCode> findByCode(String code);

}
