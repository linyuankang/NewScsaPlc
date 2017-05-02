package tw.com.scsa.newscsaplc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class setupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference scsa,Usedatabase,compneyInformation,deviceCheck;
    private String userUID;


    //下拉式元件
    private ArrayList<String> diviceSpinnerValue;
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> allItems;
    ArrayList<String> devices = new ArrayList<>();

    //基本畫面元件
    private Button setBtn;
    private Button EditPrdBtn;
    private Spinner dviceSpinner;
    //各種狀態
    boolean status=false;
    boolean spinnerStatus=false;
    boolean loginStatus=false;
    boolean hasShar=false;

    Bundle bundle = new Bundle();
    String companyEmail;
    public static final String KEY = "tw.com.scsa.newscsaplc";
    String topics_id="123";
    Context context;
    String databaseAdr;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        // region 更改上一頁成關閉
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            Intent intentHome = new Intent(Intent.ACTION_MAIN);
            intentHome.addCategory(Intent.CATEGORY_HOME);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intentHome);
            moveTaskToBack(true);
            return true;
        }
    //endregion
        return super.onKeyDown(keyCode, event);}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        // region 基本元件設定
        setBtn = (Button) findViewById(R.id.setBtn);
        EditPrdBtn= (Button) findViewById(R.id.EditPrdBtn);
        dviceSpinner= (Spinner) findViewById(R.id.divicespinner);
        diviceSpinnerValue=new ArrayList<String>();
        EditText userNameValueTemp= (EditText)findViewById(R.id.userName);
        TextView hyperText = (TextView)findViewById(R.id.hyperText);
        //endregion
        hyperText.setMovementMethod(LinkMovementMethod.getInstance());
        //宣告使用firebase帳號
        mAuth=FirebaseAuth.getInstance();
        context=this;



/*   // key 事件
        userNameValueTemp.setOnKeyListener((new EditText.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                    final String cID= ((EditText)findViewById(R.id.email)).getText().toString();
                    final String userID=((EditText)findViewById(R.id.userName)).getText().toString().replace(".","_");

                    final String dID=((EditText)findViewById(R.id.device)).getText().toString();
                    deviceCheck = database.getReference(cID+"/deviceCheck/"+companyEmail);
                    deviceCheck.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try
                            { dataSnapshot.getValue().toString();
                                Log.d("測試", dataSnapshot.getValue().toString());
                            spinnerStatus=true;
                            }
                            catch (Exception e)
                            {

                            }
                          *//*   if (spinnerStatus)
                             {
                                 GetDivice(cID,userID,dID);
                             }*//*
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                return false;
            }
        }));*/
//宣告成APP 所有畫面可以使用的 SharedPreferences
        SharedPreferences remdname = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        String companyIDValue = remdname.getString("companyID", "");
        String userNameValue = remdname.getString("userName", "");
        String companyEmailValue = remdname.getString("companyEmail", "");
        String deviceValue = remdname.getString("device", "");
        String  bossEmail= remdname.getString("bossEmail", "");
        final String  userPwd= remdname.getString("userPwd", "");
        ((EditText)findViewById(R.id.email)).setText(companyIDValue);
        ((EditText)findViewById(R.id.userName)).setText(userNameValue);
        ((EditText)findViewById(R.id.userPassword)).setText(userPwd);

        // region 設定 Shared有值的事件

        if(companyEmailValue!=""&companyIDValue!=""&userNameValue!=""&deviceValue!="")
        {
            String companyID=companyIDValue;
            String device=remdname.getString("device", "");
            String userNameReplace=remdname.getString("userName", "").replace(".","_");
            String userName=remdname.getString("userName", "");
            String companyEmail=remdname.getString("companyEmail", "");

            GetDivice(companyID,userNameReplace,device);
            ((EditText)findViewById(R.id.userName)).setText(userName);

                 scsa = database.getReference(companyID+"/deviceCheck/"+companyEmail+"/"+device);
                Usedatabase= database.getReference(companyID+"/device/"+device+"/"+userNameReplace);
                //DatabaseReference bossEmail= database.getReference(companyID+"/device/"+device+"/"+userNameReplace+"/bossEmail");
                 compneyInformation=database.getReference(companyID+"/device/"+device+"/"+userNameReplace);




            bundle.putString("companyID", companyID);

            bundle.putString("userName", userName);
            bundle.putString("userNameReplace", userNameReplace);
            bundle.putString("device", device);
            bundle.putString("companyEmail", companyEmail);
            bundle.putString("topics_id",topics_id);
            bundle.putString("bossEmail",bossEmail);

            Intent intent= new Intent();
            intent.putExtras(bundle);
            intent.setClass(setupActivity.this, MainActivity.class);
            // FirebaseMailLogin.this.startActivity(new Intent(FirebaseMailLogin.this,MainActivity.class));
            loginStatus=true;
            startActivity(intent);

        }
        //endregion
       ;
       // login();
        //取得使用者
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                 //   Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());

                    userUID= user.getUid();


                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");

                }

                // ...
            }

        };
        ///登入按鈕事件
        setBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                setUp();
            }
        });
        EditPrdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(setupActivity.this);
                LayoutInflater inflater = LayoutInflater.from(setupActivity.this);
                final View editpasswordView = inflater.inflate(R.layout.editpassword_dailog,null);
                builder.setTitle("輸入要增加員工的資料");
                builder.setView(editpasswordView) ;
                builder.setPositiveButton("確定修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText oldPwd = (EditText) (editpasswordView.findViewById(R.id.oldPwd));
                                oldPwd.setText(userPwd);
                                EditText EditNewPwd = (EditText) (editpasswordView.findViewById(R.id.EditNewPwd));
                                EditText editPwdAgain = (EditText) (editpasswordView.findViewById(R.id.editPwdAgain));
                                if(userPwd!=null) {
                                    Log.d("密碼", userPwd.toString());
                                    if(userPwd.equals(oldPwd.getText().toString()))
                                    {
                                        if(EditNewPwd.getText().toString().equals(editPwdAgain.getText().toString()))
                                        {
                                            if(editPwdAgain.getText().toString().length()>5) {
                                                Log.d("資料庫", scsa.getParent().toString());
                                                Usedatabase.child("password").setValue(editPwdAgain.getText().toString());
                                            }else
                                            {
                                                Toast.makeText(setupActivity.this,"密碼最少6各字",Toast.LENGTH_LONG).show();
                                            }

                                        }
                                        else
                                        {
                                            Toast.makeText(setupActivity.this,"前後密碼不正確",Toast.LENGTH_LONG).show();
                                        }
                                    }else
                                    {
                                        Toast.makeText(setupActivity.this,"原密碼輸入錯誤",Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(setupActivity.this,"請先登入一次再修改密碼",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                builder.show();

                /*final Intent intent =new Intent();
                final String companyID = ((EditText)findViewById(R.id.email))
                        .getText().toString();
                final String userNameReplace = ((EditText)findViewById(R.id.userName))
                        .getText().toString().replace(".","_");
                final String userName = ((EditText)findViewById(R.id.userName))
                        .getText().toString();

                final String device = ((EditText)findViewById(R.id.device))
                        .getText().toString();

                final String userPwd = ((EditText)findViewById(R.id.userPassword))
                        .getText().toString();*/

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //設定按鈕事件
    public void login(View v){


        setUp();


    }

    private void setUp()
    {

        loginStatus=false;
//宣告值 成FINAL 讓別的區域可以使用此區域變數
        final Intent intent =new Intent();
        final String companyID = ((EditText)findViewById(R.id.email))
                .getText().toString();
        final String userNameReplace = ((EditText)findViewById(R.id.userName))
                .getText().toString().replace(".","_");
        final String userName = ((EditText)findViewById(R.id.userName))
                .getText().toString();

        final String device = ((EditText)findViewById(R.id.device))
                .getText().toString();

        final String userPwd = ((EditText)findViewById(R.id.userPassword))
                .getText().toString();


        //20161208 lin  V1.0.0 + 資料庫位置
        scsa = database.getReference(companyID+"/deviceCheck/"+companyEmail+"/"+device);
        DatabaseReference deviceName = database.getReference(companyID+"/device");
       Usedatabase= database.getReference(companyID+"/device/"+device+"/"+userNameReplace);
        DatabaseReference bossEmail= database.getReference(companyID+"/device/"+device+"/"+userNameReplace+"/bossEmail");
        //DatabaseReference compneyInformation=database.getReference(companyID+"/device/"+device+"/"+userNameReplace);
       // DatabaseReference myDevices = FirebaseDatabase.getInstance().getReference(companyID+"/device/table/"+ userNameReplace);
//region 測試是否有此資料庫
        Usedatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //region 測試是否有此資料庫
                try
                {
                    String databaseAdr = dataSnapshot.getValue().toString();
                    dataSnapshot.child("bossEmail").getValue().toString();
                    //dataSnapshot.child(device).getValue().toString();
                    Log.d("所有資料",dataSnapshot.child("password").getValue().toString());
                    if(userPwd.equals(dataSnapshot.child("password").getValue().toString()))
                    {
                        status = true;
                    }
                    else
                    {
                    Toast.makeText(setupActivity.this,"密碼錯誤",Toast.LENGTH_LONG).show();
                }
                }
                catch (Exception e)
                {
                    Toast.makeText(setupActivity.this,"資料未完整輸入",Toast.LENGTH_LONG).show();
                }
                //endregion 測試是否有此資料庫
                //region 判斷是否進入下一頁
                if (status&(!loginStatus)) {
                    String companyEmail=dataSnapshot.child("bossEmail").getValue().toString().replace(".","_");
                    String device=  dataSnapshot.child("device").getValue().toString();
                    String bossEmail=dataSnapshot.child("bossEmail").getValue().toString();
                    String topics_id=dataSnapshot.child("topics_id").getValue().toString();


                    bundle.putString("companyID", companyID);
                    bundle.putString("userName", userName);
                    bundle.putString("userNameReplace", userNameReplace);
                    bundle.putString("device", device);
                    bundle.putString("companyEmail", companyEmail);
                    bundle.putString("bossEmail", bossEmail);
                    bundle.putString("topics_id",topics_id);


                    //記憶於手機
                    SharedPreferences remdname =  getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = remdname.edit();
                    edit.putString("companyID", companyID);
                    edit.putString("userName", userName);
                    edit.putString("companyEmail",companyEmail);
                    edit.putString("topics_id",topics_id);
                    edit.putString("device",device);
                    edit.putString("bossEmail",bossEmail);
                    edit.putString("userPwd",userPwd);
                    edit.commit();

                    intent.putExtras(bundle);
                    intent.setClass(setupActivity.this, MainActivity.class);
                    // FirebaseMailLogin.this.startActivity(new Intent(FirebaseMailLogin.this,MainActivity.class));
                    loginStatus=true;
                      startActivity(intent);
                }//endregion 判斷是否進入下一頁
                else if(!status&(loginStatus)){
                    Toast.makeText(setupActivity.this, "資料填寫錯誤11", Toast.LENGTH_SHORT).show();
                }
                GetDivice(companyID,userNameReplace,device);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
      /*  compneyInformation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                topics_id=dataSnapshot.child("topics_id").getValue().toString();
                Log.d("topics_id",topics_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        Query testQueryRef=scsa.limitToLast(1);
        //20161208 lin  V1.0.0 + 判斷是否有資料庫


       /* testQueryRef.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try
                {
                    String databaseAdr = dataSnapshot.getValue().toString();

                    status=true;

                }
                catch (Exception e)
                {
                    Toast.makeText(setupActivity.this, "資料填寫錯誤", Toast.LENGTH_SHORT).show();
                    status=false;
                }//20161208 lin  V1.0.0 + 判斷是否有資料庫 End
                //20161208 lin  V1.0.0 + 資料放入並開啟下頁
                if (status&(!loginStatus)) {


                    bundle.putString("companyID", companyID);
                    bundle.putString("userName", userName);
                    bundle.putString("userNameReplace", userNameReplace);
                    bundle.putString("device", device);
                    bundle.putString("companyEmail", companyEmail);
                    bundle.putString("topics_id",topics_id);


                    intent.putExtras(bundle);
                    intent.setClass(setupActivity.this, MainActivity.class);
                    // FirebaseMailLogin.this.startActivity(new Intent(FirebaseMailLogin.this,MainActivity.class));
                    loginStatus=true;
                    startActivity(intent);
                } else if(!status&(loginStatus)){
                    Toast.makeText(setupActivity.this, "資料填寫錯誤11", Toast.LENGTH_SHORT).show();
                }

                //20161208 lin  V1.0.0 + 資料放入並開啟下頁END

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
*/


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
                    Log.d("KEY在這",childdataSnapshot.getKey());

                }
                spinnerAdapter = new ArrayAdapter<String>(setupActivity.this, android.R.layout.simple_spinner_item, devices);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dviceSpinner.setAdapter(spinnerAdapter);
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
                dviceSpinner.setSelection(defaultSelect);
                dviceSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        ((EditText)findViewById(R.id.device)).setText( dviceSpinner.getSelectedItem().toString());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });
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


}
