package com.spacer.event.util;

import android.support.annotation.IntDef;




public interface AuthStatusChangedListener {

    /** @hide */
    @IntDef({JUST_SIGN_IN, JUST_SIGN_OUT})
    public @interface SignStatus{}

    public static int JUST_SIGN_IN = 0;
    public static int JUST_SIGN_OUT = 1;

    /**
     *
     *
     * @param status One of {@link #JUST_SIGN_IN} or {@link #JUST_SIGN_OUT}.
     */
    void onAuthStatusChanged(@SignStatus int status);
}
