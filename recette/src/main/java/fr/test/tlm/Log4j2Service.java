package fr.test.tlm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Service {

  private static final Logger LOGGER = LogManager.getLogger(Log4j2Service.class);

  public void run() {
    LOGGER.error("log4j2 error 1");
    LOGGER.warn("log4j2 warn 1");
    LOGGER.info("log4j2 info 1");
    LOGGER.info("log4j2 info 2");
    LOGGER.debug("log4j2 debug 1");
    LOGGER.trace("log4j2 trace 1");
    LOGGER.trace("log4j2 trace 2");
  }
}
