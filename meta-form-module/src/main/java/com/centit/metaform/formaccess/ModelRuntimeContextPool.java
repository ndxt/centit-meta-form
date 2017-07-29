package com.centit.metaform.formaccess;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codefan on 17-7-6.
 */
public abstract class ModelRuntimeContextPool {
    private static ConcurrentHashMap<String, ModelRuntimeContext> runtimeContextPool
            = new ConcurrentHashMap<>();

    public static ModelRuntimeContext getRuntimeContextPool(String modelCode) {
        if (modelCode == null)
            return null;
        return runtimeContextPool.get(modelCode);
    }

    public static void registerRuntimeContextPool(ModelRuntimeContext runtimeContext) {
        runtimeContextPool.put(runtimeContext.getModelCode(),runtimeContext);
    }

    public static void invalidRuntimeContextPool(String modelCode) {
        runtimeContextPool.remove(modelCode);
    }
}
