package com.openai.domain;


import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class OpenAiSessionAttribute implements Serializable {
    private static final long serialVersionUID = -1L;
    private Map<String, Object> attributes = new HashMap<>();


    public String getStringValue(String key) {
        return null == attributes.get(key) ? null : attributes.get(key).toString();
    }
    public void addValue(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public String toString() {
        return "SessionAttribute{" + "attributes=" + (null == attributes ? "null" : attributes.toString()) + '}';
    }

}


