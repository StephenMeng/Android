package sai.com.lbm.scanbook;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import sai.com.lbm.R;

/**
 * 笔记详细信息显示界面
 * @author 
 *
 */
public class ReviewContent extends Activity{
	
	private TextView tv_author;
	private TextView tv_content;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.content);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.titlebar);  
		tv_content=(TextView)findViewById(R.id.content_lv_content);
		tv_author= (TextView)findViewById(R.id.content_lv_author);
	
		
		Intent intent=getIntent();
		String content=intent.getStringExtra("content");
		String author=intent.getStringExtra("author");
		tv_content.setText(content);
		tv_author.setText("来自："+author);

	}
}
