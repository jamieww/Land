package com.cyclone.land;

import net.sourceforge.simcpux.Constants;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements IWXAPIEventHandler{

	/* 
	 * ���scene��WXSceneSession��Ĭ��ֵ������ô��Ϣ�ᷢ����΢�ŵĻỰ�ڡ�
	 * ���scene��WXSceneTimeline����ô��Ϣ�ᷢ��������Ȧ��
	 * ��΢��4.2����֧��WXSceneTimeline���ɵ���IWXAPI��getWXAppSupportAPI�������΢�Ű汾,0x21020001������֧�ַ�������Ȧ��
	 * */
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	// IWXAPI �ǵ�����app��΢��ͨ�ŵ�openapi�ӿ�
    private IWXAPI api;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void regToWX(View v) {
		// ͨ��WXAPIFactory��������ȡIWXAPI��ʵ��
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
    	api.registerApp(Constants.APP_ID);
	}
	
	public void sendToWX(View v) {
		String text = "��������";
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;
		
		// ��WXTextObject�����ʼ��һ��WXMediaMessage����
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// �����ı����͵���Ϣʱ��title�ֶβ�������
		// msg.title = "Will be ignored";
		msg.description = text;
		
		// ����һ��Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction�ֶ�����Ψһ��ʶһ������
		req.message = msg;
		//req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		
		// ����api�ӿڷ������ݵ�΢��
		api.sendReq(req);
		finish();
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	// ������Ӧ�÷��͵�΢�ŵ�����������Ӧ�������ص����÷���
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
	
	// ΢�ŷ������󵽵�����Ӧ��ʱ����ص����÷���
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			//goToGetMsg();		
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			//goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

}
