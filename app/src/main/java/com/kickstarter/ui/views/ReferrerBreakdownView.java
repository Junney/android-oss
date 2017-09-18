package com.kickstarter.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout;

import com.kickstarter.libs.ReferrerColor;

public class ReferrerBreakdownView extends View {

  public Canvas canvas;
  private Double customSweepAngle;
  private Double externalSweepAngle;
  private Double internalSweepAngle;
  private Double unknownSweepAngle;
  private RectF rectF;

  public ReferrerBreakdownView(Context context) {
    super(context);
    setWillNotDraw(false);
  }

  public void setCustomAngleAndColor(Double sweepAngle) {
    this.customSweepAngle = sweepAngle;
  }

  public void setExternalAngleAndColor(Double sweepAngle) {
    this.externalSweepAngle = sweepAngle;
  }

  public void setInternalAngleAndColor(Double sweepAngle) {
    this.internalSweepAngle = sweepAngle;
  }

  public void setUnknownAngleAndColor(Double sweepAngle) {
    this.unknownSweepAngle = sweepAngle;
  }

  /* https://stackoverflow.com/questions/2159320/how-to-size-an-android-view-based-on-its-parents-dimensions */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
    int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
    this.setMeasuredDimension(parentWidth/2, parentHeight);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(parentWidth/2,parentHeight);
    // left top right bottom
    params.setMargins(50, 50, 50, 50);
    this.setLayoutParams(params);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    this.canvas = canvas;
    final Paint p = new Paint();
    Integer offset = 0;
    p.setAntiAlias(true);
    p.setDither(true);
    p.setStyle(Paint.Style.FILL);
    float bottom = getHeight();
    float left = getX();
    float right = getWidth();
    float top = getY();
    //left top right bottom
//    this.rectF = new RectF(0, 0, 400, 400);
    this.rectF = new RectF(left, top, right, bottom);

    p.setColor(ReferrerColor.CUSTOM.getReferrerColor());
    this.canvas.drawArc(this.rectF, offset.floatValue(), this.customSweepAngle.floatValue(), true, p);
    offset = offset + customSweepAngle.intValue();

    p.setColor(ReferrerColor.EXTERNAL.getReferrerColor());
    this.canvas.drawArc(this.rectF, offset.floatValue(), this.externalSweepAngle.floatValue(), true, p);
    offset = offset + externalSweepAngle.intValue();

    p.setColor(ReferrerColor.INTERNAL.getReferrerColor());
    this.canvas.drawArc(this.rectF, offset.floatValue(), this.internalSweepAngle.floatValue(), true, p);
    offset = offset + internalSweepAngle.intValue();

    p.setColor(ReferrerColor.CAMPAIGN.getReferrerColor());
    this.canvas.drawArc(this.rectF, offset.floatValue(), this.unknownSweepAngle.floatValue(), true, p);
  }
}
