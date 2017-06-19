package com.sunkai.lab.androidlab.mixtypeset;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sunkai.lab.androidlab.R;

public class MixTextImageActivity extends Activity {

    private TextView mixTextImageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_text_image);
        mixTextImageTv = (TextView) findViewById(R.id.mix_text_image_01);

        initView();
    }

    private void initView() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString spannableString = new SpannableString("这里有一张图片");
        CustomImageSpan imageSpan = new CustomImageSpan(this, R.mipmap.ic_launcher_round, 2);
        spannableString.setSpan(imageSpan, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");

        spannableString = new SpannableString("这里测试超链接");
        spannableString.setSpan(new URLSpan("https://www.baidu.com/"), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");


        spannableString = new SpannableString("这里测试下划线");
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");


        spannableString = new SpannableString("字体serif");
        spannableString.setSpan(new TypefaceSpan("serif"), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");

        spannableString = new SpannableString("字体monospace");
        spannableString.setSpan(new TypefaceSpan("monospace"), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");

        spannableString = new SpannableString("字体sans-serif");
        spannableString.setSpan(new TypefaceSpan("sans-serif"), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");


        spannableString = new SpannableString("删除线");
        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");

        spannableString = new SpannableString("斜体");
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");


        spannableString = new SpannableString("三倍大小");
        spannableString.setSpan(new RelativeSizeSpan(3.0f), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");

        spannableString = new SpannableString("1.5大小 ,删除线");
        spannableString.setSpan(new RelativeSizeSpan(1.5f), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");

        spannableString = new SpannableString("引用样式");
        spannableString.setSpan(new QuoteSpan(0xff000000), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");

        spannableString = new SpannableString("自定义点击事件,颜色红色");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "点击了文字", Toast.LENGTH_SHORT).show();
            }
        }, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(0xffff0000), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString);
        spannableStringBuilder.append("\n");

        mixTextImageTv.setHighlightColor(0xff00ff00);
        mixTextImageTv.setMovementMethod(LinkMovementMethod.getInstance());
        mixTextImageTv.setText(spannableStringBuilder);
    }

    /**
     * http://www.jianshu.com/p/2650357f7547
     */
    private class CustomImageSpan extends ImageSpan {
        private int ALIGN_FONTCENTER = 2;


        public CustomImageSpan(Context context, int resourceId, int verticalAlignment) {
            super(context, resourceId, verticalAlignment);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            //draw 方法是重写的ImageSpan父类 DynamicDrawableSpan中的方法，在DynamicDrawableSpan类中，虽有getCachedDrawable()，
            // 但是私有的，不能被调用，所以调用ImageSpan中的getrawable()方法，该方法中 会根据传入的drawable ID ，获取该id对应的
            // drawable的流对象，并最终获取drawable对象
            Drawable drawable = getDrawable(); //调用imageSpan中的方法获取drawable对象
            canvas.save();

            //获取画笔的文字绘制时的具体测量数据
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();

            //系统原有方法，默认是Bottom模式)
            int transY = bottom - drawable.getBounds().bottom;
            if (mVerticalAlignment == ALIGN_BASELINE) {
                transY -= fm.descent;
            } else if (mVerticalAlignment == ALIGN_FONTCENTER) {    //此处加入判断， 如果是自定义的居中对齐
                //与文字的中间线对齐（这种方式不论是否设置行间距都能保障文字的中间线和图片的中间线是对齐的）
                // y+ascent得到文字内容的顶部坐标，y+descent得到文字的底部坐标，（顶部坐标+底部坐标）/2=文字内容中间线坐标
                transY = ((y + fm.descent) + (y + fm.ascent)) / 2 - drawable.getBounds().bottom / 2;
            }

            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }

        /**
         * 重写getSize方法，只有重写该方法后，才能保证不论是图片大于文字还是文字大于图片，都能实现中间对齐
         */
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fm.ascent = -bottom;
                fm.top = -bottom;
                fm.bottom = top;
                fm.descent = top;
            }
            return rect.right;
        }
    }
}
