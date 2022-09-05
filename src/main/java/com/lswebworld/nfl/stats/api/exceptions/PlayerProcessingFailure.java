package com.lswebworld.nfl.stats.api.exceptions;

/**
 * Exception thrown for Player processing failures.
 */
public class PlayerProcessingFailure extends RuntimeException {

  public PlayerProcessingFailure(String message) {
    super(message);
  }

}
