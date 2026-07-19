package fr.test.tlm;

import java.util.logging.Logger;

public class JulService {

  private static final Logger LOGGER = Logger.getLogger(JulService.class.getName());

  // Note : java.util.logging.Logger n'a pas de méthodes error()/warn()/debug()/trace()
  // (severe/warning/fine/finer/finest a la place) : seul info() est détecté par le plugin.
  public void run() {
    LOGGER.info("jul info 1");
    LOGGER.info("jul info 2");
  }
}
