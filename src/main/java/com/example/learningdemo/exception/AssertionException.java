package com.example.learningdemo.exception;

public class AssertionException extends RuntimeException {
    public AssertionException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message 运行时异常消息，允许使用类似{}占位符，throwable请放在参数最后一位，否则将丢失异常栈信息
     * @param args    运行时异常参数
     */
    public AssertionException(String message, Object... args) {
        super(String.format(message.replaceAll("\\{}", "%s"), args), extractThrowable(args));
    }

    private static Throwable extractThrowable(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }

        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable) lastEntry;
        }
        return null;
    }
}
