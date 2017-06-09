//
//  CRC.h
//  CustomSwitch
//
//  Created by 林琳 on 16/9/7.
//  Copyright © 2016年 Ogemray. All rights reserved.
//

#import <Foundation/Foundation.h>

// CRC效验

@interface CRC : NSObject
{
    int crc_tab[256];
}

@property(nonatomic,assign)short CRC_FIRST_DATA;

-(short)CalCrc16WithData:(NSData *)cdata andCrc:(short)ccrc;


@end
