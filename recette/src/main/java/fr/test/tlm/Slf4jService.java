package fr.test.tlm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jService {

  private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jService.class);

  public void run() {
    LOGGER.error("slf4j error 1");
    LOGGER.error("slf4j error 2");
    LOGGER.warn("slf4j warn 1");
    LOGGER.info("slf4j info 1");
    LOGGER.info("slf4j info 2");
    LOGGER.info("slf4j info 3");
    LOGGER.debug("slf4j debug 1");
    LOGGER.trace("slf4j trace 1");
  }
}
