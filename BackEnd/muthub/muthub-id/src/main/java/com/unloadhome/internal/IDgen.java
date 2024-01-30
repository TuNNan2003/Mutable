package com.unloadhome.internal;

import com.unloadhome.common.*;
public interface IDgen {
    Response get(String key);
    boolean init();
}
