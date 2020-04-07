# A-SUCC
Automated Stream-based User Content Counter for SSTM|基于动态流的用户内容数自动统计器

目前主要用于统计广场每日内容数的分配情况。

升级进展：  
* 07/04/2020 在github上建立了repo已省略在论坛贴代码的功夫，但还不知道怎么更新repo(  
* 06/04/2020 通过循环载入时间戳(循环条件为页面最下方存在"加载更多动态"的选项)达成了自动化载入。  
需要一定载入时间(总内容数/25次载入)，但效率远高于手动加载  
* 上传至github前 可根据已手动读取的动态流html读取包括部分用户信息在内的资料，并根据用户整理内容数

后续展望：  
* 加入登录功能，使得整理里区内容数成为可能，且不论有没有人要