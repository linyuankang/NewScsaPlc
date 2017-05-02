package tw.com.scsa.newscsaplc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class manage extends AppCompatActivity {
    Button manageAdd,manageDel;
    addNewSuff  stuffDialog;

    ListView stuffListView;
    public DatabaseReference mAddStaff,mDelStaff ;
    ListView staffList;
    ArrayAdapter<String> adapter;
    ArrayList<String> stuffArry=new ArrayList<>();
    String selcetEmail="";
    String companyID,userName,device,bossMail,userNameReplace,companyEmail;
    String topics_id;
    String boss;
    String company;
    String password;
    //20161208 lin  V1.0.0 +listView初始化選擇為不選擇(小於0即可)
    int number_choose = -999;
    //20161208 lin  V1.0.0 +listView上一次的選擇
    View last_choose;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        manageAdd = (Button) findViewById(R.id.manageAdd);
        manageDel = (Button) findViewById(R.id.manageDel);
        selcetEmail="";
        manageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("selcetEmail" ,bossMail);
                if (selcetEmail!="") {
                        mDelStaff = FirebaseDatabase.getInstance().getReference(companyID + "/device/"+device+"/"+ selcetEmail.replace(".", "_"));
                    if(selcetEmail.equals(bossMail))
                    {

                        Toast.makeText(manage.this,"該使用者是你,請正確選擇使用者",Toast.LENGTH_LONG).show();
                    }
                    else {
                            mDelStaff.setValue(null);
                            mAddStaff = FirebaseDatabase.getInstance().getReference(companyID+"/device/table/"+ selcetEmail.replace(".", "_")+'/'+device);
                            mAddStaff.setValue(null);
                    }
                        }

                else
                            Toast.makeText(manage.this,"請正確選擇使用者",Toast.LENGTH_LONG).show();




            }
        });
        stuffListView= (ListView) findViewById(R.id.stuffListView);
        //20161208 lin  V1.0.0 +listView 設定選取顏色
        stuffListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number_choose = position;
                TextView tmp = (TextView) view;
                String stringTmp=tmp.getText().toString();
                selcetEmail= stringTmp.substring(stringTmp.indexOf("-") + 1);
Log.d("selcetEmail",selcetEmail);
                if (last_choose != null) {
                    last_choose.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }

                view.setBackgroundColor(Color.parseColor("#E0FFFF"));
                last_choose = view;
            }
        });
        //20161208 lin  V1.0.0 +listView 設定選取顏色 end
        Bundle bundle = getIntent().getExtras();
        getSharedPreferences("staffPrefs", MODE_PRIVATE);
        //20161208 lin  V1.0.0 + 接收上頁資料
        //account =bundle.getString("account");
             /*   Intent intent =new Intent();*/
        companyID=bundle.getString("companyID");
        userName=bundle.getString("userName");
        device=bundle.getString("device");
        userNameReplace=bundle.getString("userNameReplace");
        companyEmail=bundle.getString("companyEmail");
        bossMail=bundle.getString("bossMail");
        //20161208 lin  V1.0.0 + 接收上頁資料 END
        DatabaseReference scsa = database.getReference(companyID+"/deviceCheck/"+companyEmail+"/"+device);
        DatabaseReference bossEmail= database.getReference(companyID+"/device/"+device+"/"+userNameReplace+"/bossEmail");
        DatabaseReference staffdatabase =database.getReference(companyID+"/device/"+device);
        Log.d("我是誰",companyID+"/device/"+device);
        DatabaseReference compneyInformation=database.getReference(companyID+"/device/"+device+"/"+userNameReplace);
        //20161208 lin  V1.0.0 + 取得 firebase  公司資訊
        compneyInformation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boss=dataSnapshot.child("boss").getValue().toString();
                company=dataSnapshot.child("company").getValue().toString();
                topics_id=dataSnapshot.child("topics_id").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//20161208 lin  V1.0.0 + 取得 firebase  公司資訊
        //20161208 lin  V1.0.0 + 顯示現有公司使用者

        Query staffQurey=staffdatabase.orderByChild("timeStamp");

        staffdatabase.orderByChild("staffEmail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                stuffArry.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Log.d("測試",childSnapshot.getKey());
                    stuffArry.add(childSnapshot.child("staff").getValue().toString()+"-"+childSnapshot.getKey().toString().replace("_","."));

                }

                staffList = (ListView) findViewById(R.id.stuffListView);
                adapter = new ArrayAdapter(manage.this,android.R.layout.simple_list_item_1,stuffArry);
                staffList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    /*    staffQurey.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String StuffName=dataSnapshot.child("staff").getValue().toString();
                Log.i("staff",StuffName);
                String StuffEmail=dataSnapshot.child("staffEmail").getValue().toString();

                Log.d("staffEmail",StuffEmail);
                stuffArry.add(StuffName+"-"+StuffEmail);
                adapter = new ArrayAdapter(manage.this,android.R.layout.simple_list_item_1,stuffArry);
                stuffListView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String StuffName=dataSnapshot.child("staff").getValue().toString();
                Log.i("staff",StuffName);
                String StuffEmail=dataSnapshot.child("staffEmail").getValue().toString();

                Log.d("staffEmail",StuffEmail);
                stuffArry.add(StuffName+"-"+StuffEmail);
                adapter = new ArrayAdapter(manage.this,android.R.layout.simple_list_item_1,stuffArry);
                stuffListView.setAdapter(adapter);
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
    ////20161208 lin  V1.0.0 + 顯示Fragment
    public void click(View v) {
            switch (v.getId())
            {
            case R.id.manageAdd:
                Bundle bundle = getIntent().getExtras();

                //account =bundle.getString("account");
             /*   Intent intent =new Intent();*/
                companyID=bundle.getString("companyID");
                userName=bundle.getString("userName");
                userNameReplace=bundle.getString("userNameReplace");
                device=bundle.getString("device");
                bossMail=bundle.getString("bossMail");
                stuffDialog = new addNewSuff();
                stuffDialog.show(this.getFragmentManager(), "aa");


                break;

            }


        }////20161208 lin  V1.0.0 + 顯示Fragment END
    }

