package org.numb.openapi.generator.sdk.feign.gen.delegate;

import org.springframework.cloud.openfeign.FeignClient;
import org.openapitools.configuration.ClientConfiguration;

@FeignClient(name="${ping.name:ping}", contextId="${ping.contextId:${ping.name}}", url="${ping.url:http://localhost:8082}", configuration = ClientConfiguration.class)
public interface PingApiClient extends PingApi {
}
