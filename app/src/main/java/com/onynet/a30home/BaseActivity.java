package com.onynet.a30home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.Locale;

/**
 * 时 间: 2016/12/27 0027
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public abstract class BaseActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    private static int TTS_DATA_CHECK = 1;

    private TextToSpeech tts = null;
    private boolean ttsIsInit = false;


    private MediaPlayer mediaPlayer;
    private boolean playBeep = true;

    private static final float BEEP_VOLUME = 1.0f;//提示音量大小
    private static final long VIBRATE_DURATION = 200L;//震动时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!checkNetworkState()){
//            setNetwork();
//        }
        MyApplication.getInstance().addActivity(this);


    }

    public void saveToken(String token, String group_type) {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        MyApplication.token = token;
        MyApplication.group_type = group_type;
        System.out.println("toke为:" + token);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("group_type", group_type);

        if (editor.commit()) {
            System.out.println("token保存成功...");
        }

    }

    /**
     * 初始化合成语音
     */
    protected void initTextToSpeech() {
        Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, TTS_DATA_CHECK);
    }

    /**
     * 设置合成文字
     *
     * @return
     */
    protected String setVoicePrompt() {
        return null;
    }

    /**
     * 初始化提示音
     */
    protected void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(setBeepSoundRes());
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    /**
     * 设置提示音量文件
     *
     * @return
     */
    protected int setBeepSoundRes() {
        return 0;
    }

    ;

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 播放声音和震动
     */
    protected void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }

        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION);
    }


    /**
     * 保存登录用户信息
     *
     * @param user
     * @param password
     * @param checked
     */
    public void saveAccount(String user, String password, boolean checked) {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", user);
        editor.putString("password", password);
        editor.putBoolean("checked", checked);

        if (editor.commit()) {
            System.out.println("token保存成功...");
        }
    }


    /**
     * 保存提货码
     */
    public void saveGoodsCode(long UpGoodsCode, String date) {
        SharedPreferences sharedPreferences = getSharedPreferences("UpGoodsCode", MODE_PRIVATE);

        MyApplication.UpGoodsCode = UpGoodsCode;
        MyApplication.date = date;

        System.out.println("UpGoodsCode为:" + UpGoodsCode);
        System.out.println("date为:" + date);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("date", date);
        editor.putLong("UpGoodsCode", UpGoodsCode);

        if (editor.commit()) {
            System.out.println("UpGoodsCode保存成功...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TTS_DATA_CHECK) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            ttsIsInit = true;
                            if (tts.isLanguageAvailable(Locale.CHINESE) >= 0) {
                                tts.setPitch(100.0f);
                                tts.setSpeechRate(1.1f);
                                speak();
                            }
                        }
                    }

                    private void speak() {
                        if (tts != null && ttsIsInit) {
                            tts.speak(setVoicePrompt(), TextToSpeech.QUEUE_ADD, null);
                        }
                    }
                });
            } else {
                Intent installAllVoice = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installAllVoice);
            }

        }
    }


    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


  /*  ConnectivityManager manager;

    *//**
     * 检测网络是否连接
     *
     * @return
     *//*
    public boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetwork();
        } else {
            isNetworkAvailable();
        }

        return flag;
    }


    *//**
     * 网络未连接时，调用设置方法
     *//*
    protected void setNetwork() {
        Toast.makeText(this, "wifi is closed!", Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("网络提示信息");
        builder.setMessage("网络不可用，如果继续，请先设置网络！");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = null;
                *//**
                 * 判断手机系统的版本！如果API大于10 就是3.0+
                 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
                 *//*
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    *//**
     * 网络已经连接，然后去判断是wifi连接还是GPRS连接
     * 设置一些自己的逻辑调用
     *//*
    protected void isNetworkAvailable() {

        NetworkInfo.State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
            Toast.makeText(this, "wifi is open! gprs", Toast.LENGTH_SHORT).show();
        }
        //判断为wifi状态下才加载广告，如果是GPRS手机网络则不加载！
        if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
            Toast.makeText(this, "wifi is open! wifi", Toast.LENGTH_SHORT).show();
        }

    }*/

}
