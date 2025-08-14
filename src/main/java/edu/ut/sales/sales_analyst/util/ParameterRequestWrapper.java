package edu.ut.sales.sales_analyst.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> params = new HashMap<>();

    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
        // Copy param cũ nếu cần
        params.putAll(request.getParameterMap());
    }

    public void addParameter(String name, String value) {
        params.put(name, new String[]{ value });
    }

    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        return values != null ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }
}

