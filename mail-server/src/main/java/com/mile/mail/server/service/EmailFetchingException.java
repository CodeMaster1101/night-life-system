package com.mile.mail.server.service;

class EmailFetchingException extends RuntimeException {

    EmailFetchingException(String msg, Throwable e) {
      super(msg, e);
    }

}
