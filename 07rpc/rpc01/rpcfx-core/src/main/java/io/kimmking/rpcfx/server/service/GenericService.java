package io.kimmking.rpcfx.server.service;

import java.lang.reflect.InvocationTargetException;

public interface GenericService {

    Object $invoke(String method, Object[] params) throws InvocationTargetException, IllegalAccessException;
}
