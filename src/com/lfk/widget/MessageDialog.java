package com.lfk.widget;



import com.example.justwetest.HungryShark.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MessageDialog extends Dialog {
	private TextView tv;
	private Button btn_OK;
	private Button btn_CANCEL;
	private MyOnClickListener listener; 
	private String m_strTitle;
	private String m_btnTextOK = null;
	private String m_btnTextCancel = null;
	public interface MyOnClickListener {
	    public void onClick(int nID);  
	}
    /** 
     * 设置具体点击监听器 
     * @param listener 点击监听器实现类 
     */  
    public void setOnClickListener(MyOnClickListener listener) {  
        this.listener = listener;  
    } 
    
  
	
    public MessageDialog(Context context) {
		super(context, R.style.loadingDialogStyle);
	}

	private MessageDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public MessageDialog(Context context, String strTitle, String btnTextOK) {
		super(context, R.style.loadingDialogStyle);
		m_strTitle = strTitle;
		m_btnTextOK = btnTextOK;
	}
	
	public MessageDialog(Context context, String strTitle, String btnTextOK,String btnTextCancel) {
		super(context, R.style.loadingDialogStyle);
		m_strTitle = strTitle;
		m_btnTextOK = btnTextOK;
		m_btnTextCancel = btnTextCancel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_message);
		tv = (TextView)this.findViewById(R.id.tv);  
        tv.setText(m_strTitle);  		
//        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.ly_wait);
//		linearLayout.getBackground().setAlpha(210);
        btn_OK = (Button)this.findViewById(R.id.btn_ok);
        btn_OK.setText(m_btnTextOK);
        btn_OK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v.getId()); 
			}
		});
        if(m_btnTextOK==null)
        {
        	btn_OK.setVisibility(View.GONE);
        }
        
        btn_CANCEL = (Button)this.findViewById(R.id.btn_cancel);
        btn_CANCEL.setText(m_btnTextCancel);
        btn_CANCEL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v.getId()); 
			}
		});
        if(m_btnTextCancel==null)
        {
        	btn_CANCEL.setVisibility(View.GONE);
        }
        
	}
	

}
