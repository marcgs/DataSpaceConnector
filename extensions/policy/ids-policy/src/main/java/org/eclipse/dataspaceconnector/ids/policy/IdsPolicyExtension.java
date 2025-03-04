/*
 *  Copyright (c) 2021 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.dataspaceconnector.ids.policy;

import org.eclipse.dataspaceconnector.policy.model.Permission;
import org.eclipse.dataspaceconnector.spi.contract.policy.PolicyEngine;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;

import java.util.Set;

/**
 * Registers test policy functions.
 */
public class IdsPolicyExtension implements ServiceExtension {

    public static String ABS_SPATIAL_POSITION = "ids:absoluteSpatialPosition";
    public static String PARTNER_LEVEL = "ids:partnerLevel";

    @Override
    public Set<String> requires() {
        return Set.of("edc:ids:core");
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var monitor = context.getMonitor();

        var policyEngine = context.getService(PolicyEngine.class);

        policyEngine.registerFunction(Permission.class, ABS_SPATIAL_POSITION, new AbsSpatialPositionConstraintFunction());
        policyEngine.registerFunction(Permission.class, PARTNER_LEVEL, new PartnerLevelConstraintFunction());

        monitor.info("Initialized IDS Mock Policy extension");
    }

}
