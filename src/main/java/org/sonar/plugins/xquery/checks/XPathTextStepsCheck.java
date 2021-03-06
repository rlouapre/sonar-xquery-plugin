/*
 * © 2014 by Intellectual Reserve, Inc. All rights reserved.
 */

package org.sonar.plugins.xquery.checks;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.xquery.parser.XQueryParser;
import org.sonar.plugins.xquery.parser.XQueryTree;
import org.sonar.plugins.xquery.rules.CheckClasses;

/**
 * Checks for usage of text() XPath steps.
 * 
 * @since 1.0
 */
@Rule(
    key = XPathTextStepsCheck.RULE_KEY,
    name = "Avoid Using text() in XPath",
    description = "Generally avoid using /text() in your XPath in favor of using fn:string() or allowing atomization " +
            "(through strong typing or default atomization).",
    priority = Priority.MINOR
)
public class XPathTextStepsCheck extends AbstractPathCheck {

    public static final String RULE_KEY = "XpathTextSteps";
    private static final RuleKey RULE = RuleKey.of(CheckClasses.REPOSITORY_KEY, RULE_KEY);

    @Override
    public void enterExpression(XQueryTree node) {
        super.enterExpression(node);

        // Only do further checking on path expressions
        if (XQueryParser.PathExpr == node.getType()) {
            String expr = node.getValue();

            // text() function calls in path expressions are invalid
            if (StringUtils.contains(expr, "text ( )")) {
                // Only look for the node with value of "text" since the 
                // parenthesis tokens are currently separate nodes
                createViolations(RULE, node, "text");
            }            
        }
    }
}