package com.swiggy.order.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proto.DeliveryAgent;
import proto.DeliveryAgentServiceGrpc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FulfillmentProxyServiceTest {

    private DeliveryAgentServiceGrpc.DeliveryAgentServiceBlockingStub deliveryAgentServiceStub;
    private FulfillmentProxyService fulfillmentProxyService;

    @BeforeEach
    void setUp() {
        deliveryAgentServiceStub = mock(DeliveryAgentServiceGrpc.DeliveryAgentServiceBlockingStub.class);
        fulfillmentProxyService = new FulfillmentProxyService(deliveryAgentServiceStub);
    }

    @Test
    void assignDeliveryAgentSuccessfully() {
        DeliveryAgent.AssignDeliveryAgentRequest request = DeliveryAgent.AssignDeliveryAgentRequest.newBuilder().setUserId(1).setOrderId(1).build();
        DeliveryAgent.AssignDeliveryAgentResponse response = DeliveryAgent.AssignDeliveryAgentResponse.newBuilder().setResponse("Success").build();
        when(deliveryAgentServiceStub.assignDeliveryAgent(request)).thenReturn(response);

        fulfillmentProxyService.assignDeliveryAgent(1L, 1L);

        verify(deliveryAgentServiceStub, times(1)).assignDeliveryAgent(request);
    }

    @Test
    void assignDeliveryAgentThrowsExceptionOnGrpcError() {
        DeliveryAgent.AssignDeliveryAgentRequest request = DeliveryAgent.AssignDeliveryAgentRequest.newBuilder().setUserId(1).setOrderId(1).build();
        when(deliveryAgentServiceStub.assignDeliveryAgent(request)).thenThrow(new RuntimeException("gRPC error"));

        assertThrows(RuntimeException.class, () -> fulfillmentProxyService.assignDeliveryAgent(1L, 1L));
    }
}