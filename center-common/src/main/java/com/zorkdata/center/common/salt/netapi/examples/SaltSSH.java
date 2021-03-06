package com.zorkdata.center.common.salt.netapi.examples;

import com.zorkdata.center.common.salt.netapi.calls.SaltSSHConfig;
import com.zorkdata.center.common.salt.netapi.calls.modules.Grains;
import com.zorkdata.center.common.salt.netapi.calls.modules.Test;
import com.zorkdata.center.common.salt.netapi.client.SaltClient;
import com.zorkdata.center.common.salt.netapi.datatypes.target.Glob;
import com.zorkdata.center.common.salt.netapi.datatypes.target.SSHTarget;
import com.zorkdata.center.common.salt.netapi.exception.SaltException;
import com.zorkdata.center.common.salt.netapi.results.Result;
import com.zorkdata.center.common.salt.netapi.results.SSHResult;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Example code calling salt modules via ssh.
 */
public class SaltSSH {

    private static final String SALT_API_URL = "http://localhost:8000";

    public static void main(String[] args) throws SaltException {
        // Init the client
        SaltClient client = new SaltClient(URI.create(SALT_API_URL));

        // Setup the configuration for Salt SSH (use defaults)
        SaltSSHConfig sshConfig = new SaltSSHConfig.Builder().build();

        // Ping all minions using a glob matcher
        SSHTarget<String> globTarget = new Glob("*");
        Map<String, Result<SSHResult<Boolean>>> minionResults =
                Test.ping().callSyncSSH(client, globTarget, sshConfig);

        System.out.println("--> Ping results:\n");
        minionResults.forEach((minion, result) -> {
            System.out.println(minion + " -> " + result.fold(
                    error -> "Error: " + error.toString(),
                    res -> res.getReturn().orElse(false)
            ));
        });

        // Get grains from all minions
        Map<String, Result<SSHResult<Map<String, Object>>>> grainResults =
                Grains.items(false).callSyncSSH(client, globTarget, sshConfig);

        grainResults.forEach((minion, grains) -> {
            System.out.println("\n--> Listing grains for '" + minion + "':\n");
            String grainsOutput = grains.fold(
                    error -> "Error: " + error.toString(),
                    grainsMap -> grainsMap.getReturn()
                            .map(g -> g.entrySet().stream()
                                    .map(e -> e.getKey() + ": " + e.getValue())
                                    .collect(Collectors.joining("\n")))
                            .orElse("Minion did not return: " + minion)
            );
            System.out.println(grainsOutput);
        });
    }
}
