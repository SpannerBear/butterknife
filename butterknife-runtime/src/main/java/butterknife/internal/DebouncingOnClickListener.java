package butterknife.internal;

import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * A {@linkplain View.OnClickListener click listener} that debounces multiple clicks posted in the
 * same frame. A click on one button disables all buttons for that frame.
 */
public abstract class DebouncingOnClickListener implements View.OnClickListener {
    private static final int MSG_ID = 66;
    static boolean enabled = true;
    
    private static DebouncingHandler handler;
    
    private static Handler getHandler() {
        if (handler == null) {
            handler = new DebouncingHandler();
        }
        return handler;
    }
    
    @Override
    public final void onClick(View v) {
        Handler handler = getHandler();
        if (enabled) {
            enabled = false;
            doClick(v);
            handler.sendEmptyMessageDelayed(MSG_ID, 100);
        } else {
            if (handler.hasMessages(MSG_ID)) {
                return;
            }
            handler.sendEmptyMessageDelayed(MSG_ID, 100);
        }
    }
    
    public abstract void doClick(View v);
    
    static class DebouncingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            enabled = true;
        }
    }
}
