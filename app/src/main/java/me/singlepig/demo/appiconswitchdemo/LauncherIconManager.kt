package me.singlepig.demo.appiconswitchdemo

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * @author singlepig
 * @time 2020/11/19 16:48
 * @desc description
 */
class LauncherIconManager private constructor() {
    private val TAG = "LauncherIconManager"

    companion object {
        val instance: LauncherIconManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LauncherIconManager()
        }
    }

    // 存储变化图标任务，以组件别名为key
    private val taskMap: LinkedHashMap<String, SwitchIconTask> = LinkedHashMap()

    /**
     * 添加切换图标任务
     * @param newTasks 新任务，可以传多个
     */
    fun addTask(vararg newTasks: SwitchIconTask): Unit {
        for (newTask in newTasks) {
            // 防止重复添加
            if (taskMap.containsKey(newTask.aliasComponentClassName)) return

            // 校验任务的开始和结束时间
            if (newTask.presetTime > newTask.outDateTime) {
                throw IllegalArgumentException(
                    "错误！预设时间${Date(newTask.presetTime)}, 不能晚于过期时间${
                        Date(
                            newTask.outDateTime
                        )
                    }"
                )
            }
            for (oldTask in taskMap.values) {
                if (oldTask.outDateTime >= newTask.presetTime) {
                    throw IllegalArgumentException(
                        "错误！预设时间${Date(newTask.presetTime)}, 不能早于已有任务的过期时间${
                            Date(
                                oldTask.outDateTime
                            )
                        }"
                    )
                }
            }

            // 经过校验后加入map中
            taskMap[newTask.aliasComponentClassName] = newTask
        }
    }

    /**
     * 注册以监听应用运行状态
     *
     * @param application
     */
    fun register(application: Application) {
        RunningStateRegister.register(application, object : RunningStateRegister.StateCallback {
            override fun onForeground() {
                // 应用进入前台时进行操作
            }

            override fun onBackground() {
                // 应用进入后台，进行判断，是否需要进行更改图标
                checkingTaskInOrder(application)
            }
        })
    }

    private fun checkingTaskInOrder(application: Application) {
        for (task in taskMap.values) {
            // 校对当前时间处于哪个任务的范围中
            if (checkTask(application, task)) {
                // 如果确认到第一个就退出
                break
            }
        }
    }

    /**
     * 判断当前时间与任务的关系，在任务时间范围内为true
     *
     * @param ctx
     * @param task
     */
    private fun checkTask(ctx: Context, task: SwitchIconTask) =
        // fallback形式的判断，要从时间后往前判断
        when {
            isTaskOutdated(task) -> {
                // 当前任务已经超期，本地时间大于任务的过期时间，需要禁用当前的launcher activity的图标，
                // 有可能是之前设置过，现在过期了，要恢复原来的
                disableComponent(ctx, getLauncherActivityName(ctx)!!)
                enableComponent(ctx, task.launcherComponentClassName)
                false
            }
            isTaskPreset(task) -> {
                // 当前任务已经开始，本地时间大于任务的preset时间
                disableComponent(ctx, getLauncherActivityName(ctx)!!)
                enableComponent(ctx, task.aliasComponentClassName)
                true
            }
            else -> false
        }

    private fun isTaskPreset(task: SwitchIconTask): Boolean {
        return System.currentTimeMillis() >= task.presetTime
    }

    private fun isTaskOutdated(task: SwitchIconTask): Boolean {
        return System.currentTimeMillis() >= task.outDateTime
    }

    /**
     * 启用activity-alias组件
     *
     * @param ctx
     * @param className
     */
    private fun enableComponent(ctx: Context, className: String) {
        val componentName = ComponentName(ctx, className)
        if (isComponentEnabled(ctx, componentName)) {
            Log.d(TAG, "组件${componentName.className}已启用")
            return
        }
        ctx.packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    /**
     * 禁用activity-alias组件
     *
     * @param ctx
     * @param componentName
     */
    fun disableComponent(ctx: Context, className: String): Unit {
        val componentName = ComponentName(ctx, className)
        if (isComponentDisabled(ctx, componentName)) {
            Log.d(TAG, "组件${componentName.className}已停用")
            return
        }
        ctx.packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun isComponentDisabled(ctx: Context, componentName: ComponentName): Boolean {
        return ctx.packageManager.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    }

    private fun isComponentEnabled(ctx: Context, componentName: ComponentName): Boolean {
        return ctx.packageManager.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }


    /**
     * 获取作为启动页的Activity名
     * @param context
     * @return
     */
    fun getLauncherActivityName(context: Context): String? {
        val intent =
            with(Intent(Intent.ACTION_MAIN, null)) {
                addCategory(Intent.CATEGORY_LAUNCHER)
                setPackage(context.packageName)
            }
        val resolveInfoList = context.packageManager.queryIntentActivities(intent, 0)
        return if (resolveInfoList != null && resolveInfoList.isNotEmpty()) resolveInfoList[0].activityInfo.name else null
    }
}





