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
*       Daimler TSS GmbH - Initial API and Implementation
*
*/

val slf4jVersion: String by project

plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    api(project(":spi"))
    api("org.slf4j:slf4j-api:${slf4jVersion}")
    implementation(project(":core:policy:policy-engine"))
}

publishing {
    publications {
        create<MavenPublication>("core.contract") {
            artifactId = "core.contract"
            from(components["java"])
        }
    }
}

