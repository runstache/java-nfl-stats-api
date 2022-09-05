package com.lswebworld.nfl.stats.api.exceptions;

/**
 * Schedule Processing Exception.
 */
public class ScheduleProcessingFailure extends RuntimeException {

  public ScheduleProcessingFailure(String message) {
    super(message);
  }

}
