package com.unloadhome.internal;

import com.unloadhome.dubbointerface.IdResponse;

public interface IDgen {
    IdResponse get(String key);
    boolean init();
}
