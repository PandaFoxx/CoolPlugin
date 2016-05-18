//package src.android;

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

		intentScan.putExtra("scan_mode", ScanIntent.SCAN_MODE_RESULT_AS_URI);

/*		SCAN_MODE_SHOW_NO_RESULT = 0;
		SCAN_MODE_SHOW_RESULT_UI = 1;	
		SCAN_MODE_SHARE_BY_SMS = 2;		
		SCAN_MODE_SHARE_BY_MMS = 3;		
		SCAN_MODE_SHARE_BY_EMAIL = 4;
		SCAN_MODE_RESULT_AS_URI = 5;*/
		
		this.startActivityForResult(intentScan, 5);
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == ScanIntent.SCAN_RESULT_SUCCESSED) {
			String data = intent.getStringExtra(ScanIntent.EXTRA_RESULT_BARCODE_DATA);
			int format = intent.getIntExtra(
					ScanIntent.EXTRA_RESULT_BARCODE_FORMAT, 0);
		
			barcodeData.setText(ScanIntent.EXTRA_RESULT_BARCODE_DATA+ ": " + data + "\r\n" + ScanIntent.EXTRA_RESULT_BARCODE_FORMAT + ": " + format);
		}
		else
			barcodeData.setText(R.string.scan_failed);
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