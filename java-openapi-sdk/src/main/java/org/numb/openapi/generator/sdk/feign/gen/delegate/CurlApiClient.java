package org.numb.openapi.generator.sdk.feign.gen.delegate;

import org.springframework.cloud.openfeign.FeignClient;
import org.openapitools.configuration.ClientConfiguration;

@FeignClient(name="${curl.name:curl}", contextId="${curl.contextId:${curl.name}}", url="${curl.url:http://localhost:8082}", configuration = ClientConfiguration.class)
public interface CurlApiClient extends CurlApi {
}
