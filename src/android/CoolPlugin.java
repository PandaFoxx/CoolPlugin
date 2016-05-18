//package com.letsap.plugins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.honeywell.scanintent.ScanIntent;


public class CoolPlugin extends CordovaPlugin {
    public static final int REQUEST_CODE = 0x0ba7c0de;

    private static final String SCAN = "scan";
    private static final String ENCODE = "encode";
    private static final String CANCELLED = "cancelled";
    private static final String FORMAT = "format";
    private static final String TEXT = "text";
    private static final String DATA = "data";
    private static final String TYPE = "type";
    private static final String SCAN_INTENT = "com.google.zxing.client.android.SCAN";
    private static final String ENCODE_DATA = "ENCODE_DATA";
    private static final String ENCODE_TYPE = "ENCODE_TYPE";
    private static final String ENCODE_INTENT = "CoolPlugin.ENCODE";
    private static final String TEXT_TYPE = "TEXT_TYPE";
    private static final String EMAIL_TYPE = "EMAIL_TYPE";
    private static final String PHONE_TYPE = "PHONE_TYPE";
    private static final String SMS_TYPE = "SMS_TYPE";

    private static final String LOG_TAG = "CoolPlugin";

    private CallbackContext callbackContext;

    /**
     * Constructor.
     */
    public CoolPlugin() {
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;

        if (action.equals(ENCODE)) {
            JSONObject obj = args.optJSONObject(0);
            if (obj != null) {
                String type = obj.optString(TYPE);
                String data = obj.optString(DATA);

                // If the type is null then force the type to text
                if (type == null) {
                    type = TEXT_TYPE;
                }

                if (data == null) {
                    callbackContext.error("User did not specify data to encode");
                    return true;
                }

                encode(type, data);
            } else {
                callbackContext.error("User did not specify data to encode");
                return true;
            }
        } else if (action.equals(SCAN)) {
            scan(args);
        } else {
            return false;
        }
        return true;
    }

    public void scan(JSONArray args) {
    	Intent intentScan = new Intent(ScanIntent.SCAN_ACTION);
		intentScan.addCategory(Intent.CATEGORY_DEFAULT);

		intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		int loadmode = 0;

		intentScan.putExtra("scan_mode", ScanIntent.SCAN_MODE_SHOW_NO_RESULT);

/*		SCAN_MODE_SHOW_NO_RESULT = 0;
		SCAN_MODE_SHOW_RESULT_UI = 1;	
		SCAN_MODE_SHARE_BY_SMS = 2;		
		SCAN_MODE_SHARE_BY_MMS = 3;		
		SCAN_MODE_SHARE_BY_EMAIL = 4;
		SCAN_MODE_RESULT_AS_URI = 5;*/
		
		intentScan.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());
		
		//this.cordova.startActivityForResult(intentScan, 0);
		this.cordova.startActivityForResult((CordovaPlugin) this, intentScan, 0);
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == ScanIntent.SCAN_RESULT_SUCCESSED) {
            JSONObject obj = new JSONObject();
            try {
            	obj.put(TEXT, intent.getStringExtra("SCAN_RESULT"));
                obj.put(FORMAT, intent.getStringExtra("SCAN_RESULT_FORMAT"));
                obj.put(CANCELLED, false);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "This should never happen");
            }
            //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
            this.callbackContext.success(obj);
        } else if (resultCode == ScanIntent.SCAN_RESULT_FAILED) {
            JSONObject obj = new JSONObject();
            try {
                obj.put(TEXT, "");
                obj.put(FORMAT, "");
                obj.put(CANCELLED, true);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "This should never happen");
            }
            //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
            this.callbackContext.success(obj);
        } else {
            //this.error(new PluginResult(PluginResult.Status.ERROR), this.callback);
            this.callbackContext.error("Unexpected error");
        }
	}

    public void encode(String type, String data) {
        Intent intentEncode = new Intent(ENCODE_INTENT);
        intentEncode.putExtra(ENCODE_TYPE, type);
        intentEncode.putExtra(ENCODE_DATA, data);
        // avoid calling other phonegap apps
        intentEncode.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());

        this.cordova.getActivity().startActivity(intentEncode);
    }
}