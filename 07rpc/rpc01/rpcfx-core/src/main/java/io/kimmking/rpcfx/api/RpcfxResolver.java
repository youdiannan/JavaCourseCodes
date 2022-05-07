package io.kimmking.rpcfx.api;

import io.kimmking.rpcfx.server.service.GenericService;

public interface RpcfxResolver {

    Object resolve(String serviceClass);

    GenericService resolveGeneric(String serviceClass) throws ClassNotFoundException;
}
