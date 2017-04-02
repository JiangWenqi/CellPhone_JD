# CellPhone_JD


1. 以“京东商城”中手机类产品为抓取目标设计爬虫，手机类产品页面入口为：http://list.jd.com/list.html?cat=9987,653,655，手机类产品的产品信息页地址类似于：http://item.jd.com/2967929.html
2. 分析页面结构以及网络流量，确定待抓取的数据项，至少应抓取商品名称、商品价格、商品总评价数、商品好评度、商品介绍、商品URL等。
3. 正确处理目录页面和商品详情页面，能够自动抓取至少100款商品的上述数据项。


# 收获
1)	通过此次实验熟悉垂直型爬虫的运行流程；
2)	并且学会了通过浏览器工具对动态网页进行分析；
3)	学会了对JSON进行解析并取其元素属性值；
4)	学会了用XPATH对页面想要属性元素进行定位分析；
5)	知道Webmagic的使用方法，等。

# 尚待解决的问题
1)	爬虫单线程效率太低，速度慢；
2)	多线程具有随机性，导致爬出来手机信息紊乱；
3)	数据持久化；
4)	数据库选择与使用，等
