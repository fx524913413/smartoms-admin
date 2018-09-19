package com.zorkdata.center.common.salt.netapi.calls.modules;

import com.zorkdata.center.common.salt.netapi.calls.LocalCall;

import com.google.gson.reflect.TypeToken;

import java.util.Optional;

/**
 * salt.modules.timezone
 * <p>
 * https://docs.saltstack.com/en/latest/ref/modules/all/salt.modules.timezone.html
 */
public class Timezone {

    private Timezone() {
    }

    public static LocalCall<String> getOffset() {
        return new LocalCall<>("timezone.get_offset", Optional.empty(), Optional.empty(),
                new TypeToken<String>() {
                });
    }
}
