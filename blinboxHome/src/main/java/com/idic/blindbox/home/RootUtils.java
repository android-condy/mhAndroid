package com.idic.blindbox.home;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Created by 路明辉 on 2019/11/30 0030
 */
public class RootUtils {
    private void spm(String msg) {
        Log.d("shell command:",msg);
    }
    /**
     * 判断安卓系统是否有root权限
     * @return
     */
    public boolean isRoot() {
        boolean root = false;
        try {
            if(!(new File("/system/bin/su")).exists() && !(new File("/system/xbin/su")).exists()) {
                root = false;
            } else {
                root = true;
            }
        } catch (Exception var3) {
            ;
        }
        this.spm("root:" + root);
        return root;
    }
    public boolean fileIsExists(String strFile){
        try {
            File f=new File(strFile);
            if(!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public String execRootCmd(String cmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;
        BufferedReader br = null;
        boolean isExists0 = false;
        boolean isExists1 = false;
        boolean isExists2 = false;
        isExists0 = fileIsExists("system/xbin/su")|| fileIsExists("system/bin/su");
        isExists1 = fileIsExists("system/xbin/bkcmd")|| fileIsExists("system/bin/bkcmd");
        isExists2 = fileIsExists("system/xbin/bclsu")|| fileIsExists("system/bin/bclsu");

        try {
            Process p;
            if (isExists0){
                p = Runtime.getRuntime().exec("su");
            }else if(isExists1){
                p = Runtime.getRuntime().exec("bkcmd");
            }else if (isExists2){
                p = Runtime.getRuntime().exec("bclsu");
            }else {
                p = Runtime.getRuntime().exec("su");
            }
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());
            br = new BufferedReader(new InputStreamReader(dis));

            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while((line = br.readLine()) != null) {
                result += line;
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
