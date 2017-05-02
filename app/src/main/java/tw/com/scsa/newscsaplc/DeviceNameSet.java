package tw.com.scsa.newscsaplc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class DeviceNameSet extends AppCompatActivity {
    EditText[][] bitName;
    LinearLayout[] wordlaout;
    EditText[][] wordEdit;
    TextView[][] worrdTextView;
    TextView[][] BitTextView;
    DatabaseReference setDeviceName;
    Button setNameBtn;
    private LinearLayout setLinearLayout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String companyID, userName, device, bossMail, userNameReplace, companyEmail, topics_id;

    int wordLayoutX;
    int wordlayoutY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_name_set);
        setLinearLayout = (LinearLayout) findViewById(R.id.setNamelayoutgroup);
        setNameBtn= (Button) findViewById(R.id.setNameBtn);

        Bundle bundle = this.getIntent().getExtras();
        userName = bundle.getString("userName");
        device = bundle.getString("device");
        companyID = bundle.getString("companyID");
        userNameReplace = bundle.getString("userNameReplace");
        companyEmail = bundle.getString("companyEmail");
        topics_id = bundle.getString("topics_id");
        bossMail=bundle.getString("bossEmail");

        setDeviceName = database.getReference(companyID + "/deviceName/" + companyEmail + "/" + device);

        DisplayMetrics monitorsize = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(monitorsize);
        wordlayoutY = 1;
        wordLayoutX = 16;
        wordlaout = new LinearLayout[wordlayoutY * 4];
        worrdTextView = new TextView[wordlayoutY][wordLayoutX];
        bitName = new EditText[wordlayoutY][wordLayoutX];
        BitTextView = new TextView[wordlayoutY][wordLayoutX];
        wordEdit = new EditText[wordlayoutY][wordLayoutX];
        for (int x = 0; x < wordlayoutY; x++) {
            //宣告設定大小
            LinearLayout.LayoutParams ButtonLayoutParam = new LinearLayout.LayoutParams(monitorsize.widthPixels / 12, LinearLayout.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams EditLayoutParam = new LinearLayout.LayoutParams(monitorsize.widthPixels / 7, LinearLayout.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float) 2.0);
            //將基本的LAYOUT 加入
            wordlaout[x * 2] = new LinearLayout(this);
            wordlaout[x * 2].setWeightSum((float) 16.0);
            wordlaout[x * 2].setOrientation(LinearLayout.VERTICAL);
            wordlaout[x * 2].setLayoutParams(EditLayoutParam);
            setLinearLayout.addView(wordlaout[x * 2]);

            wordlaout[x * 2 + 1] = new LinearLayout(this);
            wordlaout[x * 2 + 1].setWeightSum((float) 16.0);
            wordlaout[x * 2 + 1].setOrientation(LinearLayout.VERTICAL);
            //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float)1.0);
            wordlaout[x * 2 + 1].setLayoutParams(param);
            setLinearLayout.addView(wordlaout[x * 2 + 1]);

            wordlaout[x * 2 + 2] = new LinearLayout(this);
            wordlaout[x * 2 + 2].setWeightSum((float) 16.0);
            wordlaout[x * 2 + 2].setOrientation(LinearLayout.VERTICAL);
            wordlaout[x * 2 + 2].setLayoutParams(EditLayoutParam );
            setLinearLayout.addView(wordlaout[x * 2 + 2]);

            wordlaout[x * 2 + 3] = new LinearLayout(this);
            wordlaout[x * 2 + 3].setWeightSum((float) 16.0);
            wordlaout[x * 2 + 3].setOrientation(LinearLayout.VERTICAL);
            wordlaout[x * 2 + 3].setLayoutParams(param);
            setLinearLayout.addView(wordlaout[x * 2 + 3]);

            for (int y = 0; y < wordLayoutX; y++) {
                worrdTextView[x][y] = new TextView(this);
                int DeviceNumber = x * wordLayoutX + y;
                //將數字格式化兩位數數字
                DecimalFormat df = new DecimalFormat("00");
                String dfVelue = df.format(DeviceNumber);
                LinearLayout.LayoutParams TextParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 1.0);
                //TextParam.setMargins(5, 5, 5, 5);
                worrdTextView[x][y].setText("M" + dfVelue + ":");
                worrdTextView[x][y].setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                worrdTextView[x][y].setLayoutParams(TextParam);

                BitTextView[x][y] = new TextView((this));
                BitTextView[x][y].setText("D" + dfVelue + ":");
                BitTextView[x][y].setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                BitTextView[x][y].setLayoutParams(TextParam);

                bitName[x][y] = new EditText(this);
                bitName[x][y].setPadding(10,0,0,0);
                bitName[x][y].setTextSize(12);
                bitName[x][y].setGravity(Gravity.LEFT);
                //bitName[x][y].setBackgroundColor(Color.parseColor("#D6D6AD"));
                bitName[x][y].setText("100A ");
                bitName[x][y].setLayoutParams(TextParam);

                wordEdit[x][y] = new EditText(this);
                wordEdit[x][y].setPadding(10,0,0,0);
                //wordEdit[x][y].setBackgroundColor(Color.parseColor("#D6D6AD"));
                wordEdit[x][y].setTextSize(12);
                //wordEdit[x][y].setTextColor(Color.parseColor("#272727"));
                wordEdit[x][y].setGravity(Gravity.LEFT);
                wordEdit[x][y].setText("100A ");
                wordEdit[x][y].setLayoutParams(TextParam);

                wordlaout[x * 2 + 2].addView(worrdTextView[x][y]);
                wordlaout[x * 2 + 3].addView(wordEdit[x][y]);
                wordlaout[x * 2 ].addView(BitTextView[x][y]);
                wordlaout[x * 2+1].addView(bitName[x][y]);

                final String wodrDeviceNameText = "DevName_D" + dfVelue;
                final String bitDeviceNameText = "DevName_M" + dfVelue;
                final int testx = x, testy = y;
                setDeviceName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String wordWord = dataSnapshot.child(wodrDeviceNameText).getValue().toString();
                        //*//*   Log.d("測試", dataSnapshot.child(deviceNameText).getValue()+""+testy+testx);*//*
                        String bitWord = dataSnapshot.child(bitDeviceNameText).getValue().toString();
                        wordEdit[testx][testy].setText(bitWord);
                        bitName[testx][testy].setText(wordWord);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                setNameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < wordlayoutY; i++) {
                            for (int j = 0; j < wordLayoutX; j++) {
                                worrdTextView[i][j].getText();
                                BitTextView[i][j].getText();

                                String wordValue= wordEdit[i][j].getText().toString();
                                String bitValue=bitName[i][j].getText().toString();
                              //  Log.d("WORD","DevName_"+ BitTextView[i][j].getText().toString().replace(":",""));
                                Log.d("DevName","DevName_"+  worrdTextView[i][j].getText().toString().replace(":",""));
                                String DevName_worrd ="DevName_"+ BitTextView[i][j].getText().toString().replace(":","");
                                String DevName_bit="DevName_"+ worrdTextView[i][j].getText().toString().replace(":","");
                                setDeviceName.child(DevName_worrd).setValue(bitValue);
                                setDeviceName.child(DevName_bit).setValue( wordValue);

                            }
                        }
                        Toast.makeText(DeviceNameSet.this,"修改完成",Toast.LENGTH_LONG).show();
                    }
                });

            }
        }
    }
}