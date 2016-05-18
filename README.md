# CoolPlugin
Tutorial for creating a custom PhoneGap plugin.

http://www.mat-d.com/site/tutorial-creating-a-cordova-phonegap-plugin-for-android-app/


Step #1: Preparing our plugin directories
Let’s build our own plugin named – CoolPlugin (Ok it’s not the best plugin name around, you can give it another name, but for the purpose of this tutorial it’ll be ok ;-).

Create a directory on your hard drive, let’s say :
C:\CoolPlugin
This will be our plugin repository. Now in this directory create two other directories:
\src\android\
and
\www
Under  src\android create a file named CoolPlugin.java and paste following code in it.
This code won’t do a lot, just showing a Toast when the function execute is called.


import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import android.util.Log;
import android.provider.Settings;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
public class CoolPlugin extends CordovaPlugin {
 
public static final String TAG = "Cool Plugin";
 
/**
* Constructor.
*/
public CoolPlugin() {}
 
/**
* Sets the context of the Command. This can then be used to do things like
* get file paths associated with the Activity.
*
* @param cordova The context of the main Activity.
* @param webView The CordovaWebView Cordova is running in.
*/
 
public void initialize(CordovaInterface cordova, CordovaWebView webView) {
super.initialize(cordova, webView);
Log.v(TAG,"Init CoolPlugin");
}
 
public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
         
final int duration = Toast.LENGTH_SHORT;
// Shows a toast
Log.v(TAG,"CoolPlugin received:"+ action);
 
         
cordova.getActivity().runOnUiThread(new Runnable() {
public void run() {
Toast toast = Toast.makeText(cordova.getActivity().getApplicationContext(), action, duration);
toast.show();
}
});
 
return true;
}
}
A Cordova plugin always need a public boolean execute method. This method is called by the framework whenever the function exec is called from a piece of Javascript code. The Javascript exec function will then try to look for a matching function and plugin in the Java classes. If it finds one it will trigger the execute function.

For a better understanding you can read following webpage:
https://cordova.apache.org/docs/en/3.5.0/guide_platforms_android_plugin.md.html#Android%20Plugins
Note: Now you can modify the Java class and its execute function

 

Step #2: Writing the Javascript Part
So now we have written our – very simple – Java code, but as we want to call our function from Javascript (and by extension from our HTML file) we will need to write a small piece of Javascript.
Go to /www and create a file named CoolPlugin.js (or whatever, the most important part is the name module.exports).

Paste following code in it:

var exec = require('cordova/exec');

function CoolPlugin() { 
 console.log("CoolPlugin.js: is created");
}

CoolPlugin.prototype.showToast = function(aString){
 console.log("CoolPlugin.js: showToast");

 exec(function(result){
     /*alert("OK" + reply);*/
   },
  function(result){
    /*alert("Error" + reply);*/
   },"CoolPlugin",aString,[]);
}

 var coolPlugin = new CoolPlugin();
 module.exports = coolPlugin;

The most important line is the exec line. Here you will need to specify which Java class should receive the message. In our case it’s clear, it’s the CoolPlugin class. That’s why you specify it in exec.

Step 3: Finalizing your plugin
We’ve almost finished. We now need to prepare our plugin so that Cordova/Phonegap can import it and use it.
This is the most tricky part because most of the documentation you’ll find outside will show how to do it using GitHub. As we only want to test it locally, the method is a little bit different.

Create a file named plugin.xml with following content:

<?xml version="1.0" encoding="UTF-8"?>
<plugin xmlns="http://apache.org/cordova/ns/plugins/1.0"
    xmlns:android="http://schemas.android.com/apk/res/android"
    id="com.matd.coolplugin"
    version="0.2.11">
    <name>CoolPlugin</name>
    <description>The coolest Plugin ever implemented</description>
    <license>Apache 2.0</license>
    <keywords>cordova,coolest</keywords>
    <repo></repo>
    <issue></issue>

    <js-module src="www/CoolPlugin.js" name="CoolPlugin">
        <clobbers target="CoolPlugin" />
    </js-module>

    <!-- android -->
    <platform name="android">
        <config-file target="res/xml/config.xml" parent="/*">
            <feature name="CoolPlugin" >
                <param name="android-package" value="CoolPlugin"/>
            </feature>
        </config-file>
        <source-file src="src/android/CoolPlugin.java" target-dir="src/" />
    </platform>

</plugin>
If needed you can change the android-package value to a more complex one. In this case be sure to create the correct directory hierarchy according to your package name

The line source-file will tell Cordova where to copy your *.java in the project. In our case we tell Cordova to copy the file CoolPlugin.java to the android/src/ directory of the projet, once the plugin will be installed.
Beware: never try to modify the content of CoolPlugin under platforms/android/src/CoolPlugin.java, because the next time you will reinstall the plugin all your modifications will be lost!!!

Step 3a: Publishing the plugin
If you are planning to release your plugin on GitHub (which is highly encouraged – of course only if your plugin has a nice feature and does more than showing a Toast 😉 ) you will need to create a file called package.json in which you are going to specify the name and some description about the wonderful functions of your plugin. In our case the plugin is not doing a lot, but here’s how the file should look like:

{
    "version": "0.1",
    "name": "com.matd.coolplugin",
    "cordova_name": "Cool Plugin",
    "description": "Cool Plugin",
    "license": "Apache 2.0",
    "repo": "",
    "issue": "",
    "keywords": [
        "cordova",
        "device"
    ],
    "platforms": [
        "android",
    ],
    "engines": [],
    "englishdoc": ""
}
Step 4: Create a new Cordova project and install your plugin
Important: Create your new  Cordova project elsewhere than in the plugin’s directory! Always keep both app-code and plugin-code separate, this will save you a lot of headaches and useless copy’n’paste tasks with the risk of Cordova erasing your precious plugin code!
So create a directory with a name of your choice and create a new Cordova project (If you did it before, you can skip this step).
Creating a new Cordova project named “HelloToast”

cordova create HelloToast com.test HelloToast
cd HelloToast
Preparing the project for Android

cordova platform add android
Compiling the project and launch it to your device over ADB

cordova build
Notice that there is currently nothing in the /plugin directory in the project’s directory.

We are going to install the CoolPlugin by typing:

cordova plugin add C:/CoolPlugin/
Now check under/plugins. If the plugins files are present congrats, you have succesfully installed your plugin!

 

Step 5: Integrating and Using your Cordova plugin
Obviously if you wanted to build a plugin from scratch, you’d probably want to use it, no?
Go in the /www directory of your project and open your index.html file and paste following lines in your HTML-based app:

?
1
<script type="text/javascript" src="CoolPlugin.js"></script>
and

?
1
CoolPlugin.showToast("Doh! I'm a Toast!");
Here is the full code of the index.html file:

 

<html>
 <head>

 <meta charset="utf-8" />

 <meta name="format-detection" content="telephone=no" />

 <!-- WARNING: for iOS 7, remove the width=device-width and height=device-height attributes. See https://issues.apache.org/jira/browse/CB-4323 -->
 <meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, height=device-height, target-densitydpi=device-dpi" />
 <link rel="stylesheet" type="text/css" href="css/index.css" />
 <meta name="msapplication-tap-highlight" content="no" />
 <title>Cool Plugin Test</title>
 </head>
 <body>
 <div class="app">
 <script type="text/javascript" src="cordova.js"></script>
 <script type="text/javascript" src="CoolPlugin.js"></script>
 <script type="text/javascript" src="js/index.js"></script>
 <script type="text/javascript">
 
 app.initialize();
 
 function showToast()
 {
 var textValue = document.getElementById("myText").value;
 CoolPlugin.showToast(textValue);
 }
 
 function onDeviceReady() {
 // console.log("Device ready");
 }
 document.addEventListener("deviceready", onDeviceReady, false);
 </script>
 
 <input type="text" id="myText" text="Doh! I'm a Toast!"></input>
 <button id="myButton" title="" onclick="showToast()">Generate Toast!</button>
 <p>
 </p>
 
 </body>
</html>
Step 6: Deploy and testing and Using your Cordova plugin
Type in following line to compile/launch your application on your Android device over ADB

cordova run android
Voilà! Now you should have a beautiful toast generated when you type something in the textfield!

Here’s the result:

cordova_plugin_screenshot

 

Step 6a: Uninstallation of your plugin.
Once golden rule: never manually remove your plugins by manually deleting the plugins. You will get annoying problems
To remove a plugin type :

cordova plugin remove com.matd.coolplugin
