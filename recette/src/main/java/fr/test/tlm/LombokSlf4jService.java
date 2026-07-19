package fr.test.tlm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LombokSlf4jService {

  public void run() {
    log.error("lombok slf4j error 1");
    log.warn("lombok slf4j warn 1");
    log.warn("lombok slf4j warn 2");
    log.info("lombok slf4j info 1");
    log.debug("lombok slf4j debug 1");
    log.debug("lombok slf4j debug 2");
    log.debug("lombok slf4j debug 3");
    log.trace("lombok slf4j trace 1");
  }
}
