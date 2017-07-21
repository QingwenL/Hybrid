window.CTCallBackList = {};
String.prototype.hashCode = function() {
    var hash = 0;
    if (this.length == 0) return hash;
    for (var index = 0; index < this.length; index++) {
        var charactor = this.charCodeAt(index);
        hash = ((hash<<5)-hash)+charactor;
        hash = hash & hash;
    }
    return hash;
};

function LoadMethod(methodName, data, callback) {
    dataString = JSON.stringify(data);
    identifier = (methodName+dataString).hashCode().toString();
    window.CTCallBackList[identifier] = callback;
    url = "http://nativeapi?callbackId="+identifier+"&data="+dataString+"&serviceName="+methodName;
    window.location = url;
}

window.Callback = function(identifier, resultStatus, resultData) {
    callBackDict = window.CTCallBackList[identifier];

    if (callBackDict) {

        isFinished = true;
        switch (resultStatus){
            //success
            case "success":callBackDict.success(resultData);break;
            //fail
            case "fail":callBackDict.fail(resultData);break;
            //progress
            case "progress":{
                isFinished = false;
                callBackDict.progress(resultData);
            }break;
        }

        if (isFinished) {
            window.CTCallBackList[identifier] = NULL;
            delete window.CTCallBackList[identifier];
        }
    }
};
