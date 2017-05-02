package tw.com.scsa.newscsaplc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WordMoniter extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private GestureDetector detector;
    private ViewFlipper flipper;
    private ListView listInput;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private ArrayList<String> timeItems1;
    private LinearLayout M0000 ,M0008;
    Intent intent1;
    private  LinearLayout wordLinearLayout;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    LinearLayout[] wordlaout;
    TextView[][] wordEdit;
    TextView[][] worrdTextView;
    TextView[][] BitTextView;
    Button[][] gButton;
    String[][] textvalue;
    Button firebaseSetBtn;
    String firebaseSetValue;
    int wordLayoutX;
    int wordlayoutY;
    int gXArraySize;
    int gYArraySize;
    String companyID,userName,device,bossMail,userNameReplace,companyEmail,topics_id;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_moniter);
        wordLinearLayout= (LinearLayout) findViewById(R.id.wordlayoutgroup);
        flipper = (ViewFlipper) this.findViewById(R.id.wordMoniterFlipper);
        detector = new GestureDetector(this, this);
        firebaseSetBtn= (Button) findViewById(R.id.firebaseSetBtn);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                        Thread.sleep(50000);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

// 抓取前頁資料
        Bundle bundle = this.getIntent().getExtras();
        userName =bundle.getString("userName");
        device =bundle.getString("device");
        companyID =bundle.getString("companyID");
        userNameReplace=bundle.getString("userNameReplace");
        companyEmail=bundle.getString("companyEmail");
        topics_id=bundle.getString("topics_id");



//DAtabase 區域宣告
        final DatabaseReference scsa = database.getReference(companyID+"/deviceChange/"+companyEmail+"/"+device);
        DatabaseReference deviceName = database.getReference(companyID+"/deviceName/"+companyEmail+"/"+device);
        DatabaseReference bossEmail= database.getReference(companyID+"/device/"+device+"/"+userNameReplace+"/bossEmail");
Log.d("網址",companyID+"/deviceChange/"+companyEmail+"/"+device);

        wordlayoutY=1;
        wordLayoutX=16;
        wordlaout=new LinearLayout[wordlayoutY*4];
        worrdTextView=new TextView[wordlayoutY][wordLayoutX];
        gButton = new Button[wordlayoutY][wordLayoutX];
        BitTextView=new TextView[wordlayoutY][wordLayoutX];
        wordEdit=new TextView[wordlayoutY][wordLayoutX];
        textvalue=new String[wordlayoutY][wordLayoutX];
        firebaseSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseSetValue="";

                for(int i=0;i<wordlayoutY;i++ )
                {

                    for(int j=0;j<wordLayoutX;j++)
                    {

                            firebaseSetValue = firebaseSetValue + textvalue[i][j].toString();



                    }
                }
                scsa.child("Moniter_Test").setValue(firebaseSetValue);
            }
        });
        for(int x=0;x<wordlayoutY;x++)
        {
            wordlaout[x*2]=new LinearLayout(this);
            wordlaout[x*2].setWeightSum((float)16.0);
            wordlaout[x*2].setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams EditLayoutParam = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float)2.0);

            wordlaout[x*2].setLayoutParams(EditLayoutParam);
            wordLinearLayout.addView(wordlaout[x*2]);
            wordlaout[x*2+1]=new LinearLayout(this);
            wordlaout[x*2+1].setWeightSum((float)16.0);
            wordlaout[x*2+1].setOrientation(LinearLayout.VERTICAL);
            //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float)1.0);
            wordlaout[x*2+1].setLayoutParams(param);

            wordLinearLayout.addView(wordlaout[x*2+1]);

            wordlaout[x*2+2]=new LinearLayout(this);
            wordlaout[x*2+2].setWeightSum((float)16.0);
            wordlaout[x*2+2].setOrientation(LinearLayout.VERTICAL);
            wordlaout[x*2+2].setLayoutParams(EditLayoutParam);
            wordLinearLayout.addView(wordlaout[x*2+2]);

            wordlaout[x*2+3]=new LinearLayout(this);
            wordlaout[x*2+3].setWeightSum((float)16.0);
            wordlaout[x*2+3].setOrientation(LinearLayout.VERTICAL);
            wordlaout[x*2+3].setLayoutParams(param);
            wordLinearLayout.addView(wordlaout[x*2+3]);

            for(int y=0;y<wordLayoutX;y++)
            {
                worrdTextView[x][y]=new TextView(this);
                int DeviceNumber= x*wordLayoutX+y;
                //將數字格式化兩位數數字
                DecimalFormat df = new DecimalFormat("00");
                String dfVelue= df.format(DeviceNumber);

                LinearLayout.LayoutParams TextParam = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float)1.0);
                TextParam.setMargins(5, 5, 5, 5);
                worrdTextView[x][y].setText("M"+dfVelue+":");
                worrdTextView[x][y].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                worrdTextView[x][y].setLayoutParams(TextParam);

                BitTextView[x][y] =new TextView((this));
                BitTextView[x][y].setText("D"+dfVelue+":");
                BitTextView[x][y].setLayoutParams(TextParam);

                gButton[x][y]=new Button(this);

                gButton[x][y].setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                gButton[x][y].setLayoutParams(TextParam);
final int stringX=x,stringY=y;
                gButton[x][y].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      Log.d  ("第幾個",textvalue[stringX][stringY]);
                        if(stringX*wordlayoutY+stringY!=9||textvalue[stringX][stringY].equals("1")) {

                            if (textvalue[stringX][stringY].toString().equals("0")) {
                                textvalue[stringX][stringY] = "1";
                                v.setBackgroundColor(Color.RED);
                            } else {
                                textvalue[stringX][stringY] = "0";
                                v.setBackgroundColor(Color.GREEN);
                            }
                            firebaseSetValue = "";
                            for (int i = 0; i < wordlayoutY; i++) {
                                for (int j = 0; j < wordLayoutX; j++) {
                                    firebaseSetValue = firebaseSetValue + textvalue[i][j].toString();

                                }
                            }
                        }
                        else
                        {
                            firebaseSetValue ="0000000001000000";
                            for (int i = 0; i < wordlayoutY; i++) {
                                for (int j = 0; j < wordLayoutX; j++) {
                                    v.setBackgroundColor(Color.GREEN);

                                }
                            }
                            gButton[stringX][stringY].setBackgroundColor(Color.RED);
                            textvalue[stringX][stringY]="1";
                        }
                        scsa.child("Moniter_Test").setValue(firebaseSetValue);

                    }
                });

                wordEdit[x][y]=new TextView(this);
                wordEdit[x][y].setEnabled(false);
                wordEdit[x][y].setText("100A ");
                wordEdit[x][y].setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                wordEdit[x][y].setLayoutParams(TextParam);
                wordEdit[x][y].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        detector.onTouchEvent(event);
                        return false;
                    }
                });
               // wordlaout[x*2+3].addView(worrdTextView[x][y]);
               // wordlaout[x*2+2].addView(wordEdit[x][y]);
                wordlaout[x*2+1].addView(BitTextView[x][y]);
                wordlaout[x*2].addView(gButton[x][y]);

                final String wodrDeviceNameText="DevName_D"+dfVelue;
                final String bitDeviceNameText="DevName_M"+dfVelue;
                final int testx =x ,testy=y;
                deviceName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String wordWord= dataSnapshot.child(wodrDeviceNameText).getValue().toString();
                     //*//*   Log.d("測試", dataSnapshot.child(deviceNameText).getValue()+""+testy+testx);*//*
                        String bitWord= dataSnapshot.child(bitDeviceNameText).getValue().toString();
                        worrdTextView[testx][testy].setText(wordWord);
                        BitTextView[testx][testy].setText(bitWord);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
             /*   Query dataQuery = scsa.orderByChild("timeStamp").limitToLast(10);*/


                scsa.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //region Description 20161213 lin v1.0.0 +抓取資料列表

                        String  messageDevice=   dataSnapshot.child("Moniter_Test").getValue().toString();
                        //Log.d( "test",dataSnapshot.child("Dmessage").getValue().toString());
                        // String wordMessageText= dataSnapshot.child("Dmessage").getValue().toString();
                        //final  String wordMessageTextData=wordMessageText.substring(wordMessageText.indexOf(",")+1);
                        // String messageText=messageDevice.substring(0,messageDevice.indexOf(","));
                        //  20161213 lin  V1.0.0 +固定格式抓取最後兩位
                        // String messageTextSplit = messageDevice.substring(0,messageDevice.length()-4);
                        //messageDevice.substring(messageDevice.length()-4);
                        //取出",以後的字"
                        //String BitAdrText=messageText.substring(messageText.indexOf(",")+1);

                        String testString=messageDevice;
                        int deviceNumber;
                       /* for(int i=0;i<(wordMessageTextData.length()+1)/5;i++)
                        {
                            String words =wordMessageTextData;
                            int arryX=i/4;
                            int arryY=i%4;
                            wordEdit[arryX][arryY].setText(words.substring(i*5,i*5+4));
                            wordEdit[arryX][arryY].setTextSize(12);

                        }*/
                        deviceNumber= Integer.parseInt(messageDevice.substring(messageDevice.length()-2))+8;
                        DecimalFormat df = new DecimalFormat("0000");
                        df.format(deviceNumber);
                        for( int i = 0; i < testString.length(); i ++ )
                        {

                            int arryX=i/16;
                            int arryY=i%16;
                            textvalue[arryX][arryY]=(testString.substring(i,i+1));

                            // Log.d("arrX",arryX+""+"arryY"+arryY+"位置"+testString.substring(i,i+1)+"i"+i);
                            if((testString.substring(i,i+1)).equals("0")) {

//                        gButton[arryX][arryY].setText("Off");
                                gButton[arryX][arryY].setBackgroundColor(Color.RED);

                            }
                            else if((testString.substring(i,i+1)).equals("1"))
                            {
                                //                    gButton[arryX][arryY].setText("On");
                                gButton[arryX][arryY].setBackgroundColor(Color.GREEN);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }
        //endregion

    }
    Handler mHandler = new Handler(){
        int i = 0;
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    i++;
                    Log.d("帥唷",Integer.toString(i));
                    break;
            }
            super.handleMessage(msg);
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
        return false;
    }
}
