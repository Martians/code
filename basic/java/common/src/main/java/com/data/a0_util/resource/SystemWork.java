package com.data.a0_util.resource;

import static com.data.a0_util.format.Display.out;

public class SystemWork {

    /**
     * 更多处理，见 464 Process、469 ProcessBuilder、465 Runtime
     */
    static void runtime() {
        Runtime r = Runtime.getRuntime();
        r.traceInstructions(true);
        r.traceMethodCalls(true);
        out("total " + r.totalMemory() + ", free " + r.freeMemory());

        try {
            Process p = r.exec("notepad");
            p.waitFor();

        } catch (Exception e) {
            out(e);
        }
        out("exit...");
    }


    public static void main(String args[]) {
        if (false) {
            runtime();
        }
    }
}
