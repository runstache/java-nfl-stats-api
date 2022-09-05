package com.lswebworld.nfl.stats.api.repositories;

import com.lswebworld.nfl.stats.data.dataobjects.TypeCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Type Code JPA Repository.
 */
@Repository
public interface TypeCodeRepository extends JpaRepository<TypeCode, Integer> {

  /**
   * Retrieves a Type Code by the Code Value.
   *
   * @param code Type Code value
   * @return TypeCode
   */
  Optional<TypeCode> findByCode(String code);
}
