package com.example.administrator.crackme;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2019/12/24 0024.
 */

public class FileManage {
    public static File releaseAssetsFile(Context ctx, String assFile, String relFile, Method decMethod) {
        AssetManager manager = ctx.getAssets();
        try{
            InputStream is=manager.open(assFile);
            ByteArrayOutputStream os=new ByteArrayOutputStream();
            byte[] buf=new byte[1024];
            int read;
            while((read=is.read(buf))!=-1){
                os.write(buf,0,read);
            }
            buf[0]=100;
            buf[1]=101;
            buf[2]=120;
            byte[] dec= (decMethod !=null) ? ((byte[])decMethod.invoke(null,os.toByteArray())) : os.toByteArray();
            is.close();os.close();
            FileOutputStream of=new FileOutputStream(new File(relFile));
            of.write(dec);
            return new File(relFile);

        }
        catch (IOException e){

        }
        catch (InvocationTargetException e) {

        }catch (IllegalAccessException e){

        }
        return null;
    }
}
