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

package org.eclipse.dataspaceconnector.provision.azure.blob;

import net.jodah.failsafe.RetryPolicy;
import org.eclipse.dataspaceconnector.azure.testfixtures.AbstractAzureBlobTest;
import org.eclipse.dataspaceconnector.common.annotations.IntegrationTest;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApiImpl;
import org.eclipse.dataspaceconnector.common.testfixtures.TestUtils;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.eclipse.dataspaceconnector.common.configuration.ConfigurationFunctions.propOrEnv;

@IntegrationTest
class ObjectContainerStatusCheckerIntegrationTest extends AbstractAzureBlobTest {

    private File helloTxt;
    private ObjectContainerStatusChecker checker;

    @BeforeEach
    void setUp() {
        var policy = new RetryPolicy<>().withMaxRetries(1);
        helloTxt = TestUtils.getFileFromResourceName("hello.txt");
        Vault vault = mock(Vault.class);
        var accountKey = propOrEnv("AZ_STORAGE_KEY", null);
        assertThat(accountKey).describedAs("Azure Storage Account Key cannot be null!").isNotNull();

        expect(vault.resolveSecret(ACCOUNT_NAME + "-key1")).andReturn(accountKey).anyTimes();
        replay(vault);
        var blobStoreApi = new BlobStoreApiImpl(vault);
        checker = new ObjectContainerStatusChecker(blobStoreApi, policy);
    }

    @Test
    void isComplete_noResources() {
        putBlob("hello.txt", helloTxt);
        putBlob(testRunId + ".complete", helloTxt);

        var tp = createTransferProcess(containerName);
        assertThat(checker.isComplete(tp, emptyList())).isTrue();
    }

    @Test
    void isComplete_noResources_notComplete() {
        putBlob("hello.txt", helloTxt);

        var tp = createTransferProcess(containerName);
        assertThat(checker.isComplete(tp, emptyList())).isFalse();
    }

    @Test
    void isComplete_noResources_containerNotExist() {
        var tp = createTransferProcess(containerName);
        assertThat(checker.isComplete(tp, emptyList())).isFalse();
    }

    @Test
    void isComplete_withResources() {
        putBlob("hello.txt", helloTxt);
        putBlob(testRunId + ".complete", helloTxt);

        var tp = createTransferProcess(containerName);
        var pr = createProvisionedResource(tp);
        assertThat(checker.isComplete(tp, singletonList(pr))).isTrue();
    }

    @Test
    void isComplete_withResources_notComplete() {
        putBlob("hello.txt", helloTxt);

        var tp = createTransferProcess(containerName);
        var pr = createProvisionedResource(tp);
        assertThat(checker.isComplete(tp, singletonList(pr))).isFalse();
    }

    @Test
    void isComplete_withResources_containerNotExist() {
        var tp = createTransferProcess(containerName);
        var pr = createProvisionedResource(tp);
        assertThat(checker.isComplete(tp, singletonList(pr))).isFalse();
    }

    private TransferProcess createTransferProcess(String containerName) {
        return TransferProcess.Builder.newInstance()
                .id(UUID.randomUUID().toString())
                .dataRequest(DataRequest.Builder.newInstance()
                        .destinationType(AzureBlobStoreSchema.TYPE)
                        .dataDestination(DataAddress.Builder.newInstance()
                                .type(AzureBlobStoreSchema.TYPE)
                                .property(AzureBlobStoreSchema.CONTAINER_NAME, containerName)
                                .property(AzureBlobStoreSchema.ACCOUNT_NAME, AbstractAzureBlobTest.ACCOUNT_NAME)
                                //.property(AzureBlobStoreSchema.BLOB_NAME, ???) omitted on purpose
                                .build())
                        .build())
                .build();
    }

    private ObjectContainerProvisionedResource createProvisionedResource(TransferProcess tp) {
        return ObjectContainerProvisionedResource.Builder.newInstance()
                .containerName(containerName)
                .accountName(ACCOUNT_NAME)
                .resourceDefinitionId(UUID.randomUUID().toString())
                .transferProcessId(tp.getId())
                .id(UUID.randomUUID().toString())
                .build();
    }
}
