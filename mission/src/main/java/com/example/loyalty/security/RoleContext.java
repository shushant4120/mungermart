package com.example.loyalty.security;

public class RoleContext {
    private static final ThreadLocal<Role> current = new ThreadLocal<>();

    public static void set(Role r) {
        current.set(r);
    }

    public static Role get() {
        return current.get();
    }

    public static void clear() {
        current.remove();
    }
}
