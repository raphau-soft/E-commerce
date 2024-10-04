package com.devraf.e_commerce.filter;

import com.google.gson.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

import static java.util.Collections.list;

@Log4j2
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final String MASK = "********";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = requestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = responseWrapper(response);

        chain.doFilter(requestWrapper, responseWrapper);

        logRequest(requestWrapper);
        logResponse(responseWrapper);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        String path = request.getServletPath();
        return path.startsWith("/favicon.ico");
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        StringBuilder builder = new StringBuilder();
        builder.append(headersToString(list(request.getHeaderNames()), request::getHeader));
        builder.append(getMaskedBody(new String(request.getContentAsByteArray())));
        log.info("Request: {}", builder);
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(headersToString(response.getHeaderNames(), response::getHeader));
        builder.append(getMaskedBody(new String(response.getContentAsByteArray())));
        log.info("Response: {}", builder);
        response.copyBodyToResponse();
    }

    private String headersToString(Collection<String> headerNames, Function<String, String> headerValueResolver) {
        StringBuilder builder = new StringBuilder();
        for (String headerName : headerNames) {
            String header = headerValueResolver.apply(headerName);
            if(headerName.toLowerCase().endsWith("token")) header = MASK;
            builder.append(String.format("%s=%s", headerName, header)).append("\n");
        }
        return builder.toString();
    }

    private ContentCachingRequestWrapper requestWrapper(ServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            return requestWrapper;
        }
        return new ContentCachingRequestWrapper((HttpServletRequest) request);
    }

    private ContentCachingResponseWrapper responseWrapper(ServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper responseWrapper) {
            return responseWrapper;
        }
        return new ContentCachingResponseWrapper((HttpServletResponse) response);
    }

    private String getMaskedBody(String body) {
        if(body.isEmpty()) return body;
        try {
            JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
            maskSensitiveData(jsonObject, "password");
            maskSensitiveData(jsonObject, "confirmationPassword");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(jsonObject);
        } catch (JsonSyntaxException e) {
            return body;
        }
    }

    private void maskSensitiveData(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            jsonObject.addProperty(key, MASK);
        }
    }
}
