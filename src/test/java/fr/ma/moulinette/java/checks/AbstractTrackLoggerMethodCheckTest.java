/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2026.
 *  Laurent HADJADJ <laurent_h@me.com>.
 *  Licensed Creative Common  CC-BY-NC-SA 4.0.
 *  ---
 *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
 *  http://creativecommons.org/licenses/by-nc-sa/4.0/
 */

package fr.ma.moulinette.java.checks;

import org.junit.jupiter.api.Test;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import java.util.List;

import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractTrackLoggerMethodCheckTest {

    @Test
    void testNodesToVisit() {
        AbstractTrackLoggerMethodCheck check = new AbstractTrackLoggerMethodCheck() {
            @Override
            public void visitNode(@Nonnull Tree tree) {
                // Cette méthode n'est pas testée directement ici
            }

            @Override
            protected String getMethodName() {
              // TO.DO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'getMethodName'");
            }

            @Override
            protected String getLoggerMessage() {
              // TO.DO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'getLoggerMessage'");
            }
        };

        List<Kind> nodes = check.nodesToVisit();
        assertThat(nodes)
          .isNotNull()
          .hasSize(1)
          .first()
          .isEqualTo(Kind.METHOD_INVOCATION);
    }

}
