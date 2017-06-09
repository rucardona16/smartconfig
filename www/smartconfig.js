/*global cordova, module*/

module.exports = {
	start:function(apSsid, password, userMarking, orderMarking, deviceName, successCallback, errorCallback){
		exec(successCallback, errorCallback, "smartconfig", "start", [apSsid, password, userMarking, orderMarking, deviceName]);
	},
	
	cancel:function(successCallback, errorCallback){
		exec(successCallback, errorCallback, "smartconfig", "cancel", []);
	}
}
