package com.zorkdata.center.common.salt.netapi.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.zorkdata.center.common.salt.netapi.datatypes.Event;
import com.zorkdata.center.common.salt.netapi.parser.JsonParser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/24 17:30
 */
public class JobNewEvent {
    private static final Pattern PATTERN = Pattern.compile("^salt/job/([^/]+)/new");

    private final String jobId;
    private Data data;

    private static final Gson GSON = JsonParser.GSON;

    public static class Data {
        @SerializedName("_stamp")
        private String timestamp;
        @SerializedName("arg")
        private Object arg;
        @SerializedName("fun")
        private String fun;
        @SerializedName("jid")
        private String jid;
        @SerializedName("minions")
        private Object minions;
        @SerializedName("missing")
        private Object missing;
        @SerializedName("tgt")
        private Object tgt;
        @SerializedName("tgt_type")
        private String tgt_type;
        @SerializedName("user")
        private String user;

        //FIXUP: make metadata getter the same as result
        private Optional<JsonElement> metadata = Optional.empty();

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Object getArg() {
            return arg;
        }

        public void setArg(Object arg) {
            this.arg = arg;
        }

        public String getFun() {
            return fun;
        }

        public void setFun(String fun) {
            this.fun = fun;
        }

        public String getJid() {
            return jid;
        }

        public void setJid(String jid) {
            this.jid = jid;
        }

        public Object getMinions() {
            return minions;
        }

        public void setMinions(Object minions) {
            this.minions = minions;
        }

        public Object getMissing() {
            return missing;
        }

        public void setMissing(Object missing) {
            this.missing = missing;
        }

        public Object getTgt() {
            return tgt;
        }

        public void setTgt(Object tgt) {
            this.tgt = tgt;
        }

        public String getTgt_type() {
            return tgt_type;
        }

        public void setTgt_type(String tgt_type) {
            this.tgt_type = tgt_type;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public Optional<Object> getMetadata() {
            return metadata.flatMap(md -> {
                try {
                    return Optional.ofNullable(GSON.fromJson(md, Object.class));
                } catch (JsonSyntaxException ex) {
                    return Optional.empty();
                }
            });
        }

        public <R> Optional<R> getMetadata(Class<R> dataType) {
            return metadata.flatMap(md -> {
                try {
                    return Optional.ofNullable(GSON.fromJson(md, dataType));
                } catch (JsonSyntaxException ex) {
                    return Optional.empty();
                }
            });
        }

        public <R> Optional<R> getMetadata(TypeToken<R> dataType) {
            return metadata.flatMap(md -> {
                try {
                    return Optional.ofNullable(GSON.fromJson(md, dataType.getType()));
                } catch (JsonSyntaxException ex) {
                    return Optional.empty();
                }
            });
        }

    }

    public JobNewEvent(String jobid) {
        this.jobId = jobid;
    }

    private JobNewEvent(String jobIdIn, Data dataIn) {
        this.jobId = jobIdIn;
        this.data = dataIn;
    }

    /**
     * The id of the job
     *
     * @return job id
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * @return the event data
     */
    public Data getData() {
        return data;
    }


    /**
     * Utility method to parse a generic event into a more specific one.
     *
     * @param event the generic event to parse
     * @return an option containing the parsed value or non if it could not be parsed
     */
    public static Optional<JobNewEvent> parse(Event event) {
        Matcher matcher = PATTERN.matcher(event.getTag());
        if (matcher.matches()) {
            Data data = event.getData(Data.class);
            JobNewEvent result = new JobNewEvent(matcher.group(1), data);
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }
}
