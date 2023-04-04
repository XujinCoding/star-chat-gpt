package com.star.web.session;

/**
 * @author XuJ
 */
public class AppSession {
    private static final ThreadLocal<OpenAiSessionAttribute> SESSION_ATTRIBUTE = new ThreadLocal<>();

    public static OpenAiSessionAttribute getSessionAttribute() {
        if (SESSION_ATTRIBUTE.get() == null) {
            SESSION_ATTRIBUTE.set(new OpenAiSessionAttribute());
        }
        return SESSION_ATTRIBUTE.get();
    }
    public static void destroy() {
        SESSION_ATTRIBUTE.remove();
    }
}
