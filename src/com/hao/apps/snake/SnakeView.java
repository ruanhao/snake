package com.hao.apps.snake;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class SnakeView extends View {

	Bitmap mGreenStar = null;
	Bitmap mRedStar = null;
	Bitmap mYellowStar = null;
	Bitmap mAppleGreen = null;
	Bitmap mAppleRed = null;
	Bitmap mTurf = null;
	Bitmap mSnakeHead = null;
	int mTileWidth = 0;
	int mTileHeight = 0;
	public int mHorizonTileNum = 0;
	public int mVerticalTileNum = 0;
	Snake mSnake = null;
	
	public boolean mTimeToStartGame = true;
	
	

	public final static int HEAD = 0;
	public final static int BODY = 1;
	public final static int WALL = 2;
	public final static int APPlE = 3;

	public final static int EAST = 0;
	public final static int SOUTH = 1;
	public final static int WEST = 2;
	public final static int NORTH = 3;

	public LinkedList<Position> mSnakeBody = null;
	public HashSet<Position> mWall = null;
	public Position mApple = null;

	public SnakeHandler handler = null;
	public SnakeHandlerThread playThread = null;
	
	public int statusBefore = 0;
	

	public SnakeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mSnake = (Snake) context;
		mGreenStar = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.greenstar);
		mRedStar = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.redstar);
		mYellowStar = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.yellowstar);
		mAppleGreen = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.candygreen);
		mTurf = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.caodi);
		mAppleRed = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.applered);
		mSnakeHead = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.snakehead);
		mTileHeight = mTileWidth = mGreenStar.getWidth();
		statusBefore = SnakeView.NORTH;
		setBackgroundResource(R.drawable.background);
	}

	public void generateRandomApple() {
		// TODO Auto-generated method stub
		Position p = null;
		while (true) {
			int x = (int) (Math.random() * (mHorizonTileNum - 2) + 1);
			int y = (int) (Math.random() * (mVerticalTileNum - 2) + 1);
			p = new Position(x, y, SnakeView.APPlE, this);
			if (!mSnakeBody.contains(p)) {
				break;
			}
				
		}
		mApple = p;
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mHorizonTileNum = w / mTileWidth;
		mVerticalTileNum = h / mTileHeight;

		mSnakeBody = new LinkedList<Position>();
		mSnakeBody.addFirst(new Position(mHorizonTileNum / 2,
				mVerticalTileNum / 2 - 2, SnakeView.HEAD, this));
		mSnakeBody.add(new Position(mHorizonTileNum / 2,
				mVerticalTileNum / 2 - 1, SnakeView.BODY, this));
		mSnakeBody.add(new Position(mHorizonTileNum / 2,
				mVerticalTileNum / 2 - 0, SnakeView.BODY, this));
		mSnakeBody.add(new Position(mHorizonTileNum / 2,
				mVerticalTileNum / 2 + 1, SnakeView.BODY, this));

		mWall = new HashSet<Position>();
		for (int i = 0; i < mHorizonTileNum; i++) {
			mWall.add(new Position(i, 0, SnakeView.WALL, this));
			mWall.add(new Position(i, mVerticalTileNum - 1,
					SnakeView.WALL, this));
		}
		for (int i = 0; i < mVerticalTileNum; i++) {
			mWall.add(new Position(0, i, SnakeView.WALL, this));
			mWall.add(new Position(mHorizonTileNum - 1, i,
					SnakeView.WALL, this));
		}
		
		generateRandomApple();

		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// Paint paint = new Paint();
		//drawWall(canvas);
		drawApple(canvas);
		drawSnakeBody(canvas);
		if (mTimeToStartGame) {
			// Game Start.
			handler = new SnakeHandler(this);
			playThread = new SnakeHandlerThread(handler, mSnake);
			playThread.start();
			mTimeToStartGame = false;
		}

	}
	
	private void drawApple(Canvas canvas) {
		// TODO Auto-generated method stub
		mApple.draw(canvas);
	}

	public boolean isGameEnd(Position p) {
		
		return mSnakeBody.contains(p) || p.x < 0 || p.x > mHorizonTileNum - 1 || p.y < 0 || p.y > mVerticalTileNum - 1;
	}

	private void drawSnakeBody(Canvas canvas) {
		// TODO Auto-generated method stub
		for (Position p : mSnakeBody) {
			if (p != null) {
				if (p == mSnakeBody.getFirst())
					p.setType(SnakeView.HEAD);
				else
					p.setType(SnakeView.BODY);
				p.draw(canvas);
			}
		}
		mSnakeBody.getFirst().draw(canvas);
	}

	private void drawWall(Canvas c) {
		// TODO Auto-generated method stub
		for (Iterator<Position> iterator = mWall.iterator(); iterator.hasNext();) {
			iterator.next().draw(c);
		}
	}
}

class Position {
	int x;
	int y;
	int type;
	SnakeView snakeViewRef = null;

	public Position(int x, int y, int positionType, SnakeView v) {
		this.snakeViewRef = v;
		this.x = x;
		this.y = y;
		this.type = positionType;
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		switch (type) {
		case SnakeView.BODY:
			canvas.drawBitmap(snakeViewRef.mGreenStar, snakeViewRef.mTileWidth * x, snakeViewRef.mTileHeight * y, paint);
			break;
		case SnakeView.HEAD:
			canvas.drawBitmap(snakeViewRef.mSnakeHead, snakeViewRef.mTileWidth * x, snakeViewRef.mTileHeight * y, paint);
			break;
		case SnakeView.WALL:
			canvas.drawBitmap(snakeViewRef.mTurf, snakeViewRef.mTileWidth * x, snakeViewRef.mTileHeight * y, paint);
			break;
		case SnakeView.APPlE:
			canvas.drawBitmap(snakeViewRef.mAppleRed, snakeViewRef.mTileWidth * x, snakeViewRef.mTileHeight * y, paint);
			break;
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		super.equals(o);
		if (!(o instanceof Position))
			return false;
		Position p = (Position) o;
		if (this.x == p.x && this.y == p.y)
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return x * 37 + y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}

class SnakeHandler extends Handler {
	SnakeView mView = null;
	LinkedList<Position> snakeBody = null;

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		
		int padOrientation = 0;
		if (msg != null)
			padOrientation = msg.arg1;
		
		switch (padOrientation) {
		case Snake.PAD_ORIENTATION_EAST:
			if (mView.statusBefore != SnakeView.WEST)
				mView.statusBefore = SnakeView.EAST;
			break;
		case Snake.PAD_ORIENTATION_SOUTH:
			if (mView.statusBefore != SnakeView.NORTH)
				mView.statusBefore = SnakeView.SOUTH;
			break;
		case Snake.PAD_ORIENTATION_WEST:
			if (mView.statusBefore != SnakeView.EAST)
				mView.statusBefore = SnakeView.WEST;
			break;
		case Snake.PAD_ORIENTATION_NORTH:
			if (mView.statusBefore != SnakeView.SOUTH)
				mView.statusBefore = SnakeView.NORTH;
			break;
		case Snake.PAD_ORIENTATION_EAST_SOUTH:
			if (mView.statusBefore == SnakeView.EAST) {
				mView.statusBefore = SnakeView.SOUTH;
			} else if (mView.statusBefore == SnakeView.SOUTH) {
				mView.statusBefore = SnakeView.EAST;
			} else if (mView.statusBefore == SnakeView.WEST) {
				mView.statusBefore = SnakeView.SOUTH;
			} else if (mView.statusBefore == SnakeView.NORTH) {
				mView.statusBefore = SnakeView.EAST;
			}
			break;
		case Snake.PAD_ORIENTATION_EAST_NORTH:
			if (mView.statusBefore == SnakeView.EAST) {
				mView.statusBefore = SnakeView.NORTH;
			} else if (mView.statusBefore == SnakeView.SOUTH) {
				mView.statusBefore = SnakeView.EAST;
			} else if (mView.statusBefore == SnakeView.WEST) {
				mView.statusBefore = SnakeView.NORTH;
			} else if (mView.statusBefore == SnakeView.NORTH) {
				mView.statusBefore = SnakeView.EAST;
			}
			break;
		case Snake.PAD_ORIENTATION_WEST_SOUTH:
			if (mView.statusBefore == SnakeView.EAST) {
				mView.statusBefore = SnakeView.SOUTH;
			} else if (mView.statusBefore == SnakeView.SOUTH) {
				mView.statusBefore = SnakeView.WEST;
			} else if (mView.statusBefore == SnakeView.WEST) {
				mView.statusBefore = SnakeView.SOUTH;
			} else if (mView.statusBefore == SnakeView.NORTH) {
				mView.statusBefore = SnakeView.WEST;
			}
			break;
		case Snake.PAD_ORIENTATION_WEST_NORTH:
			if (mView.statusBefore == SnakeView.EAST) {
				mView.statusBefore = SnakeView.NORTH;
			} else if (mView.statusBefore == SnakeView.SOUTH) {
				mView.statusBefore = SnakeView.WEST;
			} else if (mView.statusBefore == SnakeView.WEST) {
				mView.statusBefore = SnakeView.NORTH;
			} else if (mView.statusBefore == SnakeView.NORTH) {
				mView.statusBefore = SnakeView.WEST;
			}
			break;
		case Snake.PAD_ORIENTATION_STILL:
			break;
		default:
			break;
		}
		Position p = null;
		Position temp = null;
		switch (mView.statusBefore) {
		case SnakeView.EAST:
			p = snakeBody.getFirst();
			temp = new Position(p.x + 1, p.y, SnakeView.BODY, mView);
			ifGameOverShowDialog(temp);
			snakeBody.addFirst(temp);
			
			if (!temp.equals(mView.mApple)) {
				snakeBody.removeLast();
			} else {
				mView.generateRandomApple();
				mView.mSnake.mScore = mView.mSnake.mScore
						+ mView.mSnakeBody.size()
						* (1000 - mView.mSnake.mInterval);
				int interval = mView.mSnake.mInterval - mView.mSnake.mInterval / 10;
				if (interval >= 30) {
					mView.mSnake.mInterval = interval;
				} else {
					mView.mSnake.mInterval = 30;
				}
				
			}
			break;
		case SnakeView.SOUTH:
			p = snakeBody.getFirst();
			temp = new Position(p.x, p.y + 1, SnakeView.BODY, mView);
			ifGameOverShowDialog(temp);
			snakeBody.addFirst(temp);
			
			if (!temp.equals(mView.mApple)) {
				snakeBody.removeLast();
			} else {
				mView.generateRandomApple();
				mView.mSnake.mScore = mView.mSnake.mScore
				+ mView.mSnakeBody.size()
				* (1000 - mView.mSnake.mInterval);
				int interval = mView.mSnake.mInterval - mView.mSnake.mInterval / 10;
				if (interval >= 30) {
					mView.mSnake.mInterval = interval;
				} else {
					mView.mSnake.mInterval = 30;
				}
			}
			break;
		case SnakeView.WEST:
			p = snakeBody.getFirst();
			temp = new Position(p.x - 1, p.y, SnakeView.BODY, mView);
			ifGameOverShowDialog(temp);
			snakeBody.addFirst(temp);
			
			if (!temp.equals(mView.mApple)) {
				snakeBody.removeLast();
			} else {
				mView.generateRandomApple();
				mView.mSnake.mScore = mView.mSnake.mScore
				+ mView.mSnakeBody.size()
				* (1000 - mView.mSnake.mInterval);
				int interval = mView.mSnake.mInterval - mView.mSnake.mInterval / 10;
				if (interval >= 30) {
					mView.mSnake.mInterval = interval;
				} else {
					mView.mSnake.mInterval = 30;
				}
			}
			break;
		case SnakeView.NORTH:
			p = snakeBody.getFirst();
			temp = new Position(p.x, p.y - 1, SnakeView.BODY, mView);
			ifGameOverShowDialog(temp);
			snakeBody.addFirst(temp);
			
			if (!temp.equals(mView.mApple)) {
				snakeBody.removeLast();
			} else {
				mView.generateRandomApple();
				mView.mSnake.mScore = mView.mSnake.mScore
				+ mView.mSnakeBody.size()
				* (1000 - mView.mSnake.mInterval);
				int interval = mView.mSnake.mInterval - mView.mSnake.mInterval / 10;
				if (interval >= 30) {
					mView.mSnake.mInterval = interval;
				} else {
					mView.mSnake.mInterval = 30;
				}
			}
		default:
			break;
		}
		mView.invalidate();
	}

	
	public SnakeHandler(View v) {
		mView = (SnakeView) v;
		snakeBody = mView.mSnakeBody;
	}
	
	public void ifGameOverShowDialog(Position p) {
		if (mView.isGameEnd(p)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mView.mSnake);
			String score = "Your points: " + (mView.mSnake.mScore);
			builder.setTitle(score);			
			builder.setMessage("Highest: ");
			builder.setPositiveButton("Try Again!!", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(mView.mSnake, Snake.class);
					mView.mSnake.startActivity(intent);
					mView.mSnake.finish();
				}
			});
			builder.setNegativeButton("Exit", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					mView.mSnake.finish();
				}
			});
			builder.show();
			mView.playThread.makeItStop();
		}
	}

}


class SnakeHandlerThread extends Thread {
	public SnakeHandler handler = null;
	public boolean stopFlag = true;
	public Snake SnakeRef = null;

	public SnakeHandlerThread(Handler h, Context c) {
		handler = (SnakeHandler) h;
		SnakeRef = (Snake) c;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			while (stopFlag) {
				int padOrientation = SnakeRef.mPadOrientation;
				Message msg = handler.obtainMessage();
				msg.arg1 = padOrientation;
				handler.sendMessage(msg);
				Thread.sleep(SnakeRef.mInterval);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void makeItStop() {
		this.stopFlag = false;
	}

}
