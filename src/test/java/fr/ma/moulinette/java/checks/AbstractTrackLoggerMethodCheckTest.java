package fr.ma.moulinette.java.checks;

import org.junit.jupiter.api.Test;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

class AbstractTrackLoggerMethodCheckTest {

    @Test
    void testNodesToVisit() {
        AbstractTrackLoggerMethodCheck check = new AbstractTrackLoggerMethodCheck() {
            @Override
            public void visitNode(Tree tree) {
                // Cette méthode n'est pas testée directement ici
            }

            @Override
            protected String getMethodName() {
              // TODO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'getMethodName'");
            }

            @Override
            protected String getLoggerMessage() {
              // TODO Auto-generated method stub
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
