package com.zorkdata.center.common.salt.netapi;

import com.zorkdata.center.common.salt.netapi.config.ClientConfig;
import com.zorkdata.center.common.salt.netapi.utils.ClientUtils;
import org.glassfish.tyrus.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/4/10 20:49
 */
@ServerEndpoint(value = "/token")
public class WebSocket {

    public static void main(String[] args) throws DeploymentException, InterruptedException {
        int WEBSOCKET_PORT = 8889;
        String WEBSOCKET_HOST = "localhost";
        String WEBSOCKET_PATH = "/ws";
        Server serverEndpoint;
        serverEndpoint = new Server(WEBSOCKET_HOST, WEBSOCKET_PORT,
                WEBSOCKET_PATH, null, TyrusWebSocketEventsTestMessages.class);
        serverEndpoint.start();
        Thread.sleep(10000000);
    }
}

@ServerEndpoint(value = "/token")
abstract class MockingWebSocket {
    private Logger logger = LoggerFactory.getLogger(MockingWebSocket.class);

    /**
     * A single message to be sent.
     */
    public static class Message {
        private String message;
        private Optional<Long> delay;

        public Message(String message, long delay) {
            this.message = message;
            this.delay = Optional.of(delay);
        }

        public Message(String message) {
            this.message = message;
            this.delay = Optional.empty();
        }

        public Optional<Long> getDelay() {
            return delay;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * @return the messages to be sent
     */
    public abstract Stream<Message> messages();

    public MockingWebSocket() {
    }

    @OnOpen
    public void onOpen(Session session) {
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        messages().forEach(m -> {
            m.getDelay().ifPresent(s -> {
                try {
                    Thread.sleep(s);
                } catch (InterruptedException e) {
                    logger.error("sleep 异常",e);
                }
            });
            try {
                session.getBasicRemote().sendText(m.getMessage());
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        });
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }
}

@ServerEndpoint(value = "/token")
class TyrusWebSocketEventsTestMessages extends MockingWebSocket {
    @Override
    public Stream<Message> messages() {
        return Stream.of("data: {\"tag\": \"20150505113307407682\", \"data\": {\"_stamp\": \"2015-05-05T18:33:07.408179\", \"minions\": [\"172.16.1.121\"]}}\n" +
                "\n" +
                "data: {\"tag\": \"new_job\", \"data\": {\"tgt_type\": \"glob\", \"jid\": \"20150505113307407682\", \"tgt\": \"*\", \"_stamp\": \"2015-05-05T18:33:07.408318\", \"user\": \"gaisford\", \"arg\": [\"i3\", {\"sysupgrade\": \"false\", \"refresh\": \"true\", \"__kwarg__\": true}], \"fun\": \"pkg.install\", \"minions\": [\"172.16.1.121\"]}}\n" +
                "\n" +
                "data: {\"tag\": \"salt/job/20150505113307407682/new\", \"data\": {\"tgt_type\": \"glob\", \"jid\": \"20150505113307407682\", \"tgt\": \"*\", \"_stamp\": \"2015-05-05T18:33:07.408428\", \"user\": \"gaisford\", \"arg\": [\"i3\", {\"sysupgrade\": \"false\", \"refresh\": \"true\", \"__kwarg__\": true}], \"fun\": \"pkg.install\", \"minions\": [\"172.16.1.121\"]}}\n" +
                "\n" +
                "data: {\"tag\": \"salt/auth\", \"data\": {\"_stamp\": \"2015-05-05T18:33:37.704344\", \"act\": \"accept\", \"id\": \"172.16.1.121\", \"pub\": \"-----BEGIN PUBLIC KEY-----\\nMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAyjipjKj/QG0JMBvHIv24\\nLmieNMsI7qiAt8Ffvp1wpovKNRgx8p/VwJzHKqH1cSbsUlDxY7O74wfZSdNI5H0K\\n5fCkX2HSfa5Lb01ZeM0ma3xRDjAtRAgfjS/s8/bkMSE29lab9MSWZ9hWhgtwIYxI\\nz4TQMlZfx2Lv6PN+Bw2oA0MwrUNdYAs2DmArJQpJGQAcKF1sUA4es9u3aiDmp5Si\\npO9PnWTcv+cJrGYhjtCCXII/040V+Go2gsmzzjIqudM0y+i8eKQ4C/6HlyaHitWe\\n8xyHnZ3tTtiODSdzrOLXuAUYrwjrqVDCp+pQhN4NUDQ0quenjFtH+6wH1NvPfYfX\\nAw5oJIa4FqFAgOE0bCL4dL5JoU93cMaG1gY8JgAGEu5Q2fSl0A+dydIN7kghXCcI\\nTfHrSh7YtpEuw0HnoH4nVEaORVARtunymKT+axmqzZKYRFdAMmjq9yfdpsMDFmBi\\n3x4qMt5tuiUgGg+wlWpGxGAzF6GEF+BmPFU7cvBHg9lr2fu0OCpbGguq5OxhgqH1\\nXrUEmdvfENI5QxTJmrkxW3hKA/U61GD/jty6oZZH1BJzreYqyrD6wIoBNvFW7tjX\\nl8eJm1uR1ZKJRFxITE2Qt8s8UYeVhqaWUGNiEhT2v6ts885osviqkMIXQcXfvems\\nrbFvhNrfq0wPvC53ZA+JwycCAwEAAQ==\\n-----END PUBLIC KEY-----\\n\", \"result\": true}}\n" +
                "\n" +
                "data: {\"tag\": \"20150505113307407682\", \"data\": {\"fun_args\": [\"i3\", {\"sysupgrade\": \"false\", \"refresh\": \"true\"}], \"jid\": \"20150505113307407682\", \"return\": {}, \"retcode\": 0, \"success\": true, \"cmd\": \"_return\", \"_stamp\": \"2015-05-05T18:33:37.778893\", \"fun\": \"pkg.install\", \"id\": \"172.16.1.121\"}}\n" +
                "\n" +
                "data: {\"tag\": \"salt/job/20150505113307407682/ret/172.16.1.121\", \"data\": {\"fun_args\": [\"i3\", {\"sysupgrade\": \"false\", \"refresh\": \"true\"}], \"jid\": \"20150505113307407682\", \"return\": {}, \"retcode\": 0, \"success\": true, \"cmd\": \"_return\", \"_stamp\": \"2015-05-05T18:33:37.779073\", \"fun\": \"pkg.install\", \"id\": \"172.16.1.121\"}}\n"
                        .split("\n\n"))
                .map(Message::new);
    }
}