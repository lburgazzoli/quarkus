package org.apache.camel.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.camel.CamelContext;

public class ResourceHelper {

    public static boolean isHttpUri(String uri) {
        return org.apache.camel.support.ResourceHelper.isHttpUri(uri);
    }

    public static String appendParameters(String uri, Map<String, Object> parameters) throws URISyntaxException {
        return org.apache.camel.support.ResourceHelper.appendParameters(uri, parameters);
    }

    public static InputStream resolveMandatoryResourceAsInputStream(CamelContext camelContext, String uri) throws IOException {
        return org.apache.camel.support.ResourceHelper.resolveMandatoryResourceAsInputStream(camelContext, uri);
    }

}
