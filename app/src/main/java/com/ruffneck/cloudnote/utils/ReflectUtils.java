package com.ruffneck.cloudnote.utils;

import android.support.v4.internal.view.SupportMenuItem;
import android.view.Menu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ReflectUtils {

    public static boolean setMenuIconVisibility(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class.forName("android.support.v7.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

            //设置默认是
            Field field = clazz.getDeclaredField("mDefaultShowAsAction");
            field.setAccessible(true);
            field.set(menu, SupportMenuItem.SHOW_AS_ACTION_ALWAYS);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
