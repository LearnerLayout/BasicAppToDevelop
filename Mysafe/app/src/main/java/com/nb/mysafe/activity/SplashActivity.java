package com.nb.mysafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nb.mysafe.R;
import com.nb.mysafe.utils.AppInfUtil;
import com.nb.mysafe.utils.IntentUtil;
import com.nb.mysafe.utils.StringUtil;
import com.nb.mysafe.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {
    private TextView tvVersionName;
    private ProgressBar progressBar;
    private RelativeLayout rlroot;

    private void assignViews() {
        tvVersionName = (TextView) findViewById(R.id.tv_version_name);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rlroot = (RelativeLayout) findViewById(R.id.rl_root);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        assignViews();
        //设置一个渐变动画
        AlphaAnimation aa =   new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(2000);
        rlroot.startAnimation(aa);
        copyDb("address.db");
        checkVersion();
    }
//创建一个从assets里拷贝数据库文件的方法,传入一个数据库名称,将该数据库拷贝到自身files文件夹下
    private void copyDb(String name) {
        //模板拷贝流
        File file = new File(getFilesDir(), name);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = getAssets().open(name);
            byte[] buffer = new byte[1024];
            int len ;
            while((len=is.read(buffer))!=-1){
                 fos.write(buffer,0,len);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void checkVersion() {

        new Thread() {
            @Override
            public void run() {
                try {
                    //先不访问服务器
                    IntentUtil.startActivityForDelayAndFinish(SplashActivity.this, HomeActivity.class, 2000l);

                    URL url = new URL(getString(R.string.serverurl));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        String str = StringUtil.readStream(connection.getInputStream());
                        JSONObject json = new JSONObject(str);
                        //将升级信息都封装到一个obj里,方便传回主ui
                        UpdateInfo updateInfo = new UpdateInfo();
                        updateInfo.version = json.getInt("version");
                        updateInfo.downloadurl = json.getString("downloadurl");
                        updateInfo.desc = json.getString("desc");
                        //判断是否跳转
                        if (updateInfo.version > AppInfUtil.getVersionCode(SplashActivity.this)) {
                            //就开始谈对话框提醒用户升级
                            ToastUtil.show(SplashActivity.this,updateInfo.desc);
                            //
                        } else {
                            IntentUtil.startActivityForDelayAndFinish(SplashActivity.this, HomeActivity.class, 2000l);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

class UpdateInfo{
    private int version;
    private String downloadurl;
    private String desc;

}
}
