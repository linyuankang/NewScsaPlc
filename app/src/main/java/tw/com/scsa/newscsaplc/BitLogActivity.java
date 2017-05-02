package tw.com.scsa.newscsaplc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class BitLogActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private GestureDetector detector;
    private ViewFlipper flipper;
    String company, companyID, device, userName, userNameReplace, companyEmail, bossMail;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Button limitSetBtn;
    private ArrayAdapter<String> adapter;
    TextView limitText;
    int limitCount = 100;
    Deque<String> items;
    ListView limitListView;
    Intent intent1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_log);
        flipper = (ViewFlipper) this.findViewById(R.id.bitLogFliper);
        detector = new GestureDetector(this, this);
        Bundle bitLogBudle = this.getIntent().getExtras();
        companyID = bitLogBudle.getString("companyID");
        userName = bitLogBudle.getString("userName");
        device = bitLogBudle.getString("device");
        userNameReplace = bitLogBudle.getString("userNameReplace");
        companyEmail = bitLogBudle.getString("companyEmail");
        bossMail = bitLogBudle.getString("bossMail");

        limitListView= (ListView) findViewById(R.id.limitList);
        items = new LinkedList<>();
        limitListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });


        DatabaseReference scsa = database.getReference(companyID + "/deviceCheck/" + companyEmail + "/" + device);
        Query messageList = scsa.limitToLast(limitCount);
        messageList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String messageTemp=dataSnapshot.child("message").getValue().toString();
                String timeTemp=dataSnapshot.child("timeStamp").getValue().toString();
                Date dt=new Date(Long.valueOf(timeTemp));
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
                String time=sdf.format(dt);

                items.push(time+"  "+messageTemp);

                adapter = new ArrayAdapter(BitLogActivity.this,android.R.layout.simple_list_item_1, (List) items);
                limitListView.setAdapter(adapter);
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
        if (e1.getX() - e2.getX() < -220) {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
            BitLogActivity.this.setResult(RESULT_OK, intent1);
            BitLogActivity.this.finish();
            overridePendingTransition(R.anim.push_right_in, R.anim. push_right_out);
            return true;
        }
        return true;
    }
}
