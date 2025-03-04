package org.eclipse.dataspaceconnector.contract.definition.store;

import org.eclipse.dataspaceconnector.contract.definition.store.model.ContractNegotiationDocument;
import org.eclipse.dataspaceconnector.policy.model.Policy;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.ContractAgreement;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.negotiation.ContractNegotiation;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.negotiation.ContractNegotiationStates;

import java.time.LocalDate;
import java.util.UUID;

public class TestFunctions {

    public static ContractNegotiation generateNegotiation() {
        return generateNegotiation(ContractNegotiationStates.UNSAVED);
    }

    public static ContractNegotiation generateNegotiation(ContractNegotiationStates state) {
        return ContractNegotiation.Builder.newInstance()
                .id(UUID.randomUUID().toString())
                .correlationId(UUID.randomUUID().toString())
                .counterPartyId("test-counterparty-1")
                .protocol("test-protocol")
                .stateCount(1)
                .contractAgreement(ContractAgreement.Builder.newInstance().id("1")
                        .providerAgentId("provider")
                        .consumerAgentId("consumer")
                        .policy(Policy.Builder.newInstance().build())
                        .contractSigningDate(LocalDate.MIN.toEpochDay())
                        .contractStartDate(LocalDate.MIN.toEpochDay())
                        .contractEndDate(LocalDate.MAX.toEpochDay())
                        .id("1:2").build())
                .state(state.code())
                .build();
    }

    public static ContractNegotiationDocument generateDocument() {
        return new ContractNegotiationDocument(generateNegotiation());
    }
}
