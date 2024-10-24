package com.bigprime.source.spi.model;

import java.io.IOException;

/**
 * @author lyw
 * @version 1.0
 */
public class SafeAppendable {
    private final Appendable a;
    private boolean empty = true;

    public SafeAppendable(Appendable a) {
        super();
        this.a = a;
    }

    public SafeAppendable append(CharSequence s) {
        try {
            if (empty && s.length() > 0) {
                empty = false;
            }
            a.append(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public boolean isEmpty() {
        return empty;
    }
}
