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

plugins {
    `java-library`
}


dependencies {
    api(project(":spi"))
    api(project(":data-protocols:ids:ids-spi"))
    api(project(":data-protocols:ids:ids-core"))
    api(project(":data-protocols:ids:ids-api-catalog"))
    api(project(":data-protocols:ids:ids-api-transfer"))
    api(project(":data-protocols:ids:ids-policy-mock"))
    api(project(":data-protocols:ids:ids-transform-v1"))
    api(project(":data-protocols:ids:ids-api-multipart-endpoint-v1"))
    api(project(":data-protocols:ids:ids-api-multipart-dispatcher-v1"))
}

publishing {
    publications {
        create<MavenPublication>("data-protocols.ids") {
            artifactId = "data-protocols.ids"
            from(components["java"])
        }
    }
}
