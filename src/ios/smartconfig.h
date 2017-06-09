#import <Cordova/CDV.h>

@interface smartconfig : CDVPlugin

- (void)start:(CDVInvokedUrlCommand*)command;

- (void)cancel:(CDVInvokedUrlCommand*)command;

@end