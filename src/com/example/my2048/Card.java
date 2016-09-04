package com.example.my2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
	// ��ʾ������
	private int num = 0;
	private TextView lable;

	public Card(Context context) {
		super(context);

		lable = new TextView(context);
		lable.setTextSize(32);
		lable.setGravity(Gravity.CENTER);
		lable.setBackgroundColor(0x33ffffff);

		// LayoutParams�൱��һ��Layout����Ϣ��������װ��Layout��λ�á��ߡ������Ϣ��
		// ��������Ļ��һ����������һ��Layoutռ��ģ������һ��View��ӵ�һ��Layout�У���ø���Layout�û������Ĳ��ַ�ʽ��
		// Ҳ���ǽ�һ���Ͽɵ�layoutParams���ݽ�ȥ��
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// ���ü�� ��������
		lp.setMargins(10, 10, 0, 0);
		// ��Ӳ���
		addView(lable, lp);
		setNum(0);
	}

	// ��ȡ����
	public int getNum() {
		return num;
	}

	// ��������
	public void setNum(int num) {
		this.num = num;
		// ����Ϊ0,���óɿ�
		if (num <= 0) {
			lable.setText("");
		} else {
			lable.setText(num + "");
		}
	}

	// �ж������������
	public boolean equals(Card c) {
		return getNum() == c.getNum();
	}

}
