package com.example.my2048;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {

	private int columuNums = 5;
	// Card数组
	private Card cardMap[][] = new Card[columuNums][columuNums];
	// 记录空点的集合,之后从这个集合中随机取出一个值就是随机数要出现的地方
	private List<Point> emptyPoints = new ArrayList<>();

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public GameView(Context context) {
		super(context);
		initView();
	}

	// 适配屏幕
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// 设置卡片的宽
		// 是屏幕的宽/4 -10 是为了跟屏幕边缘留点空隙
		int cardWidth = (Math.min(w, h) - 10) / columuNums;
		addCards(cardWidth, cardWidth);
		startGame();
	}

	/**
	 * 游戏重新开始
	 */
	private void startGame() {
		// 清空得分
		MainActivity.getInstance().clearScore();
		// 初始化
		for (int y = 0; y < columuNums; y++) {
			for (int x = 0; x < columuNums; x++) {
				cardMap[x][y].setNum(0);
			}
		}
		// 添加数字
		addRandomNum();
		addRandomNum();

	}

	/**
	 * 添加随机数
	 */
	private void addRandomNum() {
		// 清空集合
		emptyPoints.clear();
		// 遍历数组
		for (int y = 0; y < columuNums; y++) {
			for (int x = 0; x < columuNums; x++) {
				// 没有设置数字
				if (cardMap[x][y].getNum() <= 0) {
					emptyPoints.add(new Point(x, y));
				}
			}
		}
		// 从已经存放空值的集合中随机选择一个位置
		Point p = emptyPoints
				.remove((int) (Math.random() * emptyPoints.size()));
		// 在取出的位置上设置数字2或4 比例是1比9
		cardMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

	}

	// 添加所有卡片
	private void addCards(int cardWidth, int cardHeight) {
		Card card;
		for (int y = 0; y < columuNums; y++) {
			for (int x = 0; x < columuNums; x++) {
				card = new Card(getContext());
				card.setNum(0);
				addView(card, cardWidth, cardHeight);
				cardMap[x][y] = card;
			}
		}
	}

	/**
	 * 初始化方法
	 */
	private void initView() {
		// 4列
		setColumnCount(columuNums);
		// 设置背景颜色
		setBackgroundColor(0xffbbada0);
		// 设置触摸监听事件
		setOnTouchListener(new OnTouchListener() {
			// xy的开始位置以及xy的偏移量
			private float startX, startY, offsetX, offsetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 获得开始位置的坐标
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					// 计算偏移量
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;

					// 根据偏移量来判断是 竖直方向 还是水平方向移动
					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						// 水平方向移动
						if (offsetX < -5) {
							// 左
							moveLeft();
						} else if (offsetX > 5) {
							// 右
							moveRight();
						}
					} else {
						// 竖直方向移动
						if (offsetY < -5) {
							// 上
							moveUp();
						} else if (offsetY > 5) {
							// 下
							moveDown();
						}
					}

					break;
				default:
					break;
				}

				return true;
			}
		});
	}

	/**
	 * 检查游戏是否结束
	 */
	private void checkComplete() {
		boolean complete = true;
		// 逐个遍历
		ALL: for (int y = 0; y < columuNums; y++) {
			for (int x = 0; x < columuNums; x++) {
				// 判断条件
				// 1.还有数字为0
				// 2.当前位置的数字和它四个方向还有相同的数字
				if (cardMap[x][y].getNum() <= 0
						|| (x > 1 && cardMap[x][y].equals(cardMap[x - 1][y]))
						|| (x < columuNums - 1 && cardMap[x][y]
								.equals(cardMap[x + 1][y]))
						|| (y > 1 && cardMap[x][y].equals(cardMap[x][y - 1]))
						|| (y < columuNums - 1 && cardMap[x][y]
								.equals(cardMap[x][y + 1]))) {
					complete = false;
					break ALL;
				}
			}
		}
		if (complete) {
			new AlertDialog.Builder(getContext())
					.setTitle("你好")
					.setMessage("游戏结束")
					.setPositiveButton("重来",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startGame();
								}
							}).show();
		}
	}

	/**
	 * 手指向左移动
	 */
	public void moveLeft() {

		boolean merge = false;
		// 一行一行的遍历
		for (int y = 0; y < columuNums; y++) {
			// 从左开始
			for (int x = 0; x < columuNums; x++) {
				// 从当前位置向右遍历
				for (int x1 = x + 1; x1 < columuNums; x1++) {
					// 找到有数字的位置
					if (cardMap[x1][y].getNum() > 0) {
						// 当前位置为0
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);
							x--;
						} else if (cardMap[x1][y].equals(cardMap[x][y])) {
							// 如果两个数字相等
							cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
							cardMap[x1][y].setNum(0);
							// 设置分数
							MainActivity.getInstance().addScore(
									cardMap[x][y].getNum());
						}
						merge = true;
						break;
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	/**
	 * 向右
	 */
	public void moveRight() {
		boolean merge = false;
		// 一行一行遍历
		for (int y = 0; y < columuNums; y++) {
			// 从右开始
			for (int x = columuNums - 1; x >= 0; x--) {
				// 从当前位置向左
				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cardMap[x1][y].getNum() > 0) {
						// 当前位置为0
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);
							// 重新遍历一遍
							x++;
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x1][y])) {
							// 如果两个数字相等
							cardMap[x][y].setNum(cardMap[x1][y].getNum() * 2);
							cardMap[x1][y].setNum(0);
							merge = true;
							// 设置分数
							MainActivity.getInstance().addScore(
									cardMap[x][y].getNum());
						}
						break;
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	/**
	 * 向上
	 */
	public void moveUp() {
		boolean merge = false;
		// 一列一列遍历
		for (int x = 0; x < columuNums; x++) {
			// 从上向下
			for (int y = 0; y < columuNums; y++) {
				// 当前位置向下
				for (int y1 = y + 1; y1 < columuNums; y1++) {
					// 找到数字
					if (cardMap[x][y1].getNum() > 0) {
						// 当前位置数字为0
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);
							y--;
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x][y1])) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum() * 2);
							cardMap[x][y1].setNum(0);
							merge = true;
							// 设置分数
							MainActivity.getInstance().addScore(
									cardMap[x][y].getNum());
						}
						break;
					}

				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	/**
	 * 向下
	 */
	public void moveDown() {
		boolean merge = false;
		// 一列一列遍历
		for (int x = 0; x < columuNums; x++) {
			// 向上遍历
			for (int y = columuNums - 1; y >= 0; y--) {
				// 从当前位置向上遍历
				for (int y1 = y - 1; y1 >= 0; y1--) {
					// 找到值
					if (cardMap[x][y1].getNum() > 0) {
						// 当前位置为0
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);
							y++;
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x][y1])) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum() * 2);
							cardMap[x][y1].setNum(0);
							merge = true;
							// 设置分数
							MainActivity.getInstance().addScore(
									cardMap[x][y].getNum());
						}
						break;
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

}
