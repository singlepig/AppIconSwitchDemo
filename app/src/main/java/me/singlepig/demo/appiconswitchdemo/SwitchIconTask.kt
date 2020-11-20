package me.singlepig.demo.appiconswitchdemo

/**
 * @author singlepig
 * @time 2020/11/19 16:34
 * @desc 切换图标任务类
 */
data class SwitchIconTask (val launcherComponentClassName: String,  // 启动器组件类名
                           val aliasComponentClassName: String,     // 别名组件类名
                           val presetTime: Long,                    // 预设时间
                           val outDateTime: Long)                   // 过期时间
