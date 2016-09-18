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
        layoutParams.height = 200;

        final View decorView = getWindow().getDecorView();

        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;
            @Override
            public void onGlobalLayout() {
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
                    //显示工具栏
                    floatingToolbar.setX(0);
                    //200 对应上部分工具栏高度
                    floatingToolbar.setY(screenHeight-200-softKeybardHeight-naviBarHeight);
                    floatingToolbar.setVisibility(View.VISIBLE);
                } else {
                    floatingToolbar.setVisibility(View.GONE);
                }
            }
        };

    }

    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

}
