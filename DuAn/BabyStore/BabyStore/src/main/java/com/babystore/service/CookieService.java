package com.babystore.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CookieService {

    @Autowired
    HttpServletRequest req;

    @Autowired
    HttpServletResponse resp;

    public Cookie createCookie(String name, String value, int days) {
        Cookie ck = new Cookie(name, value);
        ck.setMaxAge(days * 60 * 60);
        ck.setPath("/");
        resp.addCookie(ck);
        return ck;
    }

    public Cookie getCookie(String name) {
        Cookie[] cks = req.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (ck.getName().equalsIgnoreCase(name)) return ck;
            }
        }
        return null;
    }
}
