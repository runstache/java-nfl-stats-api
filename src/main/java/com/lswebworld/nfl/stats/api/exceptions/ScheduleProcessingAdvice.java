package com.lswebworld.nfl.stats.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Schedule Processing Failure Advice.
 */
@ControllerAdvice
public class ScheduleProcessingAdvice {

  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ScheduleProcessingFailure.class)
  String scheduleProcessingHandler(ScheduleProcessingFailure failure) {
    return failure.getMessage();
  }

}
