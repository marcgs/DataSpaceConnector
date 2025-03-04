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
    id("application")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

val jupiterVersion: String by project

dependencies {
    implementation(project(":core:bootstrap"))
    implementation(project(":extensions:dataloading:dataloading-asset"))
    implementation(project(":extensions:dataloading:dataloading-contractdef"))

    // the following lines enable the CosmosDB-based AssetIndex
    implementation(project(":extensions:azure:assetindex-cosmos"))
    implementation(project(":extensions:filesystem:configuration-fs"))
    implementation(project(":extensions:azure:vault"))

    // alternatively uncomment the following line to use the in-memory AssetIndex
    //
    // implementation(project(":extensions:in-memory:assetindex-memory"))

    // the following line enables the Cosmos-DB based ContractDefinitionStore
    implementation(project(":extensions:azure:contract-definition-store-cosmos"))


    // lightweight lib for CLI args
    implementation("info.picocli:picocli:4.6.2")

    testImplementation(testFixtures(project(":common:util")))
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "org.eclipse.dataspaceconnector.dataloader.cli.DataLoaderRuntime"
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm")
    mergeServiceFiles()
    archiveFileName.set("dataloader.jar")
}
