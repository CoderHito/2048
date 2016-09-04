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
	// Card����
	private Card cardMap[][] = new Card[columuNums][columuNums];
	// ��¼�յ�ļ���,֮���������������ȡ��һ��ֵ���������Ҫ���ֵĵط�
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

	// ������Ļ
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// ���ÿ�Ƭ�Ŀ�
		// ����Ļ�Ŀ�/4 -10 ��Ϊ�˸���Ļ��Ե�����϶
		int cardWidth = (Math.min(w, h) - 10) / columuNums;
		addCards(cardWidth, cardWidth);
		startGame();
	}

	/**
	 * ��Ϸ���¿�ʼ
	 */
	private void startGame() {
		// ��յ÷�
		MainActivity.getInstance().clearScore();
		// ��ʼ��
		for (int y = 0; y < columuNums; y++) {
			for (int x = 0; x < columuNums; x++) {
				cardMap[x][y].setNum(0);
			}
		}
		// �������
		addRandomNum();
		addRandomNum();

	}

	/**
	 * ��������
	 */
	private void addRandomNum() {
		// ��ռ���
		emptyPoints.clear();
		// ��������
		for (int y = 0; y < columuNums; y++) {
			for (int x = 0; x < columuNums; x++) {
				// û����������
				if (cardMap[x][y].getNum() <= 0) {
					emptyPoints.add(new Point(x, y));
				}
			}
		}
		// ���Ѿ���ſ�ֵ�ļ��������ѡ��һ��λ��
		Point p = emptyPoints
				.remove((int) (Math.random() * emptyPoints.size()));
		// ��ȡ����λ������������2��4 ������1��9
		cardMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

	}

	// ������п�Ƭ
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
	 * ��ʼ������
	 */
	private void initView() {
		// 4��
		setColumnCount(columuNums);
		// ���ñ�����ɫ
		setBackgroundColor(0xffbbada0);
		// ���ô��������¼�
		setOnTouchListener(new OnTouchListener() {
			// xy�Ŀ�ʼλ���Լ�xy��ƫ����
			private float startX, startY, offsetX, offsetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// ��ÿ�ʼλ�õ�����
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					// ����ƫ����
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;

					// ����ƫ�������ж��� ��ֱ���� ����ˮƽ�����ƶ�
					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						// ˮƽ�����ƶ�
						if (offsetX < -5) {
							// ��
							moveLeft();
						} else if (offsetX > 5) {
							// ��
							moveRight();
						}
					} else {
						// ��ֱ�����ƶ�
						if (offsetY < -5) {
							// ��
							moveUp();
						} else if (offsetY > 5) {
							// ��
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
	 * �����Ϸ�Ƿ����
	 */
	private void checkComplete() {
		boolean complete = true;
		// �������
		ALL: for (int y = 0; y < columuNums; y++) {
			for (int x = 0; x < columuNums; x++) {
				// �ж�����
				// 1.��������Ϊ0
				// 2.��ǰλ�õ����ֺ����ĸ���������ͬ������
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
					.setTitle("���")
					.setMessage("��Ϸ����")
					.setPositiveButton("����",
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
	 * ��ָ�����ƶ�
	 */
	public void moveLeft() {

		boolean merge = false;
		// һ��һ�еı���
		for (int y = 0; y < columuNums; y++) {
			// ����ʼ
			for (int x = 0; x < columuNums; x++) {
				// �ӵ�ǰλ�����ұ���
				for (int x1 = x + 1; x1 < columuNums; x1++) {
					// �ҵ������ֵ�λ��
					if (cardMap[x1][y].getNum() > 0) {
						// ��ǰλ��Ϊ0
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);
							x--;
						} else if (cardMap[x1][y].equals(cardMap[x][y])) {
							// ��������������
							cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
							cardMap[x1][y].setNum(0);
							// ���÷���
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
	 * ����
	 */
	public void moveRight() {
		boolean merge = false;
		// һ��һ�б���
		for (int y = 0; y < columuNums; y++) {
			// ���ҿ�ʼ
			for (int x = columuNums - 1; x >= 0; x--) {
				// �ӵ�ǰλ������
				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cardMap[x1][y].getNum() > 0) {
						// ��ǰλ��Ϊ0
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);
							// ���±���һ��
							x++;
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x1][y])) {
							// ��������������
							cardMap[x][y].setNum(cardMap[x1][y].getNum() * 2);
							cardMap[x1][y].setNum(0);
							merge = true;
							// ���÷���
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
	 * ����
	 */
	public void moveUp() {
		boolean merge = false;
		// һ��һ�б���
		for (int x = 0; x < columuNums; x++) {
			// ��������
			for (int y = 0; y < columuNums; y++) {
				// ��ǰλ������
				for (int y1 = y + 1; y1 < columuNums; y1++) {
					// �ҵ�����
					if (cardMap[x][y1].getNum() > 0) {
						// ��ǰλ������Ϊ0
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);
							y--;
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x][y1])) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum() * 2);
							cardMap[x][y1].setNum(0);
							merge = true;
							// ���÷���
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
	 * ����
	 */
	public void moveDown() {
		boolean merge = false;
		// һ��һ�б���
		for (int x = 0; x < columuNums; x++) {
			// ���ϱ���
			for (int y = columuNums - 1; y >= 0; y--) {
				// �ӵ�ǰλ�����ϱ���
				for (int y1 = y - 1; y1 >= 0; y1--) {
					// �ҵ�ֵ
					if (cardMap[x][y1].getNum() > 0) {
						// ��ǰλ��Ϊ0
						if (cardMap[x][y].getNum() <= 0) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);
							y++;
							merge = true;
						} else if (cardMap[x][y].equals(cardMap[x][y1])) {
							cardMap[x][y].setNum(cardMap[x][y1].getNum() * 2);
							cardMap[x][y1].setNum(0);
							merge = true;
							// ���÷���
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
