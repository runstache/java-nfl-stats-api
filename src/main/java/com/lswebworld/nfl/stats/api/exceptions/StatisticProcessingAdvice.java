package com.lswebworld.nfl.stats.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Statistic Rest Api Advice.
 */
@ControllerAdvice
public class StatisticProcessingAdvice {

  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(StatisticProcessingFailure.class)
  String statisticProcessingHandler(StatisticProcessingFailure failure) {
    return failure.getMessage();
  }

}
