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

package org.eclipse.dataspaceconnector.spi.types.domain.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.eclipse.dataspaceconnector.policy.model.Policy;
import org.eclipse.dataspaceconnector.spi.types.domain.asset.Asset;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@link ContractAgreement} to regulate data transfer between two parties.
 */
@JsonDeserialize(builder = ContractAgreement.Builder.class)
public class ContractAgreement {

    private final String id;
    private final String providerAgentId;
    private final String consumerAgentId;
    private final long contractSigningDate;
    private final long contractStartDate;
    private final long contractEndDate;
    private final List<String> assetIds;
    private final Policy policy;

    private ContractAgreement(@NotNull String id,
                              @NotNull String providerAgentId,
                              @NotNull String consumerAgentId,
                              long contractSigningDate,
                              long contractStartDate,
                              long contractEndDate,
                              @NotNull List<String> assetIds,
                              @NotNull Policy policy) {
        this.id = Objects.requireNonNull(id);
        this.providerAgentId = Objects.requireNonNull(providerAgentId);
        this.consumerAgentId = Objects.requireNonNull(consumerAgentId);
        this.contractSigningDate = contractSigningDate;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.assetIds = Objects.requireNonNull(assetIds);
        this.policy = Objects.requireNonNull(policy);

        if (contractSigningDate == 0) {
            throw new IllegalArgumentException("contract signing date must be set");
        }
        if (contractStartDate == 0) {
            throw new IllegalArgumentException("contract start date must be set");
        }
        if (contractEndDate == 0) {
            throw new IllegalArgumentException("contract end date must be set");
        }
    }

    /**
     * Unique identifier of the {@link ContractAgreement}.
     *
     * @return contract id
     */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * The id of the data providing agent.
     * Please note that id should be taken from the corresponding data ecosystem.
     * For example: In IDS the connector uses a URI from the IDS Information Model as ID. If the contract was
     * negotiated inside the IDS ecosystem, this URI should be used here.
     *
     * @return provider id
     */
    @NotNull
    public String getProviderAgentId() {
        return providerAgentId;
    }

    /**
     * The id of the data consuming agent.
     * Please note that id should be taken from the corresponding contract ecosystem.
     * For example: In IDS the connector uses a URI from the IDS Information Model as ID. If the contract was
     * negotiated inside the IDS ecosystem, this URI should be used here.
     *
     * @return consumer id
     */
    @NotNull
    public String getConsumerAgentId() {
        return consumerAgentId;
    }

    /**
     * The date when the {@link ContractAgreement} has been signed. <br>
     * Numeric value representing the number of seconds from
     * 1970-01-01T00:00:00Z UTC until the specified UTC date/time.
     *
     * @return contract signing date
     */
    public long getContractSigningDate() {
        return contractSigningDate;
    }

    /**
     * The date from when the {@link ContractAgreement} is valid. <br>
     * Numeric value representing the number of seconds from
     * 1970-01-01T00:00:00Z UTC until the specified UTC date/time.
     *
     * @return contract start date
     */
    public long getContractStartDate() {
        return contractStartDate;
    }

    /**
     * The date until the {@link ContractAgreement} remains valid. <br>
     * Numeric value representing the number of seconds from
     * 1970-01-01T00:00:00Z UTC until the specified UTC date/time.
     *
     * @return contract end date
     */
    public long getContractEndDate() {
        return contractEndDate;
    }

    /**
     * List of asset identifier that are covered by the {@link ContractAgreement}.
     *
     * @return list of assets
     */
    @NotNull
    public List<String> getAssetIds() {
        return assetIds;
    }

    /**
     * A policy describing how the {@link Asset} of this contract may be used by the consumer.
     *
     * @return policy
     */
    @NotNull
    public Policy getPolicy() {
        return policy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, providerAgentId, consumerAgentId, contractSigningDate, contractStartDate, contractEndDate, assetIds, policy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractAgreement that = (ContractAgreement) o;
        return contractSigningDate == that.contractSigningDate && contractStartDate == that.contractStartDate && contractEndDate == that.contractEndDate &&
                Objects.equals(id, that.id) && Objects.equals(providerAgentId, that.providerAgentId) && Objects.equals(consumerAgentId, that.consumerAgentId) &&
                Objects.equals(assetIds, that.assetIds) && Objects.equals(policy, that.policy);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String id;
        private String providerAgentId;
        private String consumerAgentId;
        private long contractSigningDate;
        private long contractStartDate;
        private long contractEndDate;
        private List<String> assetIds = Collections.emptyList();
        private Policy policy;

        private Builder() {
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder providerAgentId(String providerAgentId) {
            this.providerAgentId = providerAgentId;
            return this;
        }

        public Builder consumerAgentId(String consumerAgentId) {
            this.consumerAgentId = consumerAgentId;
            return this;
        }

        public Builder contractSigningDate(long contractSigningDate) {
            this.contractSigningDate = contractSigningDate;
            return this;
        }

        public Builder contractStartDate(long contractStartDate) {
            this.contractStartDate = contractStartDate;
            return this;
        }

        public Builder contractEndDate(long contractEndDate) {
            this.contractEndDate = contractEndDate;
            return this;
        }

        public Builder assetIds(List<String> assetIds) {
            this.assetIds = assetIds;
            return this;
        }

        public Builder policy(Policy policy) {
            this.policy = policy;
            return this;
        }

        public ContractAgreement build() {
            return new ContractAgreement(id, providerAgentId, consumerAgentId, contractSigningDate, contractStartDate, contractEndDate, assetIds, policy);
        }

    }
}
