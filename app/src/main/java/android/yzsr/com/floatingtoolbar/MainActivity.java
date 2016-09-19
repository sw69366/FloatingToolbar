package android.yzsr.com.floatingtoolbar;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {


    OnSoftKeyboardChangeListener listener;
    View floatingToolbar;
    RelativeLayout rootView;
    private int naviBarHeight, screenHeight, screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = ((RelativeLayout) findViewById(R.id.rootView));

        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        DisplayMetrics out = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindowManager().getDefaultDisplay().getRealMetrics(out);
        }
        screenHeight = out.heightPixels;
        screenWidth = out.widthPixels;
        naviBarHeight = out.heightPixels - rect.bottom;

        floatingToolbar = findViewById(R.id.floatingToolBar);


        //这里手动计算工具栏大小
        ViewGroup.LayoutParams layoutParams = floatingToolbar.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = (int) (screenWidth * 0.13f);

        floatingToolbar.setLayoutParams(layoutParams);

        final View decorView = getWindow().getDecorView();

        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {


            @Override
            public void onGlobalLayout() {

            }
        });


       getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
           int previousKeyboardHeight = -1;
           @Override
           public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
               Rect rect = new Rect();
               decorView.getWindowVisibleDisplayFrame(rect);
               int displayHeight = rect.bottom - rect.top;
               int height = decorView.getHeight();
               int keyboardHeight = height - displayHeight;
               if (previousKeyboardHeight != keyboardHeight) {
                   boolean hide = (double) displayHeight / height > 0.8;
                   listener.onSoftKeyBoardChange(keyboardHeight, !hide);
               }
               previousKeyboardHeight = height;
           }
       });


        listener = new OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
                if (visible) {
                    floatingToolbar.setVisibility(View.VISIBLE);
                    int height = floatingToolbar.getLayoutParams().height;
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) floatingToolbar.getLayoutParams();
                    lp.topMargin = screenHeight - softKeybardHeight - height + naviBarHeight;

                    floatingToolbar.setLayoutParams(lp);


                } else {
                    floatingToolbar.setVisibility(View.GONE);
                }
            }
        };

    }

    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }


}
