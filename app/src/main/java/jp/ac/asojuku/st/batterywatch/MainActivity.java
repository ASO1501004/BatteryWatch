package jp.ac.asojuku.st.batterywatch;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private MyBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //フォアグラウンドになる時呼び出される
    @Override
    protected void onResume() {
        super.onResume();
        //インスタンスを生成
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, filter);

    }
    //フォアグラウンドでなくなる時呼び出される
    @Override
    protected void onPause() {
        super.onPause();
        //登録を解除
        unregisterReceiver(mReceiver);
    }

    //内部クラス
    //インテントを受信したら、OnReceive()メソッドに記述された処理を実行する
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // 複数のインテントを受信する場合はif文を使う
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                // 電池残量の最大値
                int scale = intent.getIntExtra("scale", 0);
                // 電池残量
                int level = intent.getIntExtra("level", 0);
                //充電中か放電中かといった状態
                int status = intent.getIntExtra("status", 0);
                String statusString = "";
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        statusString = "unknown";
                        break;
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        statusString = "charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        statusString = "discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        statusString = "not charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        statusString = "full";
                        break;
                }
                //カレンダーオブジェクトを生成して、現在の時刻を取得してバッテリーの状態とともにログ出力
                final Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);
                final int second = calendar.get(Calendar.SECOND);
                String title ="battery watch";
                String message = " " + hour + ":" + minute + ":" + second + " " + statusString + " " + level + "/" + scale;
                Log.v(title, message);

                Activity mainActivity = (Activity)context;
                TextView tvtitle = (TextView)mainActivity.findViewById(R.id.tvtitle);
                tvtitle.setText(title);
b
                TextView tvMsg = (TextView)mainActivity.findViewById(R.id.tvMsg);
                tvMsg.setText(message);

                Toast.makeText(mainActivity,title+message,Toast.LENGTH_LONG).show();
            }
        }
    };
}