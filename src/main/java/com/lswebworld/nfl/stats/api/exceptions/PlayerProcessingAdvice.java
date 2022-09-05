package com.lswebworld.nfl.stats.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice Class for Player Processing Failure.
 */
@ControllerAdvice
class PlayerProcessingAdvice {

  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(PlayerProcessingFailure.class)
  String playerProcessingHandler(PlayerProcessingFailure failure) {
    return failure.getMessage();
  }



}
