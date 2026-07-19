package fr.test.tlm;

public class UnknownFallbackService {

  private static final CustomLogger LOGGER = new CustomLogger();

  public void run() {
    LOGGER.error("unknown error 1");
    LOGGER.warn("unknown warn 1");
    LOGGER.info("unknown info 1");
  }
}
