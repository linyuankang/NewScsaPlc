package tw.com.scsa.newscsaplc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * Created by Initial_1 on 2016/12/27.
 */

public class OnlinerMoniterService extends Service {
    private Context context;
    private long timecurrentTimeMillis;
    DatabaseReference mMessage ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private NotificationManager notificationManager;
    Thread moniterOnline;
    Handler handler;
    long nowTimeStamp;
    long timestamp;
    boolean ThreadStatus;
    boolean notyStatus;
    int myTimmer;
    String companyID, userName, device, bossMail, userNameReplace, companyEmail, topics_id;
    public static final String KEY = "tw.com.scsa.newscsaplc";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        context = this;
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(this, "Service start", Toast.LENGTH_SHORT).show();
        SharedPreferences remdname = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        String companyID = remdname.getString("companyID", "");
        String userNameValue = remdname.getString("userName", "");
        String companyEmail = remdname.getString("companyEmail", "");
         device = remdname.getString("device", "");
        String  bossEmail= remdname.getString("bossEmail", "");
        final String  userPwd= remdname.getString("userPwd", "");
        Log.d("我在這",companyID + "/deviceCheck/" + companyEmail + "/" + device);
        mMessage= database.getReference(companyID + "/deviceCheck/" + companyEmail + "/" + device);
        notyStatus=false;
        Query dataQuery = mMessage.orderByChild("timeStamp").limitToLast(2);

        dataQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataSnapshot.child("timeStamp").getValue().toString();
                nowTimeStamp=Long.parseLong(dataSnapshot.child("timeStamp").getValue().toString());
                notyStatus=false;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                
               /* if (ThreadStatus)
                {


                }
                else
                {

                    moniterOnline.interrupt();
                    try {
                        moniterOnline.start();
                    }catch (IllegalThreadStateException e){Log.d("錯誤","123");}
                }*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        handler = new Handler();
        myTimmer=40;
        moniterOnline =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true)
                    {
                        Thread.sleep(10000);
                        timecurrentTimeMillis = System.currentTimeMillis();

                        Date mDate = new Date();
                       timestamp = System.currentTimeMillis();
                        if(timestamp-nowTimeStamp>150000&  !notyStatus&nowTimeStamp!=0)
                        {
                            Notification1( context, 123);

                            notyStatus=true;
                        }
                        Log.d("現在",timestamp+"");
                        Log.d("網路",nowTimeStamp+"");
                    }
                }catch (Exception e)
                {

                }handler.postDelayed(moniterOnline,1000);
            }
        });

       /* moniterOnline=new  Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(myTimmer>0){            //判斷是否使用Thread顯示字串
                        runOnUiThread(new Runnable() {        //可以使用此方法臨時交給UI做顯示
                            @Override
                            public void run() {
                                ThreadStatus=true;
                                // TODO Auto-generated method stub
                                Log.d("時間",myTimmer+"");
                                if(myTimmer<10){}
                                myTimmer--;
                            }

                        });
                        Thread.sleep(1000);
                        handler.postDelayed(moniterOnline,1000);
                    }
                    runOnUiThread(new Runnable() {        //可以使用此方法臨時交給UI做顯示
                        public void run(){

                            ThreadStatus=false;
                           // moniterOnline.interrupt();
                        }
                    });



                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });*/

        moniterOnline.start();
    }
    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
    private void Notification1(Context context, int nid) {
        // 2.建立-通知服務建構器

        Notification.Builder builder = new Notification.Builder(context);

        // 3.建立按下訊息嵌板後所要轉跳的Intent
        Intent intent = new Intent(context, MainActivity.class);
        // PendingIntent pendIntent = PendingIntent.getActivity(context, Integer.parseInt(id.toString()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 設定Intent標誌參數
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri defaultSoundUri= Uri. parse("android.resource://" + getPackageName() + "/" + R.raw.yisell_sound);
        // 設定請求碼
        int requestCode = 1;
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 4.設定震動頻率
        long[] vibratepattern = { 100, 400, 500, 400 };
        // Respurces 轉 bitmap
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.jjj);
        // 5.定義 Notification.Builder 建構器
        builder.setSmallIcon(R.drawable.log_37_small) // 通知服務 icon
                .setLargeIcon(bmp)
                .setContentTitle(
                        device+"與伺服器連線中斷") // 標題
                .setContentText("偵測到無即時更新") // 內文
                .setTicker("網路"+nowTimeStamp+"現在"+timestamp) // Ticker 標題
                .setLights(0xFFFFFFFF, 1000, 1000) // LED
                .setSound(defaultSoundUri)
                .setVibrate(vibratepattern) // 震動
                .setContentIntent(pendingIntent) // 設定Intent服務
                .setAutoCancel(true); // true：按下訊息嵌板後會自動消失
            /*
            在鎖屏上揭露完整訊息：Notification.VISIBILITY_PUBLIC
            基本的資訊與通知的圖示：Notification.VISIBILITY_PRIVATE（預設）
            在鎖屏上揭露訊息：Notification.VISIBILITY_SECRET
            */

        builder.setPriority(Notification.PRIORITY_HIGH);
        Notification notification = builder.build();
        notificationManager.notify(nid, notification); // 發佈Notification

    }


}

