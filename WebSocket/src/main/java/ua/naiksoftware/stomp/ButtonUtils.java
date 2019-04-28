package ua.naiksoftware.stomp;

/**
 * 避免按钮重复点击操作
 */
public class ButtonUtils {
    private static long mLastClickTime = 0;
    public static final int TIME_INTERVAL = 3000;
    public static boolean isClicker(){
        long currentOnClickerTime = System.currentTimeMillis();
        if (currentOnClickerTime-mLastClickTime>=TIME_INTERVAL){
           mLastClickTime=currentOnClickerTime;
           return true;
       }else{
           return false;
       }
    }
}
