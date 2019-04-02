package com.example.zhepingjiang.navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.Lists;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TimelineItemDecoration extends RecyclerView.ItemDecoration {
    private Paint infoPaint;

    private Paint timePaint;
    private Paint datePaint;

    private int itemView_leftinterval;
    private int itemView_topinterval;

    private int circle_radius;

    private Bitmap curIcon;
    private Bitmap consumptionIcon;
    private Bitmap disposalIcon;
    private Bitmap purchaseIcon;

        public TimelineItemDecoration(Context context) {
        infoPaint = new Paint();
        infoPaint.setColor(Color.RED);

        timePaint = new Paint();
        timePaint.setColor(Color.BLACK);
        timePaint.setTextSize(50);

        datePaint = new Paint();
        datePaint.setColor(Color.BLACK);
        datePaint.setTextSize(50);

        itemView_leftinterval = 550;
        itemView_topinterval = 50;
        circle_radius = 20;

        try {
            purchaseIcon = BitmapFactory.decodeStream(context.getAssets().open("ic_buy.png"));
            disposalIcon = BitmapFactory.decodeStream(context.getAssets().open("ic_throw.png"));
            consumptionIcon = BitmapFactory.decodeStream(context.getAssets().open("ic_eat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(itemView_leftinterval, itemView_topinterval, 0, 0);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            /**
             * Draw the circle
             */
            float centerx = child.getLeft() - itemView_leftinterval / 3;
            float centery = child.getTop() - itemView_topinterval + (itemView_topinterval + child.getHeight()) / 2;

            float upLine_up_x = centerx;
            float upLine_up_y = child.getTop() - itemView_topinterval;

            float upLine_bottom_x = centerx;
            float upLine_bottom_y = centery - circle_radius;

            // Draw upper line
            infoPaint.setStrokeWidth(10);
            infoPaint.setColor(Color.LTGRAY);
            if (i != 0) {
                c.drawLine(upLine_up_x, upLine_up_y, upLine_bottom_x, upLine_bottom_y, infoPaint);
            }

            float bottomLine_up_x = centerx;
            float bottom_up_y = centery + circle_radius;

            float bottomLine_bottom_x = centerx;
            float bottomLine_bottom_y = child.getBottom();

            // Draw lower line
            if (i != childCount - 1) {
                c.drawLine(bottomLine_up_x, bottom_up_y, bottomLine_bottom_x, bottomLine_bottom_y, infoPaint);
            }

            // Draw action icon
            String action = ((TextView)child.findViewById(R.id.ItemAction)).getText().toString();
            switch (action) {
                case "purchase":
                    curIcon = purchaseIcon;
                    break;
                case "disposal":
                    curIcon = disposalIcon;
                    break;
                case "consumption":
                    curIcon = consumptionIcon;
                    break;
                default: curIcon = purchaseIcon;
            }
            c.drawBitmap(curIcon,centerx - circle_radius - 30 ,centery - circle_radius - 20, infoPaint);

            int index = parent.getChildAdapterPosition(child);
            // Date and Time text
            float Text_x = child.getLeft() - itemView_leftinterval * 5 / 6;
            float Text_y = upLine_bottom_y;

            DateTime itemDateTime = DateTime.parse(((TextView)child.findViewById(R.id.ItemDateTime)).getText().toString(),
                    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));

            c.drawText(itemDateTime.toString("MMM dd"), Text_x, Text_y, datePaint);

            if (action != "purchase") {
                c.drawText(itemDateTime.toString("HH:mm"), Text_x + 5, Text_y + 50, timePaint);
            }

        }
    }

}