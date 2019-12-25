package com.example.administrator.crackme;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.ArrayMap;
//import FileManage;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2019/12/24 0024.
 */

public class ShellApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Object currentActivityThread = RefInvoke.invokeStaticMethod("android.app.ActivityThread",
                    "currentActivityThread",
                    new Class[] {}, new Object[] {});
            Object mBoundApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread","mBoundApplication",
                    currentActivityThread);
            Object loadedApkInfo = RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData","info",
                    mBoundApplication);
            RefInvoke.setFieldOjbect("android.app.LoadedApk", "mApplication", loadedApkInfo, null);
            String srcName="com.example.administrator.dexloader1.MainActivity";
            ApplicationInfo appinfo=(ApplicationInfo)RefInvoke.getFieldOjbect("android.app.LoadedApk",
                    "mApplicationInfo",loadedApkInfo);
            appinfo.className=srcName;
            Object oldApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread",
                     "mInitialApplication",currentActivityThread);
            // 获取currentActivityThread对象sCurrentActivityThread中的mAllApplications成员变量（存放Application的列表）
            ArrayList<Application> mAllApplications = (ArrayList<Application>) RefInvoke.getFieldOjbect("android.app.ActivityThread",
                    "mAllApplications" ,currentActivityThread);
            // 从当前mAllApplications中移除当前（旧的）Application
            mAllApplications.remove(oldApplication);
            Application app = (Application) RefInvoke.invokeMethod("android.app.LoadedApk", "makeApplication",
                    loadedApkInfo, new Class[] { boolean.class, Instrumentation.class }, new Object[] { false, null });
            app.onCreate();
            RefInvoke.setFieldOjbect("android.app.ActivityThread","mInitialApplication",currentActivityThread,app);




        }catch (Exception e){

        }
    }

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);
        //创建目录，释放dex
        File cache=getDir("nmsl",MODE_PRIVATE);
        String dex=cache+"/classes.dex";
        File dexfile=FileManage.releaseAssetsFile(this,"classes.dex",dex,null);//在根目录写回文件
        DexClassLoader dcl=new DexClassLoader(dex,getDir("nmsl_oat",MODE_PRIVATE).getAbsolutePath(),
                getApplicationInfo().nativeLibraryDir,getClassLoader());//loaddex
        try {
            Class clazz = Class.forName("android.app.ActivityThread");//反射获取系统类
            Method method = clazz.getDeclaredMethod("currentActivityThread");//获取currentActivityThread函数，无参数
            method.setAccessible(true);
            Object currentThread= method.invoke(new Class[]{},new Object[]{});
            Class clazz1=Class.forName("android.app.ActivityThread");
            Field filed=clazz1.getDeclaredField("mPackages");
            filed.setAccessible(true);
            ArrayMap arrayMap=(ArrayMap)filed.get(currentThread);
            WeakReference wr=(WeakReference)arrayMap.get(getPackageName());
            Class clazz2=Class.forName("android.app.LoadedApk");
            Field filed1=clazz2.getDeclaredField("mClassLoader");
            filed1.setAccessible(true);
            filed1.set(wr.get(),dcl);
        }catch (Exception e){

        }




    }
}
