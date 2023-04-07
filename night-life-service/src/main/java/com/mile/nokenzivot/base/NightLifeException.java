package com.mile.nokenzivot.base;

import java.io.Serial;

class NightLifeException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;
  NightLifeException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
