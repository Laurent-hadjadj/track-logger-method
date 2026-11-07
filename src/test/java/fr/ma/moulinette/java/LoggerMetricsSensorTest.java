/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2026.
 *  Laurent HADJADJ <laurent_h@me.com>.
 *  SPDX-License-Identifier: LGPL-3.0-or-later
 *  Voir le fichier LICENSE à la racine du dépôt.
 */
package fr.ma.moulinette.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockingDetails;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.measure.NewMeasure;
import org.sonar.api.scanner.fs.InputProject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Vérifie le fix du bug des ratios :
 * les ratios doivent toujours être publiés (même à 0), et le sensor ne doit
 * publier les métriques qu'une seule fois par analyse, quel que soit le
 * nombre d'invocations de {@code execute()}.
 */
class LoggerMetricsSensorTest {

  private LoggerMetricsSensor sensor;
  private SensorContext context;
  @SuppressWarnings("rawtypes")
  private NewMeasure newMeasure;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @BeforeEach
  void setUp() {
    LoggerCounter.reset();
    LoggerCounter loggerCounter = new LoggerCounter();
    sensor = new LoggerMetricsSensor(loggerCounter);

    context = mock(SensorContext.class);
    InputProject project = mock(InputProject.class);
    when(context.project()).thenReturn(project);

    newMeasure = mock(NewMeasure.class);
    when(context.newMeasure()).thenReturn(newMeasure);
    when(newMeasure.forMetric(any())).thenReturn(newMeasure);
    when(newMeasure.on(any())).thenReturn(newMeasure);
    when(newMeasure.withValue(any())).thenReturn(newMeasure);
  }

  @AfterEach
  void tearDown() {
    LoggerCounter.reset();
  }

  @Test
  void testPublishesRatioForEveryFrameworkEvenWhenGrandTotalIsZero() {
    sensor.execute(context);

    // Avant le fix, publishRatios() retournait immédiatement si grandTotal == 0 :
    // aucune métrique de ratio n'était jamais sauvegardée pour une analyse sans
    // appel logger détecté. SLF4J (premier) et Unknown (dernier) de l'énumération
    // suffisent à couvrir toute la boucle.
    verify(newMeasure).forMetric(LoggerMetrics.LOGGER_SLF4J_RATIO);
    verify(newMeasure).forMetric(LoggerMetrics.LOGGER_UNKNOWN_RATIO);
  }

  @Test
  void testSecondExecutionDoesNotRepublishMetrics() {
    sensor.execute(context);
    MockingDetails detailsAfterFirst = mockingDetails(newMeasure);
    int invocationsAfterFirst = detailsAfterFirst.getInvocations().size();
    assertThat(invocationsAfterFirst).isGreaterThan(0);

    sensor.execute(context);
    int invocationsAfterSecond = mockingDetails(newMeasure).getInvocations().size();

    assertThat(invocationsAfterSecond)
        .as("le second execute() ne doit déclencher aucune sauvegarde supplémentaire")
        .isEqualTo(invocationsAfterFirst);
  }
}
