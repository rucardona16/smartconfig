#include <Cordova/CDV.h>
#include <UIKit/UIKit.h>
#include <../SmartConfigLib/smartConfig.h>
#include <../SmartConfigLib/DataTransform.h>
#include <../SmartConfigLib/RemoteSession.h>
#include <../SmartConfigLib/HttpManager.h>
#include <../SmartConfigLib/DeviceViewController.h>
#include <../SmartConfigLib/LCToast.h>
#include <../SmartConfigLib/ConfigClass.h>
#include <../SmartConfigLib/DeviceModel.h>

@interface smartconfig : CDVPlugin
{
    ConfigClass *_configClass; 
}

- (void)start:(CDVInvokedUrlCommand*)command;

- (void)cancel:(CDVInvokedUrlCommand*)command;

@end