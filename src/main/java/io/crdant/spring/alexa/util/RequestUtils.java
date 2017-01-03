package io.crdant.spring.alexa.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Reader;

public class RequestUtils {
    public static JsonNode getRequestJson(HttpServletRequest request) throws IOException {
        return getRequestJson(WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class));
    }

    public static JsonNode getRequestJson (NativeWebRequest request) throws IOException {
        ContentCachingRequestWrapper wrapper = request.getNativeRequest(ContentCachingRequestWrapper.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode bodyRoot = null ;
        if ( ( wrapper.getContentAsByteArray() == null ) || ( wrapper.getContentAsByteArray().length == 0 ) ) {
            Reader bodyReader = wrapper.getReader();
            char[] buffer = new char[wrapper.getContentLength()];
            bodyReader.read(buffer);
            bodyRoot = mapper.readValue(bodyReader, JsonNode.class);
        } else {
            bodyRoot = mapper.readValue(wrapper.getContentAsByteArray(), JsonNode.class);
        }
        return bodyRoot ;
    }
}
