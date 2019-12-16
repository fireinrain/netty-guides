package com.sunrise.netty.studyapi.udp;

//使用netty 来开发udp 应用

//关于套接字socket 传输协议tcp/udp
// https://zh.wikipedia.org/wiki/TCP/IP%E5%8D%8F%E8%AE%AE%E6%97%8F

//各层协议

	        //7  应用层
            //application layer	例如HTTP、SMTP、SNMP、FTP、Telnet、SIP、SSH、NFS、RTSP、XMPP、Whois、ENRP、TLS
            //6	表示层
            //presentation layer	例如XDR、ASN.1、SMB、AFP、NCP
            //5	会话层
            //session layer	例如ASAP、ISO 8327 / CCITT X.225、RPC、NetBIOS、ASP、IGMP、Winsock、BSD sockets
            //4	传输层
            //transport layer	例如TCP、UDP、RTP、SCTP、SPX、ATP、IL
            //3	网络层
            //network layer	例如IP、ICMP、IPX、BGP、OSPF、RIP、IGRP、EIGRP、ARP、RARP、X.25
            //2	数据链路层
            //data link layer	例如以太网、令牌环、HDLC、帧中继、ISDN、ATM、IEEE 802.11、FDDI、PPP
            //1	物理层
            //physical layer	例如數據機、无线电、光纤