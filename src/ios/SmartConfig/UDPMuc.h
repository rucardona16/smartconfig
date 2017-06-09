//
//  UDPMuc.h
//  SmartConnection
//
//  Created by sunrun on 14-8-19.
//  Copyright (c) 2014年 sunrun. All rights reserved.
//

#import <Foundation/Foundation.h>
//#import "AsyncUdpSocket.h"
#import "GCDAsyncUdpSocket.h"
@interface UDPMuc : NSObject
{
    GCDAsyncUdpSocket *udpSocket;
    int a;
    
    int udpNums[50][3];
}
@property (nonatomic,assign) int tag;
//- (void)sendByDatas:(Byte)mesg andNum:(int)num;

- (void)sendByDatas:(Byte)mesg andDatas:(Byte)mesg2 andNum:(int)num;

- (void)sendByDatas:(NSData *)cdata andDataLength:(int)cdataLength;
@end
