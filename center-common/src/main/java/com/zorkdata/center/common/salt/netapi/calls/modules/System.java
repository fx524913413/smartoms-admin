package com.zorkdata.center.common.salt.netapi.calls.modules;

import com.zorkdata.center.common.salt.netapi.calls.LocalCall;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * salt.modules.system
 * <p>
 * https://docs.saltstack.com/en/latest/ref/modules/all/salt.modules.system.html
 */
public class System {

    private System() {
    }

    public static LocalCall<String> reboot(Optional<Integer> at_time) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        at_time.ifPresent(t -> {
            args.put("at_time", t);
        });
        return new LocalCall<>("system.reboot", Optional.empty(), Optional.of(args),
                new TypeToken<String>() {
                });
    }
}
