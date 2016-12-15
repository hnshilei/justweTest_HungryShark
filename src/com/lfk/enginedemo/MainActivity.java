package com.lfk.enginedemo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;


import com.example.justwetest.HungryShark.R;
import com.lfk.widget.MessageDialog;
import com.lfk.widget.MessageDialog.MyOnClickListener;





/**
 * Created by MLD_shilei on 2016/10/8.
 */
public class MainActivity extends FragmentActivity {

	
    Button btn_test;
    TrainGameFragment1 frag;
    
	MessageDialog dlg_pause;//训练暂停对话框
	MessageDialog dlg_break;//训练中断
	MessageDialog dlg_stop;	//训练结束

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_test = (Button) findViewById(R.id.btn_ok);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameFrag();
            }
        });
        
		DialogInterface.OnKeyListener mylistener = new DialogInterface.OnKeyListener() {
			   @Override
			   public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
			   {
			   if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
			    {
			     //操作
			    }
			   return true;
			   }
			  };
	    dlg_pause = new MessageDialog(this,"暂停中...","继续");
	    dlg_pause.setCanceledOnTouchOutside(false);
	    dlg_pause.setOnKeyListener(mylistener);
	    dlg_pause.setOnClickListener(new MyOnClickListener() {
			@Override
			public void onClick(int nID) {
				MainActivity.this.continueTrain();
			}
		});
	    dlg_break = new MessageDialog(this,"结束训练","确定","取消");
	    dlg_break.setCanceledOnTouchOutside(false);
	    dlg_break.setOnKeyListener(mylistener);
	    dlg_break.setOnClickListener(new MyOnClickListener() {
			@Override
			public void onClick(int nID) {
				switch (nID) {
				case R.id.btn_cancel:
					MainActivity.this.continueTrain();
					break;
				case R.id.btn_ok:
					MainActivity.this.finish();
					break;
				default:
					break;
				}
			}
		});
	    
	    dlg_stop = new MessageDialog(this,"您的治疗已完成","确定");
	    dlg_stop.setCanceledOnTouchOutside(false);
	    dlg_stop.setOnKeyListener(mylistener);
	    dlg_stop.setOnClickListener(new MyOnClickListener() {
			@Override
			public void onClick(int nID) {
				MainActivity.this.finish();					
			}
		});
		

    }

    private void showGameFrag()
    {
        frag = new TrainGameFragment1();
        switchToFragment(R.id.ly_train_content,frag,false);
    }
    
    public void switchToFragment(int containerViewId , Fragment dfragment, boolean isAddedStack){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.add(containerViewId , dfragment);
        ft.replace(containerViewId, dfragment, "center");
        ft.setTransition(FragmentTransaction. TRANSIT_FRAGMENT_OPEN);
        if(isAddedStack)
            ft.addToBackStack(null);
        ft.commit();
    }
    
	public void breakTrain() {
		dlg_break.show();
		pauseTrainNoDialog();
	}

	public void pauseTrain() {
		dlg_pause.show();
		pauseTrainNoDialog();
	}
	
	//训练完成处理
	public void endTrain()
	{
		pauseTrainNoDialog();
		dlg_stop.show();	
	}
	
	public void continueTrain() {
		dlg_pause.hide();
		dlg_break.hide();
		frag.setPauseEngin(false);
		frag.resumSound();
	}

	public void pauseTrainNoDialog() {
		frag.setPauseEngin(true);
		frag.pauseSound();
	}
}
