package com.techchallenge.pagamentos.util;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ResourceUtil {
    private ResourceUtil() {

    }

    public static String getContentFromResource(String resourceName) {
        try {
            InputStream stream = ResourceUtil.class.getResourceAsStream(resourceName);
            return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
