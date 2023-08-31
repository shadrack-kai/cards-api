package com.logicea.cards.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author STUNJE
 */
public class HeaderWrapper extends HttpServletRequestWrapper {

    private Map<String, String> headers = new HashMap<>();

    public HeaderWrapper(HttpServletRequest request) {
        super(request);
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (headers.containsKey(name)) {
            headerValue = headers.get(name);
        }
        return headerValue;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names =
                Collections.list(super.getHeaderNames()).stream().map(String::toLowerCase).collect(Collectors.toList());
        for (String name : headers.keySet()) {
            names.add(name);
        }
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values =
                Collections.list(super.getHeaders(name)).stream().map(String::toLowerCase).collect(Collectors.toList());
        if (headers.containsKey(name)) {
            values.add(headers.get(name));
        }
        return Collections.enumeration(values);
    }

}

