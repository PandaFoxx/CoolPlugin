var exec = require('cordova/exec');

function CoolPlugin() { 
    /**
     * Barcode format constants, defined in ZXing library.
     *
     * @type Object
     */
    this.format = {
        "all_1D": 61918,
        "aztec": 1,
        "codabar": 2,
        "code_128": 16,
        "code_39": 4,
        "code_93": 8,
        "data_MATRIX": 32,
        "ean_13": 128,
        "ean_8": 64,
        "itf": 256,
        "maxicode": 512,
        "msi": 131072,
        "pdf_417": 1024,
        "plessey": 262144,
        "qr_CODE": 2048,
        "rss_14": 4096,
        "rss_EXPANDED": 8192,
        "upc_A": 16384,
        "upc_E": 32768,
        "upc_EAN_EXTENSION": 65536
    };
	
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