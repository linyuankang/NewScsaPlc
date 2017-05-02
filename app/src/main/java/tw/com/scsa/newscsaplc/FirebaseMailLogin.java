package tw.com.scsa.newscsaplc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseMailLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userUID;
    private Button sinout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_mail_login);

        mAuth=FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());

                    userUID= user.getUid();
                    Intent intent =new Intent();
                    intent.setClass(FirebaseMailLogin.this,setupActivity.class);
                    //  FirebaseMailLogin.this.startActivity(new Intent(FirebaseMailLogin.this,AppActivity.class));
                    startActivity(intent);
                   // Toast.makeText(FirebaseMailLogin.this,userUID+"登入成功",Toast.LENGTH_LONG).show();
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");

                }

                // ...
            }

        };

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
    public void login(View v){
        final String email = "linyuankang@yahoo.com.tw";
        final String password = "ac41702";
        Log.d("AUTH", email+"/"+password);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("onComplete", "onComplete");
                        if (!task.isSuccessful()){
                            Log.d("onComplete", "登入失敗");
                             register(email, password);
                        }
                    }
                });
    }
    private void register(final String email, final String password) {
        new AlertDialog.Builder(FirebaseMailLogin.this)
                .setTitle("登入問題")
                .setMessage("無此帳號，是否要以此帳號與密碼註冊?")
                .setPositiveButton("註冊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createUser(email, password);
                    }
                })
                .setNeutralButton("取消", null)
                .show();
    }
    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String message =
                                        task.isComplete() ? "註冊成功" : "註冊失敗";
                                new AlertDialog.Builder(FirebaseMailLogin.this)
                                        .setMessage(message)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        });
    }

}