package fr.test.tlm;

// Logger maison, non reconnu par LoggerFramework.fromType() : sert a tester le repli
// sur le nom de variable conventionnel (classe LoggerFramework.UNKNOWN).
public class CustomLogger {
  public void error(String msg) { System.out.println("ERROR " + msg); }
  public void warn(String msg) { System.out.println("WARN " + msg); }
  public void info(String msg) { System.out.println("INFO " + msg); }
}
