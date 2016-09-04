package com.example.my2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
	// 显示的数字
	private int num = 0;
	private TextView lable;

	public Card(Context context) {
		super(context);

		lable = new TextView(context);
		lable.setTextSize(32);
		lable.setGravity(Gravity.CENTER);
		lable.setBackgroundColor(0x33ffffff);

		// LayoutParams相当于一个Layout的信息包，它封装了Layout的位置、高、宽等信息。
		// 假设在屏幕上一块区域是由一个Layout占领的，如果将一个View添加到一个Layout中，最好告诉Layout用户期望的布局方式，
		// 也就是将一个认可的layoutParams传递进去。
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// 设置间隔 左上右下
		lp.setMargins(10, 10, 0, 0);
		// 添加布局
		addView(lable, lp);
		setNum(0);
	}

	// 获取数字
	public int getNum() {
		return num;
	}

	// 设置数字
	public void setNum(int num) {
		this.num = num;
		// 数字为0,设置成空
		if (num <= 0) {
			lable.setText("");
		} else {
			lable.setText(num + "");
		}
	}

	// 判断两个数字相等
	public boolean equals(Card c) {
		return getNum() == c.getNum();
	}

}
