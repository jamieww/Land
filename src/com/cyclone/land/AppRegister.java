package com.cyclone.land;

import net.sourceforge.simcpux.Constants;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub

		final IWXAPI api = WXAPIFactory.createWXAPI(arg0, null);

		// 将该app注册到微信
		api.registerApp(Constants.APP_ID);
	}

}
