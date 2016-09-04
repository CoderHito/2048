package com.example.my2048;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	// 分数显示
	private TextView tv_score;
	// 计算分数
	private int score;
	private static MainActivity mainActivity = null;

	/**
	 * 获取Mainactivity的实例
	 * 
	 * @return MainActivity的实例
	 */
	public static MainActivity getInstance() {
		return mainActivity;
	}

	/**
	 * 构造方法
	 */
	public MainActivity() {
		mainActivity = this;
	}

	/**
	 * 清空得分
	 */
	public void clearScore() {
		score = 0;
		showScore();
	}

	/**
	 * 添加得分
	 * 
	 * @param num
	 *            得分
	 */
	public void addScore(int num) {
		score += num;
		showScore();
	}

	/**
	 * 显示分数
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
