package sst.global.utils;

import org.slf4j.MDC;
import java.util.UUID;

public class MdcUtils {

    public static final String TRACE_ID  = "traceId";
    public static final String CLIENT_IP = "clientIp";

    private MdcUtils() {}

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    public static void put(String key, String value) {
        MDC.put(key, value != null ? value : "-");
    }

    public static String get(String key) {
        String value = MDC.get(key);
        return value != null ? value : "-";
    }

    public static void clear() {
        MDC.clear();
    }
}
