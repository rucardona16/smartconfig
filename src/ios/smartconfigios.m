#import "smartconfigios.h"

@implementation smartconfig

- (void)start:(CDVInvokedUrlCommand*)command
{
    NSString* apSsid = (NSString *)[command.arguments objectAtIndex:0];
    NSString* password = (NSString *)[command.arguments objectAtIndex:1];
    NSString* userMarking = (NSString *)[command.arguments objectAtIndex:2];
    NSString* orderMarking =(NSString *)[command.arguments objectAtIndex:3];
    NSString* deviceName =(NSString *)[command.arguments objectAtIndex:3];

    //_configClass = [[ConfigClass alloc] init]; 
    //_configClass.delegate = self; 

    //[_configClass starConfigWithWifiName:apSsid andWifiPsw:password andUserMarking:userMarking andOrderMarking:orderMarking andDeviceName:deviceName];

    /*NSString* name = [[command arguments] objectAtIndex:0];
    NSString* msg = [NSString stringWithFormat: @"start, %@", name];

    CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:msg];

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];*/
}

- (void)cancel:(CDVInvokedUrlCommand*)command
{

    /*NSString* name = [[command arguments] objectAtIndex:0];
    NSString* msg = [NSString stringWithFormat: @"cancel, %@", name];

    CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:msg];

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];*/
    //[_configClass stopConfig];
}

@end
