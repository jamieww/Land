package com.cyclone.land;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAnnounceActivity extends Activity {

	private String landURL = "http://192.168.1.100:8080/webLand/announce_xml.jsp";
	private String postURL = "http://192.168.1.100:8080/webLand/announce.jsp";
	private HttpClient httpClient = new DefaultHttpClient();
	
	private List<Announce> announces;
	private ListView listAnnounceView;
	private TextView showInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("Show Announce Activity start...");
		setContentView(R.layout.show_from_announce);
		
		System.out.println("Show Announce Activity 1");
		loadAnnounceFromServer();
		
		listAnnounceView = (ListView)this.findViewById(R.id.listAnnounce);
		showInfo = (TextView)this.findViewById(R.id.T1);
		listAnnounceView.setAdapter(new MyAdapter(announces));
		registerForContextMenu(listAnnounceView);
		
		listAnnounceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView title = (TextView)arg1.findViewById(R.id.annTitle);
				showInfo.setText(title.getText());
			}
		});
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onContextItemSelected(item);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch(item.getItemId()) {
		case R.id.ann_contextitem1:
			String seqid;
			
			seqid = String.valueOf(info.id);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��NameValuePair������Ҫ���ݵ�Post����
			params.add(new BasicNameValuePair("seqid", seqid));    //���Ҫ���ݵĲ���
			connectPost(postURL, params);
			
			loadAnnounceFromServer();
			listAnnounceView = (ListView)this.findViewById(R.id.listAnnounce);
			listAnnounceView.setAdapter(new MyAdapter(announces));
			
			TextView title = (TextView)info.targetView.findViewById(R.id.annTitle);
			
			Toast.makeText(this, "ɾ���ˡ�" + title.getText().toString() + "��", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.ann_contextmenu, menu);
	}

	public void sendAnnounceToServer(View v) {
		Intent _intent = new Intent(this, SendAnnounceActivity.class);
		startActivityForResult(_intent, 101);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 101 && resultCode == Activity.RESULT_OK) {
			loadAnnounceFromServer();
			listAnnounceView = (ListView)this.findViewById(R.id.listAnnounce);
			listAnnounceView.setAdapter(new MyAdapter(announces));
		}
	}
	
	private void loadAnnounceFromServer() {
		//�������ӳ�ʱ
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
		//�������ݶ�ȡʱ�䳬ʱ
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 30000);
		//���ô����ӳ���ȡ���ӳ�ʱ
		ConnManagerParams.setTimeout(httpClient.getParams(), 10000);
		System.out.println("Show Announce Activity 2");
		
		HttpGet httpget = new HttpGet(landURL);
		try {
			HttpResponse response = httpClient.execute(httpget); 
			System.out.println("Show Announce Activity 3");
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				System.out.println("Show Announce Activity 4");
				announces = parseDate(response.getEntity().getContent()); 
				//String result = EntityUtils.toString(response.getEntity(),"UTF-8");
				//System.out.println(result);
			}
			else {
				//
				System.out.println(response.getStatusLine().toString());
			}
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void connectPost(String url, List<NameValuePair> params){
		HttpClient httpClient = new DefaultHttpClient();    // �½�HttpClient����
		HttpPost httpPost = new HttpPost(url);    // �½�HttpPost����
		
		try {
			 HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);  // �����ַ���
    	     httpPost.setEntity(entity);    // ���ò���ʵ��
    	     HttpResponse httpResp = httpClient.execute(httpPost); // ��ȡHttpResponseʵ��
    		if(httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  //��Ӧͨ��
    			String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");   
    			
    			/*
    			Intent httpclientIntent = new Intent();
    			httpclientIntent.putExtra("content", result);
    			httpclientIntent.setClass(getApplicationContext(), detailActivity.class);
    			startActivity(httpclientIntent);
    			*/
    		}else{
    			//��Ӧδͨ��
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
	
	public List<Announce> parseDate(InputStream inStream) {
		List<Announce> announces = null;
		Announce currentAnnounce = null;

		try {
			/**
			 * android�ṩ��һ��������'Xml'
			 * ͨ�������������Ժܷ����ȥnewһ��Pull�Ľ�����,����������XmlPullParser
			 **/
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();   //����һ��XmlPullParser�����Ĺ���
			factory.setNamespaceAware(true);
			
			XmlPullParser xmlPull = factory.newPullParser(); //��ȡһ������ʵ��
			xmlPull.setInput(inStream, "UTF-8");    //�����������ı����ʽ
			/**
			 * �����¼�getEventType() =�����¼��� ��������ĳ���ַ�,�������xml���﷨�淶. ���ͻ��������﷨�����������
			 **/
			int eventCode = xmlPull.getEventType();

			/**
			 * �����¼�: StartDocument,�ĵ���ʼ Enddocument,�ĵ����� ÿ�ζ���һ���ַ�,�Ͳ���һ���¼�
			 * ֻҪ����xml�ĵ��¼���Ϊ��,��һֱ���¶�
			 **/
			while (eventCode != XmlPullParser.END_DOCUMENT) {
				switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: // �ĵ���ʼ�¼�,������һЩ���ݳ�ʼ������
					announces = new ArrayList<Announce>();
					break;

				case XmlPullParser.START_TAG:// Ԫ�ؿ�ʼ.
					String name = xmlPull.getName();
					if (name.equalsIgnoreCase("announce")) {
						currentAnnounce = new Announce();
						currentAnnounce.setSeqID(Integer.valueOf(xmlPull.getAttributeValue(null, "seqid")));
					} 
					else if (currentAnnounce != null) {
						if (name.equalsIgnoreCase("contenttitle")) {
							currentAnnounce.setContentTitle(xmlPull.nextText());
						} 
						else if (name.equalsIgnoreCase("content")) {
							currentAnnounce.setContent(xmlPull.nextText());
						}
						else if (name.equalsIgnoreCase("edittime")) {
							currentAnnounce.setEditTime(xmlPull.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG: // Ԫ�ؽ���,
					if (currentAnnounce != null
							&& xmlPull.getName().equalsIgnoreCase("announce")) {
						announces.add(currentAnnounce);
						currentAnnounce = null;
					}
					break;
				}
				eventCode = xmlPull.next();// ���뵽һ��һ��Ԫ��.
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return announces;
	}
	
	public class MyAdapter extends BaseAdapter {

		private List<Announce> listAnnounce;
		
		public MyAdapter(List<Announce> listAnnounce) {
			this.listAnnounce = listAnnounce;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAnnounce.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listAnnounce.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return listAnnounce.get(position).getSeqID();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null) {
				LayoutInflater inflater = (LayoutInflater)ShowAnnounceActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View itemView = inflater.inflate(R.layout.ann_item, null);
				TextView title = (TextView)itemView.findViewById(R.id.annTitle);
				title.setText(listAnnounce.get(position).getContentTitle());
				TextView content = (TextView)itemView.findViewById(R.id.annContent);
				content.setText(listAnnounce.get(position).getContent());
				
				return itemView;
			}
			else {
				TextView title = (TextView)convertView.findViewById(R.id.annTitle);
				title.setText(listAnnounce.get(position).getContentTitle());
				TextView content = (TextView)convertView.findViewById(R.id.annContent);
				content.setText(listAnnounce.get(position).getContent());
				
				return convertView;
			}
		}
		
	}

}
