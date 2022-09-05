package com.lswebworld.nfl.stats.api.exceptions;

/**
 * Failure Exception for processing Statistic Entries.
 */
public class StatisticProcessingFailure extends RuntimeException {

  public StatisticProcessingFailure(String message) {
    super(message);
  }
}
