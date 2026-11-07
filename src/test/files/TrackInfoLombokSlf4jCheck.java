import lombok.extern.slf4j.Slf4j;

@Slf4j
class ExempleService {

  public void methodeA() {
    log.info("Entrée methodeA"); // Noncompliant {{Utilisation du Logger en mode : info [Lombok @Slf4j]}}
    log.info("Sortie methodeA — detail={}", "x"); // Noncompliant {{Utilisation du Logger en mode : info [Lombok @Slf4j]}}
  }

  /** Javadoc methodeB. */
  public void methodeB() {
    log.info("Entrée methodeB"); // Noncompliant {{Utilisation du Logger en mode : info [Lombok @Slf4j]}}
  }

  /** Javadoc methodeC. */
  public void methodeC() {
    log.info("Entrée methodeC"); // Noncompliant {{Utilisation du Logger en mode : info [Lombok @Slf4j]}}
  }

  /** Javadoc methodeD. */
  public void methodeD() {
    log.info("Entrée methodeD"); // Noncompliant {{Utilisation du Logger en mode : info [Lombok @Slf4j]}}
  }

  private void methodeInterneSansLog() {
    // aucun appel logger ici
  }
}
