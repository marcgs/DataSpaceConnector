/*
 *  Copyright (c) 2021 Daimler TSS GmbH
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Daimler TSS GmbH - Initial Implementation
 *
 */

package org.eclipse.dataspaceconnector.ids.transform;

import de.fraunhofer.iais.eis.BinaryOperator;
import de.fraunhofer.iais.eis.LeftOperand;
import de.fraunhofer.iais.eis.util.RdfResource;
import org.eclipse.dataspaceconnector.ids.spi.transform.IdsTypeTransformer;
import org.eclipse.dataspaceconnector.ids.spi.transform.TransformerContext;
import org.eclipse.dataspaceconnector.policy.model.AtomicConstraint;
import org.eclipse.dataspaceconnector.policy.model.Constraint;
import org.eclipse.dataspaceconnector.policy.model.Expression;
import org.eclipse.dataspaceconnector.policy.model.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IdsConstraintToConstraintTransformer implements IdsTypeTransformer<de.fraunhofer.iais.eis.Constraint, Constraint> {

    @Override
    public Class<de.fraunhofer.iais.eis.Constraint> getInputType() {
        return de.fraunhofer.iais.eis.Constraint.class;
    }

    @Override
    public Class<Constraint> getOutputType() {
        return Constraint.class;
    }

    @Override
    public @Nullable Constraint transform(de.fraunhofer.iais.eis.Constraint object, @NotNull TransformerContext context) {
        Objects.requireNonNull(context);
        if (object == null) {
            return null;
        }

        Operator operator = null;
        BinaryOperator binaryOperator = object.getOperator();
        if (binaryOperator != null) {
            operator = context.transform(binaryOperator, Operator.class);
        }

        if (operator == null) {
            context.reportProblem("Cannot transform constraint: Operator is missing");

            // no operator, no need to proceed
            return null;
        }

        Expression leftExpression = null;
        LeftOperand leftOperand = object.getLeftOperand();
        if (leftOperand != null) {
            leftExpression = context.transform(leftOperand, Expression.class);
        }

        if (leftExpression == null) {
            context.reportProblem("Cannot transform constraint: LeftExpression is missing");
            return null;
        }


        Expression rightExpression = null;
        RdfResource rightOperand = object.getRightOperand();
        if (rightOperand != null) {
            rightExpression = context.transform(rightOperand, Expression.class);
        }

        if (rightExpression == null) {
            context.reportProblem("Cannot transform constraint: RightExpression is missing");
            return null;
        }

        return AtomicConstraint.Builder.newInstance()
                .leftExpression(leftExpression)
                .operator(operator)
                .rightExpression(rightExpression)
                .build();
    }
}
