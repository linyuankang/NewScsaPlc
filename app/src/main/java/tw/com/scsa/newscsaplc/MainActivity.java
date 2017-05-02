package tw.com.scsa.newscsaplc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private GestureDetector detector;
    DatabaseReference scsa,timerDatabase;
    private ViewFlipper flipper;
    private ListView listInput;
    private ArrayAdapter<String> adapter;
    private Queue<String> items;
    private ArrayList<String> timeItems1;
    private LinearLayout M0000, M0008;
    private LinearLayout wordLinearLayout;
    Thread moniterOnline;
    Thread monitThread;
    Button Online;
    ArrayList<String> devices = new ArrayList<>();
    private Spinner devicemoniterSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> diviceSpinnerValue;
int myTimmer;
    ArrayList<HashMap> objectList;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    Button[][] gButton;
    LinearLayout[] wordlaout;
    TextView[][] wordEdit;
    TextView[][] worrdTextView;
    TextView[][] BitTextView;

    int wordLayoutX;
    int wordlayoutY;
    int gXArraySize;
    int gYArraySize;
    String companyID, userName, device, bossMail, userNameReplace, companyEmail, topics_id;
    public  final String KEY = "tw.com.scsa.newscsaplc";
    boolean hasDevice=false;
    boolean ThreadStatus;
    //監控
    boolean notyStatus;
    long nowTimeStamp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String mAuthId, mAuthPWD;
    private Button manage,
            bitAdr11, bitAdr12, bitAdr13, bitAdr14, bitAdr15, bitAdr16, bitAdr17, bitAdr18,
            bitAdr21, bitAdr22, bitAdr23, bitAdr24, bitAdr25, bitAdr26, bitAdr27, bitAdr28;
    //bitAdr31,bitAdr32,bitAdr33,bitAdr34,bitAdr35,bitAdr36,bitAdr37,bitAdr38,
    //bitAdr41,bitAdr42,bitAdr43,bitAdr44,bitAdr45,bitAdr46,bitAdr47,bitAdr48;


    String id = "id", name = "name";
    int scsacount = 0;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //20161214 lin  V1.0.0 + 滑行控制項開啟
        flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper01);
        detector = new GestureDetector(this, this);
        Online=(Button)findViewById(R.id.Online);
        // DatabaseReference nowDataTime = database.getReference("Name").child("time");
        DisplayMetrics monitorsize =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(monitorsize);

        ThreadStatus=false;

        //Log.e("螢幕寬",monitorsize.widthPixels+"");
        manage = (Button) findViewById(R.id.manage);
        myTimmer=100;
        moniterOnline=new  Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (true) {
                        Thread.sleep(2000);
                         //  Date mDate = new Date();
                        long timestamp = System.currentTimeMillis();
                        if (timestamp - nowTimeStamp > 100000 & !notyStatus) {
                            runOnUiThread(new Runnable() {        //可以使用此方法臨時交給UI做顯示
                                public void run(){

                                    Online.setVisibility(View.VISIBLE);
                                    notyStatus = true;
                                }
                            });

                        }
                      /*  Log.d("這是誰", timestamp + "");
                        Log.d("timeStamp", nowTimeStamp + "");*/



                    }


                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        moniterOnline.start();
     /*   if (ThreadStatus)
        {
            Log.d("這是",ThreadStatus+"");
            moniterOnline.interrupt();
            Online.setVisibility(View.INVISIBLE);
            ThreadStatus=false;

        }
        else
        {
            Log.d("不是",ThreadStatus+"");
            ThreadStatus=true;
            moniterOnline.start();

        }*/

        Intent intent = new Intent(MainActivity.this,OnlinerMoniterService.class);



        startService(intent);

        wordLinearLayout = (LinearLayout) findViewById(R.id.wordlayoutgroup);
        devicemoniterSpinner= (Spinner) findViewById(R.id.spinner);
        diviceSpinnerValue=new ArrayList<String>();
        devicemoniterSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (!device.equals(devicemoniterSpinner.getSelectedItem().toString())) {
                    DatabaseReference  deviceTry= database.getReference(companyID+"/deviceCheck/"+companyEmail+"/"+devicemoniterSpinner.getSelectedItem().toString());
                    deviceTry.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try
                            {
                                String selcetPostinValue=devicemoniterSpinner.getSelectedItem().toString();
                                dataSnapshot.getValue().toString();
                                Log.d("1234" ,dataSnapshot.getValue().toString());
                                hasDevice=true;
                            }catch (Exception e)
                            {
                                Log.i("1235",companyID+"/deviceCheck/"+companyEmail+"/"+devicemoniterSpinner.getSelectedItem().toString());
                                Toast.makeText(MainActivity.this,"您沒有該設備權限",Toast.LENGTH_LONG).show();
                                hasDevice=false;
                            }
                            if(hasDevice)
                            {
                                device = devicemoniterSpinner.getSelectedItem().toString();
                                SharedPreferences remdname = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = remdname.edit();

                                Log.d("scsa1", device);

                                // edit.putString("device",device);
                                getTextAndButton(scsa);
                                Bundle bundle = MainActivity.this.getIntent().getExtras();
                                bundle.putString("device", device);

                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                    // TODO Auto-generated method stub
                      /*  ((EditText)findViewById(R.id.device)).setText( devicemoniterSpinner.getSelectedItem().toString());*/
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //接收前頁資料

        Bundle bundle = this.getIntent().getExtras();
        userName = bundle.getString("userName");
        device = bundle.getString("device");
        companyID = bundle.getString("companyID");
        userNameReplace = bundle.getString("userNameReplace");
        companyEmail = bundle.getString("companyEmail");
        topics_id = bundle.getString("topics_id");
        bossMail=bundle.getString("bossEmail");


         scsa = database.getReference(companyID + "/deviceCheck/" + companyEmail + "/" + device);

        DatabaseReference deviceName = database.getReference(companyID + "/deviceName/" + companyEmail + "/" + device);

        DatabaseReference bossEmail = database.getReference(companyID + "/device/;" + device + "/" + userNameReplace + "/bossEmail");
        Log.e("比較",userName+" "+bossMail);
        GetDivice(companyID,userNameReplace,device);
        //      bossEmail.getKey();
//判斷是否為管理者 並顯示管理者按鈕
        if (userName.equals(bossMail)) {
            manage.setVisibility(View.VISIBLE);
        }

        //20161208 lin  V1.0.0 + 設定抓取資料最後 10筆 異常資料

        Query queryRef = database.getReference(companyID + "/deviceAlert/" + companyEmail + "/" + device).limitToLast(10);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic(topics_id);


        // FirebaseMessaging.getInstance().subscribeToTopic("SCSA321");
        //region Description 20161208 lin  V1.0.0 + 設定wordTextView陣列
        wordlayoutY = 1;
        wordLayoutX = 16;
        wordlaout = new LinearLayout[wordlayoutY * 4];
        worrdTextView = new TextView[wordlayoutY][wordLayoutX];
        gButton = new Button[wordlayoutY][wordLayoutX];
        BitTextView = new TextView[wordlayoutY][wordLayoutX];
        wordEdit = new TextView[wordlayoutY][wordLayoutX];
        for (int x = 0; x < wordlayoutY; x++) {
            wordlaout[x * 2] = new LinearLayout(this);
            wordlaout[x * 2].setWeightSum((float) 16.0);
            wordlaout[x * 2].setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams ButtonLayoutParam = new LinearLayout.LayoutParams(monitorsize.widthPixels/12, LinearLayout.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams EditLayoutParam = new LinearLayout.LayoutParams(monitorsize.widthPixels/7, LinearLayout.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float) 2.0);

            wordlaout[x * 2].setLayoutParams(ButtonLayoutParam);
            wordLinearLayout.addView(wordlaout[x * 2]);
            wordlaout[x * 2 + 1] = new LinearLayout(this);
            wordlaout[x * 2 + 1].setWeightSum((float) 16.0);
            wordlaout[x * 2 + 1].setOrientation(LinearLayout.VERTICAL);
            //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float)1.0);
            wordlaout[x * 2 + 1].setLayoutParams(param);

            wordLinearLayout.addView(wordlaout[x * 2 + 1]);

            wordlaout[x * 2 + 2] = new LinearLayout(this);
            wordlaout[x * 2 + 2].setWeightSum((float) 16.0);
            wordlaout[x * 2 + 2].setOrientation(LinearLayout.VERTICAL);
            wordlaout[x * 2 + 2].setLayoutParams(EditLayoutParam);
            wordLinearLayout.addView(wordlaout[x * 2 + 2]);

            wordlaout[x * 2 + 3] = new LinearLayout(this);
            wordlaout[x * 2 + 3].setWeightSum((float) 16.0);
            wordlaout[x * 2 + 3].setOrientation(LinearLayout.VERTICAL);
            wordlaout[x * 2 + 3].setLayoutParams(param);
            wordLinearLayout.addView(wordlaout[x * 2 + 3]);

            for (int y = 0; y < wordLayoutX; y++) {
                worrdTextView[x][y] = new TextView(this);
                int DeviceNumber = x * wordLayoutX + y;
                //將數字格式化兩位數數字
                DecimalFormat df = new DecimalFormat("00");
                String dfVelue = df.format(DeviceNumber);

                LinearLayout.LayoutParams TextParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float) 1.0);
                TextParam.setMargins(10, 10, 5, 5);
                worrdTextView[x][y].setText("M" + dfVelue + ":");
                worrdTextView[x][y].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                worrdTextView[x][y].setLayoutParams(TextParam);

                BitTextView[x][y] = new TextView((this));
                BitTextView[x][y].setText("D" + dfVelue + ":");
                BitTextView[x][y].setLayoutParams(TextParam);

                gButton[x][y] = new Button(this);

                gButton[x][y].setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                gButton[x][y].setLayoutParams(TextParam);

                wordEdit[x][y] = new TextView(this);
                wordEdit[x][y].setEnabled(false);
                wordEdit[x][y].setBackgroundColor(Color.parseColor("#D6D6AD"));
                wordEdit[x][y].setText("0.00");
                wordEdit[x][y].setTextColor(Color.parseColor("#272727"));
                wordEdit[x][y].setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                wordEdit[x][y].setLayoutParams(TextParam);
                wordEdit[x][y].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        detector.onTouchEvent(event);
                        return false;
                    }
                });
                wordlaout[x * 2 + 3].addView(worrdTextView[x][y]);
                wordlaout[x * 2 + 2].addView(wordEdit[x][y]);
                wordlaout[x * 2 + 1].addView(BitTextView[x][y]);
                wordlaout[x * 2 ].addView(gButton[x][y]);

                final String wodrDeviceNameText = "DevName_D" + dfVelue;
                final String bitDeviceNameText = "DevName_M" + dfVelue;
                final int testx = x, testy = y;
                deviceName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String wordWord = dataSnapshot.child(wodrDeviceNameText).getValue().toString();
                        //*//*   Log.d("測試", dataSnapshot.child(deviceNameText).getValue()+""+testy+testx);*//*
                        String bitWord = dataSnapshot.child(bitDeviceNameText).getValue().toString();
                        worrdTextView[testx][testy].setText(wordWord);
                        BitTextView[testx][testy].setText(bitWord);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                getTextAndButton(scsa);
              /*  Query dataQuery = scsa.orderByChild("timeStamp").limitToLast(10);


                dataQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        //region Description 20161213 lin v1.0.0 +抓取資料列表
                        Log.d("userNameReplace",companyID+"  "+companyEmail +"  " +userNameReplace+ "  "+device);
                        String messageText = dataSnapshot.child("message").getValue().toString();
                        Log.d("test", dataSnapshot.child("Dmessage").getValue().toString());
                        String wordMessageText = dataSnapshot.child("Dmessage").getValue().toString();
                        final String wordMessageTextData = wordMessageText.substring(wordMessageText.indexOf(",") + 1);
                        String messageDevice = messageText.substring(0, messageText.indexOf(","));
                        //  20161213 lin  V1.0.0 +固定格式抓取最後兩位
                        String messageTextSplit = messageDevice.substring(0, messageDevice.length() - 4);
                        messageDevice.substring(messageDevice.length() - 4);
                        //取出",以後的字"
                        String BitAdrText = messageText.substring(messageText.indexOf(",") + 1);

                        String testString = BitAdrText;
                        int deviceNumber;
                        for(int i=0;i<(wordMessageTextData.length()+1)/5;i++)
                        {
                            String words =wordMessageTextData;

                            int arryX=i/16;
                            int arryY=i%16;
                            wordEdit[arryX][arryY].setText(words.substring(i*5,i*5+4));
                            wordEdit[arryX][arryY].setTextSize(12);

                        }
                        deviceNumber = Integer.parseInt(messageDevice.substring(messageDevice.length() - 2)) + 8;
                        DecimalFormat df = new DecimalFormat("0000");
                        df.format(deviceNumber);
                        for (int i = 0; i < testString.length(); i++) {

                            int arryX = i / 16;
                            int arryY = i % 16;


                            // Log.d("arrX",arryX+""+"arryY"+arryY+"位置"+testString.substring(i,i+1)+"i"+i);
                            if ((testString.substring(i, i + 1)).equals("0")) {

//                        gButton[arryX][arryY].setText("Off");
                                gButton[arryX][arryY].setBackgroundColor(Color.RED);
                            } else if ((testString.substring(i, i + 1)).equals("1")) {
                                //                    gButton[arryX][arryY].setText("On");
                                gButton[arryX][arryY].setBackgroundColor(Color.GREEN);

                            }
                        }
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
                });*/

            }
        }
        //endregion

    }

    private void getTextAndButton(DatabaseReference scsa)
    {
        Query dataQuery = scsa.orderByChild("timeStamp").limitToLast(1);


        dataQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //取得現在時間 並將 斷線顯示隱藏
                dataSnapshot.child("timeStamp").getValue().toString();
                nowTimeStamp=Long.parseLong(dataSnapshot.child("timeStamp").getValue().toString());
                notyStatus=false;
                Online.setVisibility(View.INVISIBLE);
                //region Description 20161213 lin v1.0.0 +抓取資料列表
               // Log.d("userNameReplace",companyID+"  "+companyEmail +"  " +userNameReplace+ "  "+device);
                String messageText = dataSnapshot.child("message").getValue().toString();
                //Log.d("test", dataSnapshot.child("Dmessage").getValue().toString());
                String wordMessageText = dataSnapshot.child("Dmessage").getValue().toString();
                final String wordMessageTextData = wordMessageText.substring(wordMessageText.indexOf(",") + 1);
                String messageDevice = messageText.substring(0, messageText.indexOf(","));
                //  20161213 lin  V1.0.0 +固定格式抓取最後兩位
                String messageTextSplit = messageDevice.substring(0, messageDevice.length() - 4);
                messageDevice.substring(messageDevice.length() - 4);
                //取出",以後的字"
                String BitAdrText = messageText.substring(messageText.indexOf(",") + 1);

                String testString = BitAdrText;

                if ((wordMessageTextData.length()!=16*5-1)||testString.length()!=16){}
                else{
                int deviceNumber;
                for(int i=0;i<(wordMessageTextData.length()+1)/5;i++)
                {
                    String words =wordMessageTextData;
                    int getPosition=i%2;
                    int arryX=i/16;
                    int arryY=i%16;
                    if(getPosition==0)
                    {


                       // Log.d("完成測試"+i,words.substring(i*5,i*5+4)+" "+words.substring(i*5+5,i*5+4+5));
                        String test1= String.format("%.2f",getWord(words.substring(i*5,i*5+4),words.substring(i*5+5,i*5+4+5),i));
                        if(test1.equals("0.00"))
                        {

                        }else {
                            wordEdit[arryX][arryY / 2].setText(test1);
                        }
                    }

                   // wordEdit[arryX][arryY].setText(words.substring(i*5,i*5+4));

                    wordEdit[arryX][arryY].setTextSize(12);

                }

                deviceNumber = Integer.parseInt(messageDevice.substring(messageDevice.length() - 2)) + 8;
                DecimalFormat df = new DecimalFormat("0000");
                df.format(deviceNumber);


                for (int i = 0; i < testString.length(); i++) {

                    int arryX = i / 16;
                    int arryY = i % 16;


                    // Log.d("arrX",arryX+""+"arryY"+arryY+"位置"+testString.substring(i,i+1)+"i"+i);
                    if ((testString.substring(i, i + 1)).equals("0")) {

//                        gButton[arryX][arryY].setText("Off");
                        gButton[arryX][arryY].setBackgroundColor(Color.GREEN);
                    } else if ((testString.substring(i, i + 1)).equals("1")) {
                        //                    gButton[arryX][arryY].setText("On");
                        gButton[arryX][arryY].setBackgroundColor(Color.RED);

                    }
                }
                }




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
    }


    //monitThread.start();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences remdname = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = remdname.edit();
            edit.remove("companyID");
            edit.remove("userName");
            edit.remove("companyEmail");
            edit.remove("device");
            edit.commit();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, setupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_notyTest)
        {

            Intent intent = new Intent();
            Bundle bundle= this.getIntent().getExtras();
            intent.putExtras(bundle);
            intent.setClass(MainActivity.this, WordMoniter.class);
            startActivity(intent);
        }
        else if (id == R.id.deviceNameSet)
        {

            Intent intent = new Intent();

            Bundle bundle= this.getIntent().getExtras();
            intent.putExtras(bundle);
            intent.setClass(MainActivity.this, DeviceNameSet.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

        public void manageButton(View v)
        {
            Intent managePage=new Intent();
            Bundle manageBudle =this.getIntent().getExtras();
            manageBudle.putString("bossMail", bossMail);
            managePage.putExtras(manageBudle);

            managePage.setClass(MainActivity.this, manage.class);
            startActivity(managePage);

        }
    Handler mHandler = new Handler(){
        int i = 0;
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    i++;
Log.d("執行","123");

                    break;
            }
            super.handleMessage(msg);
            Online.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 220) {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
            Intent bitLogPage=new Intent();
            Bundle bitLogBudle =MainActivity.this.getIntent().getExtras();
            bitLogBudle.putString("bossMail", bossMail);
            bitLogPage.putExtras(bitLogBudle);

            bitLogPage.setClass(MainActivity.this, BitLogActivity.class);
            startActivity(bitLogPage);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            return true;
        } else if (e1.getX() - e2.getX() < -220) {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
            Intent ErrLogPage=new Intent();
            Bundle ErrLogBudle =MainActivity.this.getIntent().getExtras();
            ErrLogBudle.putString("bossMail", bossMail);
            ErrLogPage.putExtras(ErrLogBudle);

            ErrLogPage.setClass(MainActivity.this, ErrLogActivity.class);
            startActivity(ErrLogPage);
            overridePendingTransition(R.anim.push_right_in, R.anim. push_right_out);
            return true;
        }
        return true;
    }
    private void GetDivice(String companyID,String userNameReplace, final  String device)
    {
        DatabaseReference myDevices = FirebaseDatabase.getInstance().getReference(companyID+"/device/table/"+ userNameReplace);
//Log.e("資料位置1111",companyID+"/device/table/"+ userNameReplace);
        myDevices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                devices.clear();

                for (DataSnapshot childdataSnapshot : dataSnapshot.getChildren()) {
                    devices.add(childdataSnapshot.getKey());


                }
                spinnerAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, devices);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                devicemoniterSpinner.setAdapter(spinnerAdapter);
                int defaultSelect=0;
                for(int i=0 ;i<devices.size();i++)
                {
                    boolean compareDeviceName=false;
                    if(device.equals(devices.get(i).toString()))
                    {

                        defaultSelect=i;
                    }

                }
                Log.d("defaultSelect",""+defaultSelect+"");
                devicemoniterSpinner.setSelection(defaultSelect);

           /*     dviceSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        *//*String SelectValue =dviceSpinner.getSelectedItem().toString();
                        ((EditText)findViewById(R.id.device)).setText(SelectValue);*//*
                    }
                });*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public float getWord(String firValue,String endValue,int ii)
    {
        int i = Integer.parseInt(endValue, 16);
        int j = Integer.parseInt(firValue, 16);
        String en = Integer.toBinaryString(j);
        String st = Integer.toBinaryString(i);
        String bitValue = st+en;
        Float.intBitsToFloat(Integer.valueOf(bitValue,2));
        //Log.d("最後測試"+ii, Float.intBitsToFloat(Integer.valueOf(bitValue,2))+"");
        return  Float.intBitsToFloat(Integer.valueOf(bitValue,2));
    }
    public void refresh() {

        onCreate(null);

    }


}
