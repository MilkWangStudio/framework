package io.milkwang.util.common;

import com.google.common.collect.Lists;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class CookieUtils {

    public CookieUtils() {
    }

    public static void addCookie(String name, String value, String domain, int maxage, String path, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }

        cookie.setMaxAge(maxage);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void addCookie(String name, String value, String domain, int maxage, HttpServletResponse response) {
        addCookie(name, value, domain, maxage, "/", response);
    }

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
            return null;
        }
    }

    public static List<String> getCookies(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Lists.newArrayList();
        } else {
            List<String> result = Lists.newArrayList();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    result.add(cookie.getValue());
                }
            }
            return result;
        }
    }

    public static void removeCookie(String name, String domain, HttpServletResponse response) {
        addCookie(name, null, domain, 0, response);
    }

    public static void removeCookie(String name, HttpServletResponse response) {
        removeCookie(name, null, response);
    }
}
