package com.lswebworld.nfl.stats.api.repositories;

import com.lswebworld.nfl.stats.data.dataobjects.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Statistics Repository.
 */
@Repository
public interface StatisticsRepository extends JpaRepository<Statistic, Long> {

}
