package com.flooferland.showbiz.backend.util;

public final class ShowbizExceptions {
    public static class RuntimeNoTrace extends RuntimeException {
        public RuntimeNoTrace(String info) {
            super(info);
        }

        @Override
        public void printStackTrace() {}

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
