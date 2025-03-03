package com.swiggy.order.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proto.DeliveryAgentServiceGrpc;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
    }

    @Bean
    public DeliveryAgentServiceGrpc.DeliveryAgentServiceBlockingStub deliveryAgentServiceBlockingStub(ManagedChannel grpcChannel) {
        return DeliveryAgentServiceGrpc.newBlockingStub(grpcChannel);
    }
}
