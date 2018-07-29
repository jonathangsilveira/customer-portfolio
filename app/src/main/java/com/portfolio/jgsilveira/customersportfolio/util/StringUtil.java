package com.portfolio.jgsilveira.customersportfolio.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

public final class StringUtil {

    public static final String VAZIO = "";

    private StringUtil() {
    }

    public static String getString(CharSequence text, @NonNull CharSequence defaultValue) {
        if (TextUtils.isEmpty(text)) {
            return defaultValue.toString();
        }
        return text.toString();
    }

}
