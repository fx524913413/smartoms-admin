package com.zorkdata.center.common.salt.netapi.examples;

import com.zorkdata.center.common.salt.netapi.AuthModule;
import com.zorkdata.center.common.salt.netapi.calls.modules.Grains;
import com.zorkdata.center.common.salt.netapi.calls.modules.Test;
import com.zorkdata.center.common.salt.netapi.client.SaltClient;
import com.zorkdata.center.common.salt.netapi.datatypes.target.Glob;
import com.zorkdata.center.common.salt.netapi.datatypes.target.MinionList;
import com.zorkdata.center.common.salt.netapi.datatypes.target.Target;
import com.zorkdata.center.common.salt.netapi.exception.SaltException;
import com.zorkdata.center.common.salt.netapi.results.Result;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Example code calling salt modules using the generic interface.
 */
public class Calls {

    private static final String SALT_API_URL = "http://localhost:8000";
    private static final String USER = "saltdev";
    private static final String PASSWORD = "saltdev";

    public static void main(String[] args) throws SaltException {
        // Init the client
        SaltClient client = new SaltClient(URI.create(SALT_API_URL));

        // Ping all minions using a glob matcher
        Target<String> globTarget = new Glob("*");
        Map<String, Result<Boolean>> results = Test.ping().callSync(
                client, globTarget, USER, PASSWORD, AuthModule.AUTO);

        System.out.println("--> Ping results:\n");
        results.forEach((minion, result) -> System.out.println(minion + " -> " + result));

        // Get the grains from a list of minions
        Target<List<String>> minionList = new MinionList("minion1", "minion2");

        // An empty result is returned for targeted minions that are down or minionList
        // entries that do not match actual targets.
        Map<String, Result<Map<String, Object>>> grainResults = Grains.items(false)
                .callSync(client, minionList, USER, PASSWORD, AuthModule.AUTO);

        grainResults.forEach((minion, grains) -> {
            System.out.println("\n--> Listing grains for '" + minion + "':\n");
            String grainsOutput = grains.fold(
                    error -> "Error: " + error.toString(),
                    grainsMap -> grainsMap.entrySet().stream()
                            .map(e -> e.getKey() + ": " + e.getValue())
                            .collect(Collectors.joining("\n"))
            );
            System.out.println(grainsOutput);
        });
    }
}
