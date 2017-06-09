#import <Cordova/CDV.h>
#import <UIKit/UIKit.h>
#import "smartConfig.h"
#import "DataTransform.h"
#import "RemoteSession.h"
#import "HttpManager.h"
#import "DeviceViewController.h"
#import "LCToast.h"
#import "ConfigClass.h"
#import "DeviceModel.h"

@interface smartconfig : CDVPlugin
{
    ConfigClass *_configClass; 
}

- (void)start:(CDVInvokedUrlCommand*)command;

- (void)cancel:(CDVInvokedUrlCommand*)command;

@end