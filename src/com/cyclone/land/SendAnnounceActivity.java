/**
 * 
 */
package com.cyclone.land;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Jamie
 *
 */
public class SendAnnounceActivity extends Activity {

	private String postURL = Announce.HOSTADDR + "/webLand/announce.jsp";
	
	private Context mContext;
	private EditText etTitle;
	private EditText etContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_to_announce);
		
		mContext = this;
	}
	
	public void sendAnnounceContent(View v) {
		etTitle = (EditText)findViewById(R.id.etTitle);
		etContent = (EditText)findViewById(R.id.etContent);
		
		String title = etTitle.getText().toString();
		String content = etContent.getText().toString();
		
		if(title.length() < 1 || content.length() < 1) {
			Toast.makeText(mContext, "标题或内容不允许为空!", 1).show();
			return;
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用NameValuePair来保存要传递的Post参数
		params.add(new BasicNameValuePair("title", title));    //添加要传递的参数
		params.add(new BasicNameValuePair("content", content));
		connectPost(postURL, params);
		
		Intent data = new Intent();
		setResult(Activity.RESULT_OK, data);
		finish();
	}
	
	private void connectPost(String url, List<NameValuePair> params){
		HttpClient httpClient = new DefaultHttpClient();    // 新建HttpClient对象
		HttpPost httpPost = new HttpPost(url);    // 新建HttpPost对象
		
		try {
			 HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);  // 设置字符集
    	     httpPost.setEntity(entity);    // 设置参数实体
    	     HttpResponse httpResp = httpClient.execute(httpPost); // 获取HttpResponse实例
    		if(httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  //响应通过
    			String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");   
    			
    			/*
    			Intent httpclientIntent = new Intent();
    			httpclientIntent.putExtra("content", result);
    			httpclientIntent.setClass(getApplicationContext(), detailActivity.class);
    			startActivity(httpclientIntent);
    			*/
    		}else{
    			//响应未通过
    			System.out.println(httpResp.getStatusLine().toString());
    		}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}

}
