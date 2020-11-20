## 说明
练手Demo，实现根据系统时间来切换应用图标和应用名，实现类似淘宝等大型电商APP在购物节前后自动换图标的功能。
主要通过`activity-alias`和`PackageManager#setComponentEnabledSetting`来实现。
系统时间设置在2020-06-17至2020-06-19之间会切换为618大促，系统时间设置在2020-11-10至2020-11-12之间会切换为双11大促。


## 参考文章

https://www.jianshu.com/p/ad567861bc0e

https://github.com/madchan/LauncherIconLib

https://developer.android.com/guide/topics/manifest/activity-alias-element?hl=zh-cn
