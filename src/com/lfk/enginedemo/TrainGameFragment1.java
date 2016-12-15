package com.lfk.enginedemo;

import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import com.example.justwetest.HungryShark.R;
import com.lfk.justweengine.Anim.FrameAnimation;
import com.lfk.justweengine.Anim.VelocityAnimation;
import com.lfk.justweengine.Anim.ZoomAnimation;
import com.lfk.justweengine.Drawable.Button.OnClickListener;
import com.lfk.justweengine.Drawable.Button.TextureButton;
import com.lfk.justweengine.Drawable.Button.ZoomCenterButtonAnim;
import com.lfk.justweengine.Drawable.Sprite.BaseSprite;
import com.lfk.justweengine.Drawable.Sprite.BaseSub;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Engine.SimpleEngine;
import com.lfk.justweengine.Engine.TouchMode;
import com.lfk.justweengine.Info.UIdefaultData;


public class TrainGameFragment1 extends SimpleEngine {
    private static final boolean DEBUG = false;

    private static final int TEMPLATE_TRARIN_TIME = 5;//训练时间（单位分钟）
	private static final float SHIP_ZOOM_MAX = 2.0f;//缩放最大值
	private static final float SHIP_ZOOM_MIN = 0.2f;//缩放最小值
	private static final float SHIP_ZOOM_SPACE = 0.5f;//每次缩放多少
	private static final float SHIP_ZOOM_STEP = 0.05f;//缩放动画的步进
	private static final int   SHIP_ZOOM_COUNT = 3;//大于多少个的时候触发缩放动画
	
	
	
    GameTextPrinter printer;
    Paint paint;
    Canvas canvas;
    GameTimer timer, enemyTimer;//, shoottimer
    String strTimerElapse;
    Bitmap backGround2X;
    BaseSprite titleLeft,titleCenter,titleRight;
    TextureButton btnCancel;
    TextureButton btnPause;
    BaseSprite ship;
    float startX, startY, offsetX, offsetY;
    Rect bg_rect;
    Rect bg_rect_full;
    Rect bg_rect_dest;
    Point bg_scroll;
//    GameTexture shoot;
    GameTexture enemyPic01,enemyPic02,enemyPic03;
    Random random;
    final int SHIP = 0;
    final int BULLET = 1;
    final int ENEMY = 2;
    final int TITLE = 3;

    int EnemyNum = 0;    
    int killEnemyNum = 0;
    
    int m_continueKillEnemyNum = 0;
    int m_continueMissEnemyNum = 0;
//    final float PI = 3.14159f;

	private Activity mContext;
	private MainActivity mParentActivity;
	

	public int m_nItemTotalTime = 0;//单元训练总时间
	public long m_currItemTime = 0;	//单元训练当前时间
	public long m_beginTime = 0;	//开始时间
	
	private boolean m_bIsFinish = false;
	
	private float m_fCurrShipScale = 0.0f;

    public TrainGameFragment1() {
        super(false);
        paint = new Paint();
        canvas = null;
       // shoottimer = new GameTimer();
        timer = new GameTimer();
        random = new Random();
        enemyTimer = new GameTimer();
    }

    @Override
    public void init() {
        UIdefaultData.init(this.getActivity());
        mContext = this.getActivity();
        mParentActivity = ((MainActivity) mContext);
		initData();
        return;
    }

    @Override
    public void load() {
        printer = new GameTextPrinter();
        printer.setTextColor(Color.WHITE);
        printer.setTextSize(40);
        printer.setLineSpaceing(28);
        printer.setTextFont(this.getActivity(),"fonts/Structr Bold.ttf");

        loadMainActor();

        loadTitlePic();
        
        loadButtons();
        
        loadBackGroud();

        loadEnemy();

//        shoot = new GameTexture(this.getActivity());
//        shoot.loadFromAsset("pic/flare.png");
//播放背景音
        dobBroadcast(R.raw.backgroundmusic_shark);
    }

    void loadMainActor()
    {
        // load ship
      GameTexture shipTexture = new GameTexture(this.getActivity());
        shipTexture.loadFromAsset("game/shark/pic/shark.png");
//        texture1.loadFromAssetStripFrame("pic/shark4.png", 0, 0, 1024, 2048);
        ship = new BaseSprite(this, 400, 196, 5);
        ship.setTexture(shipTexture);
        ship.setPosition(100, 200);
        m_fCurrShipScale = 1.0f;
        ship.setScale(m_fCurrShipScale);
//        ship.setRotation(PI);
//        ship.setRotationAnchor(ship.getWidth()/2,ship.getHeight()/2);
        ship.setAlpha(0xee);
        ship.setName("SHIP");
        ship.setIdentifier(SHIP);
        ship.addAnimation(new FrameAnimation(0, 23, 1,60));

//        ship.addfixedAnimation("start",
//                new MoveAnimation(UIdefaultData.centerInHorizontalX - ship.getWidthWithScale() / 2,
//                        UIdefaultData.screenHeight - 2 * ship.getHeightWidthScale(), new Float2(10, 10)));
     // 为状态机添加一个任务
//        ship.addState(new StateFinder() {
//            @Override
//            public boolean isContent(BaseSub baseSub) {
//                return Game.this.killEnemyNum > 5;
//            }
//        }, new FrameAnimation(0, 63, 1));
        
        addToSpriteGroup(ship);
    }
    
    void loadTitlePic()
    {
        //load title
        GameTexture texLeft = new GameTexture(this.getActivity());
        texLeft.loadFromAsset("game/shark/pic/time.png"); 
        titleLeft = new BaseSprite(this);
        titleLeft.setTexture(texLeft);
        titleLeft.setIdentifier(TITLE);
        titleLeft.setScale(0.8f);
//        titleLeft.setRotation((PI/2.0f));
        titleLeft.setPosition(10, 20);
        titleLeft.setCollidable(false);
        addToSpriteGroup(titleLeft);
        
        GameTexture texCenter = new GameTexture(this.getActivity());
        texCenter.loadFromAsset("game/shark/pic/gamename.png");
        titleCenter = new BaseSprite(this);
        titleCenter.setTexture(texCenter);
        titleCenter.setIdentifier(TITLE);
        titleCenter.setScale(0.6f);
//        titleCenter.setRotation((PI/2.0f));
        titleCenter.setPosition(UIdefaultData.centerInHorizontalX - 150,10);
        titleCenter.setCollidable(false);
        addToSpriteGroup(titleCenter);        
   
        GameTexture texRight = new GameTexture(this.getActivity());
        texRight.loadFromAsset("game/shark/pic/score.png");
        titleRight = new BaseSprite(this);
        titleRight.setTexture(texRight);
        titleRight.setIdentifier(TITLE);
        titleRight.setScale(0.8f);
//        titleRight.setRotation((PI/2.0f));
        titleRight.setPosition(UIdefaultData.screenWidth-100, 20);
        titleRight.setCollidable(false);
        addToSpriteGroup(titleRight);  
    }

    void loadButtons()
    {
        btnPause = new TextureButton(this, "btnPause");
        GameTexture btntexture2 = new GameTexture(this.getActivity());
        btntexture2.loadFromAsset("game/shark/pic/PauseNormal.png");
        // 添加图片资源
        btnPause.setTexture(btntexture2);
        // button的接口回调，不是View的那个接口
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
    			handler.sendEmptyMessage(0);
            }
          });
        btnPause.setPosition(UIdefaultData.screenWidth-280, UIdefaultData.screenHeight-btntexture2.getBitmap().getHeight()-150);
        btnPause.setScale(1.0f,1.0f);
//        btnPause.setRotation(PI/2);
        btnPause.setRotationAnchor(40, 40);
//        btnPause.setDipScale(40, 40);
        // 设定中心放缩
        btnPause.setZoomCenter(true);
        // 三个参数 初始值／放大值／帧数
        btnPause.setAnimation(new ZoomCenterButtonAnim(10, 30, 3));
        // 添加到ButtonGroup进行绘制和处理
        addToButtonGroup(btnPause);

        btnCancel = new TextureButton(this, "btnCancel");
        GameTexture btntexture = new GameTexture(this.getActivity());
        btntexture.loadFromAsset("game/shark/pic/CloseNormal.png");
        // 添加图片资源
        btnCancel.setTexture(btntexture);
        // button的接口回调，不是View的那个接口
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
            	handler.sendEmptyMessage(1);
            }
          });
        btnCancel.setPosition(UIdefaultData.screenWidth-150,UIdefaultData.screenHeight-btntexture.getBitmap().getHeight()-150);
        btnCancel.setScale(1.0f,1.0f);
//        button.setDipScale(80, 80);
        // 设定中心放缩
        btnCancel.setZoomCenter(true);
        // 三个参数 初始值／放大值／帧数
        btnCancel.setAnimation(new ZoomCenterButtonAnim(1, 30, 1));
        // 添加到ButtonGroup进行绘制和处理
        addToButtonGroup(btnCancel);
        
//        setTouchMode(TouchMode.BUTTON);
        setTouchMode(TouchMode.SINGLE_BUTTON);
    }
    
    void loadBackGroud()
    {
        // load bg
        GameTexture tex = new GameTexture(this.getActivity());
        if (!tex.loadFromAsset("game/shark/pic/backgroundDn.png")) {
            fatalError("Error loading space");
        }
        float fRatio = (float)(UIdefaultData.screenHeight)/(float)(tex.getBitmap().getHeight());
        bg_rect_full = new Rect(0, 0 ,(int)(tex.getBitmap().getWidth()*fRatio), UIdefaultData.screenHeight);

        backGround2X = Bitmap.createBitmap(
        		bg_rect_full.width()*2,
        		bg_rect_full.height(),
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(backGround2X);
        Rect dst = new Rect(0, 0, bg_rect_full.width(),bg_rect_full.height());
        canvas.drawBitmap(tex.getBitmap(), null, dst, null);
        dst = new Rect(bg_rect_full.width(),0, 
        		bg_rect_full.width()*2,bg_rect_full.height());
        canvas.drawBitmap(tex.getBitmap(), null, dst, null);
//        bg_rect = new Rect(0, 0, UIdefaultData.screenWidth, UIdefaultData.screenHeight);
        bg_rect = new Rect(0, 0, bg_rect_full.width(), bg_rect_full.height());
        bg_rect_dest = new Rect(0, 0, UIdefaultData.screenWidth, UIdefaultData.screenHeight);
        bg_scroll = new Point(0, 0);
    }
    
    void loadEnemy()
    {
        enemyPic01 = new GameTexture(this.getActivity());
        enemyPic01.loadFromAsset("game/shark/pic/fish_01.png");
        enemyPic02 = new GameTexture(this.getActivity());
        enemyPic02.loadFromAsset("game/shark/pic/fish_02.png");
        enemyPic03 = new GameTexture(this.getActivity());
        enemyPic03.loadFromAsset("game/shark/pic/fish_03.png");
    }


    @Override
    public void draw() {
        canvas = super.getCanvas();
        canvas.drawBitmap(backGround2X, bg_rect, bg_rect_dest, paint);

        printer.setCanvas(canvas);
//        printer.drawText("Engine demo", 10, 20);
        printer.drawText(strTimerElapse, 80, 60, 0);
        printer.drawText(String.valueOf(killEnemyNum*10), UIdefaultData.screenWidth-160, 60, 0);
    }

    @Override
    public void update() {
		if(m_bIsFinish)
		{
			return;
		}
		if(m_currItemTime >= m_nItemTotalTime)
		{
			m_bIsFinish  = true;
			handler.sendEmptyMessage(2);
			return;
		}
    	
//    	nEnemyPosX = random.nextInt(UIdefaultData.screenWidth-200);
        if (timer.stopWatch(20)) {
            scrollBackground();
        }
        long nTime = timer.getElapsed();
        m_currItemTime = (int) ((nTime-m_beginTime)/1000);
        strTimerElapse = getTimeDuration(nTime);
//        if (ship.getFixedAnimation("start").animating) {
//            ship.fixedAnimation("start");
//        } else 
        {
//            fireBullet();
//            int size = getTypeSizeFromRecycleGroup(ENEMY);
//            if (size > 0)
//                enemyNum -= size;
            if (enemyTimer.stopWatch(1000)) {
	            int py = random.nextInt(100);
	            int height = bg_rect_full.height();
	        	int nPY = (int) ((float)(py/100.0f)*height);
        		nPY = bg_rect_full.height()-nPY-150;
        		addFish(nPY);
            }
        }

        for(int i=0;i<getSpriteGroupSize();i++)
        {
        	BaseSub sprite = getSpriteGroup().get(i);
        	if(sprite.getIdentifier()==ENEMY && sprite.getAlive())
        	{
                if(sprite.s_position.x < 0){
                	sprite.setAlive(false);
                	m_continueMissEnemyNum++;
                	m_continueKillEnemyNum = 0;
                }
                float oriScale = m_fCurrShipScale;
                if(m_continueMissEnemyNum>=SHIP_ZOOM_COUNT)
                {
                	m_continueMissEnemyNum = 0;
                	if(m_fCurrShipScale+SHIP_ZOOM_SPACE <= SHIP_ZOOM_MAX)
                	{
                		m_fCurrShipScale += SHIP_ZOOM_SPACE;
                		ship.addAnimation(new ZoomAnimation(oriScale, m_fCurrShipScale, SHIP_ZOOM_STEP));
                	}
           	    	return;
                }
        	}
        }
        
     }

//    public void fireBullet() {
//        if (!shoottimer.stopWatch(300)) return;
//        BaseSprite bullet;
//        if (getTypeSizeFromRecycleGroup(BULLET) > 0) {
//            bullet = (BaseSprite) recycleSubFromGroup(BULLET);
//            bullet.clearAllAnimation();
//            removeFromRecycleGroup(bullet);
//        } else {
//            bullet = new BaseSprite(this);
//            bullet.setTexture(shoot);
//            bullet.setDipScale(8, 18);
//            bullet.setIdentifier(BULLET);
//        }
//        double angle = 270.0;
//        float speed = 20.0f;
//        int lifetime = 2500;
//        bullet.addAnimation(new VelocityAnimation(angle, speed,
//                lifetime));
//        bullet.setPosition(ship.s_position.x +
//                        ship.getWidthWithScale() / 2
//                        - bullet.getWidthWithScale() / 2,
//                ship.s_position.y - 24);
//        bullet.setAlive(true);
//        addToSpriteGroup(bullet);
//    }

    private void addFish01(int nPosY) {
    	BaseSprite enemy;
        enemy = new BaseSprite(this,80,53,12);
        enemy.setTexture(enemyPic01);
        enemy.setIdentifier(ENEMY);
        enemy.setScale(1.0f);
//            enemy.setRotation(PI);
//            enemy.setRotationAnchor(enemy.getWidthWithScale()/2, enemy.getHeightWidthScale()/2);
//            enemy.addAnimation(new IntMoveAnimation(nPosX,0,new Point(50,5)));
        enemy.addAnimation(new FrameAnimation(0, 12, 1,30));
       	double angle = 180.0;
       	float speed = 5.0f;
       	int lifetime = 30000;
       	enemy.addAnimation(new VelocityAnimation(angle, speed, lifetime));
       	enemy.setPosition(bg_rect_dest.width(), nPosY);
        enemy.setAlive(true);
        addToSpriteGroup(enemy);
        EnemyNum++;
    }

    private void addFish02(final int nPosY) {
        BaseSprite enemy;
        enemy = new BaseSprite(this,81,41,12);
        enemy.setTexture(enemyPic02);
        enemy.setIdentifier(ENEMY);
        enemy.setScale(1.0f);
//        enemy.setPosition(100, bg_rect_dest.height());
//            enemy.setRotation(PI);
//            enemy.setRotationAnchor(enemy.getWidthWithScale()/2, enemy.getHeightWidthScale()/2);
//        enemy.addAnimation(new IntMoveAnimation(nPos,0,new Point(50,5)));
        enemy.addAnimation(new FrameAnimation(0, 15, 1,30));
        
        double angle = 180.0;
        float speed = 5.0f;
        int lifetime = 30000;
       	enemy.addAnimation(new VelocityAnimation(angle, speed, lifetime));
       	enemy.setPosition(bg_rect_dest.width(), nPosY);
        enemy.setAlive(true);
        addToSpriteGroup(enemy);
        EnemyNum++;
    }

    private void addFish03(int nPosY) {
        BaseSprite enemy;
        enemy = new BaseSprite(this,81,81,12);
        enemy.setTexture(enemyPic03);
        enemy.setIdentifier(ENEMY);
        enemy.setScale(1.0f);
        enemy.setPosition(100, 500);
//        enemy.setRotation(PI/2);
//        enemy.setRotationAnchor(40, 40);
//            enemy.addAnimation(new IntMoveAnimation(nPosX,bg_rect_dest.height(),new Point(50,5)));
        enemy.addAnimation(new FrameAnimation(0, 19, 1,30));
        double angle = 180.0;
        float speed = 5.0f;
        int lifetime = 30000;
       	enemy.addAnimation(new VelocityAnimation(angle, speed, lifetime));
       	enemy.setPosition(bg_rect_dest.width(), nPosY);
        enemy.setAlive(true);
        addToSpriteGroup(enemy);
        EnemyNum++;
    }

     private void addFish(int nPosY) {
    	int nType = random.nextInt(3);
    	switch (nType) {
		case 0:
			addFish01(nPosY);
			break;
		case 1:
			addFish02(nPosY);
			break;
		case 2:
			addFish03(nPosY);
			break;

		default:
			addFish01(nPosY);
			break;
		}
    	
    }
    
    @Override
    public void touch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX() - startX;
                offsetY = event.getY() - startY;
                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    if (ship.s_position.x + offsetX > 0
                            && ship.s_position.x + offsetX +
                            ship.getHeightWidthScale() < UIdefaultData.screenWidth) {
                        ship.s_position.x += offsetX;
                        resetEvent(event);
                    }
                } else {
                    if (ship.s_position.y + offsetY > 0
                            && ship.s_position.y + offsetY +
                            ship.getHeightWidthScale() < UIdefaultData.screenHeight) {
                        ship.s_position.y += offsetY;
                        resetEvent(event);
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
        }
    }

    @Override
    public void collision(BaseSub baseSub) {
        BaseSprite other = (BaseSprite) baseSub.getOffender();
        if (baseSub.getIdentifier() == SHIP &&
                other.getIdentifier() == ENEMY) {
            other.setAlive(false);
            removeFromSpriteGroup(other);
            addToRecycleGroup(other);
            killEnemyNum++;

            m_continueKillEnemyNum++;
        	m_continueMissEnemyNum = 0;
        	float oriScale = m_fCurrShipScale;
        	if(m_continueKillEnemyNum>=SHIP_ZOOM_COUNT)
            {
            	m_continueKillEnemyNum = 0;
            	if(m_fCurrShipScale-SHIP_ZOOM_SPACE>=SHIP_ZOOM_MIN)
            	{
                	m_fCurrShipScale -= SHIP_ZOOM_SPACE;
                	if(m_fCurrShipScale<=0.1)
                	{
                		m_fCurrShipScale = 0.1f;
                	}
                	ship.addAnimation(new ZoomAnimation(oriScale, m_fCurrShipScale, SHIP_ZOOM_STEP));
            	}
            }
        }
    }

    private void resetEvent(MotionEvent event) {
        startX = (int) event.getX();
        startY = (int) event.getY();
    }

    public void scrollBackground() {
        bg_scroll.x += 2.0f;
        bg_rect.left = bg_scroll.x;
        bg_rect.right = bg_rect.left + UIdefaultData.screenWidth - 1;
        if (bg_scroll.x + bg_rect_full.width() > backGround2X.getWidth()) {
        	bg_scroll.x = 0;
        }
    }

	@Override
	public void debugDraw(RectF bound) {
		if(DEBUG)
		{
			Paint debugPaint = new Paint();
			debugPaint.setColor(Color.RED);
			debugPaint.setStyle(Paint.Style.STROKE);
	        canvas.drawRect(bound, debugPaint);
		}
	}

	public static String getTimeDuration(long millisecond)
	{
		String time = "";
		long sec = (millisecond/1000);
		
		if(sec / 3600 >= 1)
		{
			  time += (sec / 3600) + ":";
		}
		
		sec = sec % 3600;
		if(sec / 60 >= 0 && sec / 3600 < 1)
		{
			if((sec / 60)<10)
			{
				time += "0"+(sec / 60) + ":";	
			}
			else
			{
				time += (sec / 60) + ":";
			}
		}
		sec = sec % 60;
		if(sec / 60 < 1)
		{
			if(sec / 10 < 1)
			{
				time += "0"+sec;
			}
			else
			{
				time += sec;
			}
		}
		
		return time;
	}
	
    private Boolean initData() { 
    	m_nItemTotalTime = TEMPLATE_TRARIN_TIME*60;
//        String strFileName = ((UI_Train_Root) mContext).m_CurrItem.kegelFileNameString;
//        if(strFileName==null || strFileName.isEmpty())
//        {
//        	return false;
//        }
//    	if(!readTemplateFile(strFileName))
//    	{
//    		return false;
//    	}
    	

        return true;
    }
    
 	/**
	 * 播放音频文件
	 * @param index
	 */
	private MediaPlayer mp = null;
	public void dobBroadcast(int resID)
	{
		try
		{
			if(mp!=null)
			{
				if(mp.isPlaying())
				{
					mp.stop();	
				}
				mp.release();
				mp = null;
			}

			mp = MediaPlayer.create(mContext, resID);
			mp.setLooping(true);
			mp.start();
			
//			mp.setOnCompletionListener(new OnCompletionListener()
//			{
//				@Override
//				public void onCompletion(MediaPlayer mp)
//				{
//					mp.release();
//					mp = null;
//				}
//			});
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}	
	
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	setPauseEngin(true);
        	if(msg.what == 0)//暂停
        	{
        		mParentActivity.pauseTrain();
        	}
        	if(msg.what == 1)//终止
        	{
        		mParentActivity.breakTrain();
        	}
        	if(msg.what == 2)//结束训练
        	{
        		mParentActivity.endTrain();
        		releaseRes();
        	}

        }
    };

	@Override
	public void onStart() {
		m_beginTime = timer.getElapsed();
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	
  	public void pauseSound()
 	{
		if(mp!=null)
		{
			if(mp.isPlaying())
			{
				mp.pause();	
			}
		}
 	}
 	
 	public void resumSound()
 	{
		if(mp!=null)
		{
			mp.start();
		}
 	}
 	
 	void releaseRes()
 	{
		mContext = null;
		backGround2X = null;
		enemyPic01 = null;
		enemyPic02 = null;
		enemyPic03 = null;
		
        paint = null;
        canvas = null;
        timer = null;
        random = null;
        enemyTimer = null;

 		if(mp!=null)
		{
			if(mp.isPlaying())
			{
				mp.stop();
			}
			mp.release();
			mp = null;
		}	
 	}
 	
}
