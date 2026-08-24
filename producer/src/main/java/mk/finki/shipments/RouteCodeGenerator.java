package mk.finki.shipments;

import java.util.LinkedHashMap;
import java.util.Map;

public class RouteCodeGenerator {

    private static final Map<String, String> COUNTRY_CODES =
            new LinkedHashMap<>();

    static {
        COUNTRY_CODES.put("grcija", "GR");
        COUNTRY_CODES.put("greece", "GR");

        COUNTRY_CODES.put("srbija", "RS");
        COUNTRY_CODES.put("serbia", "RS");

        COUNTRY_CODES.put("bih", "BA");
        COUNTRY_CODES.put("bosna", "BA");
        COUNTRY_CODES.put("bosna i hercegovina", "BA");

        COUNTRY_CODES.put("hrvatska", "HR");
        COUNTRY_CODES.put("croatia", "HR");

        COUNTRY_CODES.put("slovenija", "SI");
        COUNTRY_CODES.put("slovenia", "SI");

        COUNTRY_CODES.put("ungarija", "HU");
        COUNTRY_CODES.put("hungary", "HU");

        COUNTRY_CODES.put("avstrija", "AT");
        COUNTRY_CODES.put("austria", "AT");

        COUNTRY_CODES.put("albanija", "AL");
        COUNTRY_CODES.put("albania", "AL");

        COUNTRY_CODES.put("makedonija", "MK");
        COUNTRY_CODES.put("north macedonia", "MK");
    }

    private final Map<String, String> routeCodes = new LinkedHashMap<>();

    private int nextRouteNumber = 1;

    public String generate(String exporter, String importer) {

        String destinationCode = extractCountryCode(importer);

        String routeKey = normalize(exporter) + "->" + normalize(importer);

        if (routeCodes.containsKey(routeKey)) {
            return routeCodes.get(routeKey);
        }

        String routeCode = String.format(
                "MK-%s-%03d",
                destinationCode,
                nextRouteNumber++
        );

        routeCodes.put(routeKey, routeCode);

        return routeCode;
    }

    private String extractCountryCode(String value) {

        if (value == null || value.isBlank()) {
            return "XX";
        }

        String normalized = normalize(value);

        for (Map.Entry<String, String> entry : COUNTRY_CODES.entrySet()) {
            if (normalized.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "XX";
    }

    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return value
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", " ");
    }
}