package com.snackviet.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {
	@Autowired
	HttpSession session;
	
	public void setAttribute(String name, Object value) {
		session.setAttribute(name, value);
	}
	
	public <T> T getAttribute(String name){
		return  (T) session.getAttribute(name);
	}
	
	public void removeAttribute(String name) {
		session.removeAttribute(name);
	}

	public Object getNestedAttribute(String sessionKey, String nestedKey) {
        Object attribute = session.getAttribute(sessionKey);
        if (attribute instanceof String) {
            try {
                JSONObject jsonObject = new JSONObject((String) attribute);
                return jsonObject.opt(nestedKey);
            } catch (Exception e) {
                throw new RuntimeException("Error parsing JSON from session attribute", e);
            }
        } else {
            throw new IllegalArgumentException("Session attribute is not a JSON string");
        }
    }
}
