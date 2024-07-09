/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2024.
 *  Laurent HADJADJ <laurent_h@me.com>.
 *  Licensed Creative Common  CC-BY-NC-SA 4.0.
 *  ---
 *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
 *  http://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package fr.ma.moulinette.java;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MyJavaFileCheckRegistrarTest {

    @Test
    void testRegister() {
        // Create an instance of MyJavaFileCheckRegistrar
        MyJavaFileCheckRegistrar registrar = new MyJavaFileCheckRegistrar();

        // Mock the RegistrarContext
        CheckRegistrar.RegistrarContext registrarContext = mock(CheckRegistrar.RegistrarContext.class);

        // Call the register method of MyJavaFileCheckRegistrar
        registrar.register(registrarContext);

        // Capture the arguments passed to registerClassesForRepository
        ArgumentCaptor<List<Class<? extends JavaCheck>>> mainChecksCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<Class<? extends JavaCheck>>> testChecksCaptor = ArgumentCaptor.forClass(List.class);

        // Verify that registerClassesForRepository was called with the expected arguments
        verify(registrarContext).registerClassesForRepository(
                eq(MyJavaRulesDefinition.REPOSITORY_KEY), // Use eq() matcher for String comparison
                mainChecksCaptor.capture(),
                testChecksCaptor.capture());

        // Assert that the captured values are as expected
        assertThat(mainChecksCaptor.getValue()).isEqualTo(RulesList.getJavaChecks());
        assertThat(testChecksCaptor.getValue()).isEqualTo(RulesList.getJavaTestChecks());
    }

}
