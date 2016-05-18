var exec = require('cordova/exec');

function CoolPlugin() { 
 console.log("CoolPlugin.js: is created");
}

BarcodeScanner.prototype.scan = function (successCallback, errorCallback, config) {

    if(config instanceof Array) {
        // do nothing
    } else {
        if(typeof(config) === 'object') {
            config = [ config ];
        } else {
            config = [];
        }
    }

    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("CoolPlugin.scan failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("CoolPlugin.scan failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'CoolPlugin', 'scan', config);
};

 var coolPlugin = new CoolPlugin();
 module.exports = coolPlugin;