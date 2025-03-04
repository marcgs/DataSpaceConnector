/*
 *  Copyright (c) 2020, 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.dataspaceconnector.transfer.core.transfer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExponentialWaitStrategyTest {

    @Test
    void verifyBackoff() {
        ExponentialWaitStrategy strategy = new ExponentialWaitStrategy(5000);

        assertEquals(5000, strategy.retryInMillis());
        assertEquals(10000, strategy.retryInMillis());

        strategy.success(); // reset

        assertEquals(5000, strategy.retryInMillis());

    }
}
