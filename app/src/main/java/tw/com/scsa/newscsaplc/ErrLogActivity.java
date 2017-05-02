package tw.com.scsa.newscsaplc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ErrLogActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector detector;
    private ViewFlipper flipper;
    String companyID,userName,device,bossMail,userNameReplace,companyEmail,topics_id;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Deque<String> items;
    private ArrayAdapter<String> adapter;
    private ListView errList;
    Intent intent1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_err_log);
        errList= (ListView) findViewById(R.id.errList1);
        flipper = (ViewFlipper) this.findViewById(R.id.errFlipper);
        detector = new GestureDetector(this, this);
        items = new LinkedList<String>();

errList.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        return false;
    }
});
        Bundle bundle = this.getIntent().getExtras();
        userName =bundle.getString("userName");
        device =bundle.getString("device");
        companyID =bundle.getString("companyID");
        userNameReplace=bundle.getString("userNameReplace");
        companyEmail=bundle.getString("companyEmail");
        topics_id=bundle.getString("topics_id");
        Query queryRef =database.getReference(companyID+"/deviceAlert/"+companyEmail+"/"+device).limitToLast(10);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int i =0;
                //  Log.d("TEST",dataSnapshot.child("message").getValue().toString());
                //

                int pointPosition=(dataSnapshot.child("message").getValue().toString()).indexOf(",")+1;//找到點的位子
                String childMessageText=(dataSnapshot.child("message").getValue().toString());//點的位子以後得字串
                String childTimeText=dataSnapshot.child("timeStamp").getValue().toString();
                Date dt=new Date(Long.valueOf(childTimeText));
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
                String time=sdf.format(dt);
                Log.d("test",childMessageText);
                items.push(time+"   "+childMessageText);
              /*  timeItems1.add(time);*/

                HashMap<String , String> hashMap = new HashMap<>();
                hashMap.put("title" ,childMessageText+"");
                hashMap.put("text" , time);
                //把title , text存入HashMap之中

                // String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(Integer.valueOf(childTimeText) * 1000));
                adapter = new ArrayAdapter(ErrLogActivity.this,android.R.layout.simple_list_item_1, (List) items);
                errList.setAdapter(adapter);

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
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));

            ErrLogActivity.this.setResult(RESULT_OK, intent1);
            ErrLogActivity.this.finish();
            overridePendingTransition(R.anim.push_left_in, R.anim. push_left_out);
            return true;
        }
        return true;
    }
}
