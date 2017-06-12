#import <Cordova/CDV.h>
#import <UIKit/UIKit.h>
#import "smartConfig.h"
//#import "../SmartConfigLib/DataTransform.h"
//#import "../SmartConfigLib/RemoteSession.h"
//#import "../SmartConfigLib/HttpManager.h"
//#import "../SmartConfigLib/DeviceViewController.h"
//#import "../SmartConfigLib/LCToast.h"
#import "ConfigClass.h"
#import "DeviceModel.h"

@interface smartconfig : CDVPlugin
{
    //ConfigClass *_configClass; 
}

- (void)start:(CDVInvokedUrlCommand*)command;

- (void)cancel:(CDVInvokedUrlCommand*)command;

@end