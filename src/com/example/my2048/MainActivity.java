package com.example.my2048;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	// ������ʾ
	private TextView tv_score;
	// �������
	private int score;
	private static MainActivity mainActivity = null;

	/**
	 * ��ȡMainactivity��ʵ��
	 * 
	 * @return MainActivity��ʵ��
	 */
	public static MainActivity getInstance() {
		return mainActivity;
	}

	/**
	 * ���췽��
	 */
	public MainActivity() {
		mainActivity = this;
	}

	/**
	 * ��յ÷�
	 */
	public void clearScore() {
		score = 0;
		showScore();
	}

	/**
	 * ��ӵ÷�
	 * 
	 * @param num
	 *            �÷�
	 */
	public void addScore(int num) {
		score += num;
		showScore();
	}

	/**
	 * ��ʾ����
	 */
	public void showScore() {
		tv_score.setText(score + "");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv_score = (TextView) findViewById(R.id.tv_score);

	}

}
