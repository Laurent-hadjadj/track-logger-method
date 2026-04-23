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
