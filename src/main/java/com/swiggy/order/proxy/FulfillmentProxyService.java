package com.swiggy.order.proxy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proto.DeliveryAgent;
import proto.DeliveryAgentServiceGrpc;

@Service
@RequiredArgsConstructor
public class FulfillmentProxyService {

    private final DeliveryAgentServiceGrpc.DeliveryAgentServiceBlockingStub deliveryAgentServiceBlockingStub;

    public void assignDeliveryAgent(Long userId, Long orderId) {
        DeliveryAgent.AssignDeliveryAgentRequest request = DeliveryAgent.AssignDeliveryAgentRequest.newBuilder()
                .setUserId(Math.toIntExact(userId))
                .setOrderId(Math.toIntExact(orderId))
                .build();

        DeliveryAgent.AssignDeliveryAgentResponse response = deliveryAgentServiceBlockingStub.assignDeliveryAgent(request);

        response.getResponse();
    }
}
