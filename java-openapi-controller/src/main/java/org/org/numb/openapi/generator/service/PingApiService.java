package org.org.numb.openapi.generator.service;

import org.org.numb.openapi.generator.gen.delegate.PingApi;
import org.org.numb.openapi.generator.gen.model.SomeObj;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PingApiService implements PingApi {


    @Override
    public ResponseEntity<SomeObj> getPing(Long petId, String name, String status) {
        return PingApi.super.getPing(petId, name, status);
    }

    @Override
    public ResponseEntity<SomeObj> postPing(SomeObj someObj) {
        return PingApi.super.postPing(someObj);
    }
}
