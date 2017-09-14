#import "smartconfigios.h"

@implementation smartconfig

- (void)start:(CDVInvokedUrlCommand*)command
{
    NSString* apSsid = (NSString *)[command.arguments objectAtIndex:0];
    NSString* password = (NSString *)[command.arguments objectAtIndex:1];
    NSString* userMarking = (NSString *)[command.arguments objectAtIndex:2];
    NSString* orderMarking =(NSString *)[command.arguments objectAtIndex:3];
    NSString* deviceName =(NSString *)[command.arguments objectAtIndex:3];
    self.callbackId = [NSString stringWithFormat:@"%@", command.callbackId];


    _configClass = [[ConfigClass alloc] init];
    _configClass.delegate = self;
    [_configClass starConfigWithWifiName:apSsid andWifiPsw:password andUserMarking:userMarking andOrderMarking:orderMarking andDeviceName:deviceName];


}

- (void)cancel:(CDVInvokedUrlCommand*)command
{
    [_configClass stopConfig];
    NSString* name = [[command arguments] objectAtIndex:0];
    NSString* msg = [NSString stringWithFormat: @"cancel, %@", name];

    CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:msg];

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    [_configClass stopConfig];
}


#pragma mark ------<ConfigDelegate>
//Configuration of equipment agent method of success
- (void)configSuccessWithDeviceMac:(DeviceModel *)deviceModel
{

    CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:deviceModel.dmac];

    [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}

@end