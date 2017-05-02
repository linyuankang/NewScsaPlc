package tw.com.scsa.newscsaplc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Initial_1 on 2016/12/6.
 */

public class addNewSuff extends DialogFragment implements
        android.content.DialogInterface.OnClickListener{
    public DatabaseReference mAddStaff,mDelStaff ;
    TextView newAccountEmail,newAccountName,getNewAccountPhone,checkPasswrod,newAccountPasswrod;
    String  userNameReplace,userName,device,bossMail;
    String companyId;
    String topics_id;
    String boss;
    String bossEmail;
    String company;
    String password;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        LinearLayout root = (LinearLayout) inflater.inflate(R.layout.addnewsuff, null);

        newAccountEmail = (TextView) root.findViewById(R.id.newAccountEmail);
        newAccountName= (TextView) root.findViewById(R.id.newAccountName);
        getNewAccountPhone= (TextView) root.findViewById(R.id.newAccountPhone);
        newAccountPasswrod=(TextView) root.findViewById(R.id.newAccountPasswrod);
        checkPasswrod=(TextView) root.findViewById(R.id.checkPasswrod);

        AlertDialog.Builder builder1 = builder.setView(root)
                .setPositiveButton("新增", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        companyId=((manage) getActivity()).companyID.toString();
                        userName=((manage) getActivity()).userName.toString();
                        userNameReplace=((manage) getActivity()).userNameReplace.toString();
                        device= ((manage) getActivity()).device.toString();
                        bossMail= ((manage) getActivity()).bossMail.toString();
                        topics_id=((manage) getActivity()).topics_id.toString();
                        boss=((manage) getActivity()).boss.toString();

                        company=((manage) getActivity()).company.toString();
Log.d("bossemail",bossMail);

                        boolean emailTest= android.util.Patterns.EMAIL_ADDRESS.matcher(newAccountEmail.getText().toString()).matches();
                        if(emailTest)
                        {
                            if(newAccountPasswrod.getText().toString().equals(checkPasswrod.getText().toString())) {
                                mAddStaff = FirebaseDatabase.getInstance().getReference(companyId + "/device/" + device + "/" + newAccountEmail.getText().toString().replace(".", "_"));
                                //password = mAddStaff.push().getKey();
                                password = mAddStaff.push().getKey();

                                mAddStaff.child("staff").setValue(newAccountName.getText().toString());
                                mAddStaff.child("staffEmail").setValue(newAccountEmail.getText().toString());
                                mAddStaff.child("staffPhoneNumber").setValue(getNewAccountPhone.getText().toString());
                                mAddStaff.child("used").setValue("false");
                                mAddStaff.child("password").setValue(password);
                                mAddStaff.child("companyId").setValue(companyId);
                                mAddStaff.child("boss").setValue(boss);
                                mAddStaff.child("bossEmail").setValue(bossMail);
                                mAddStaff.child("company").setValue(company);
                                mAddStaff.child("device").setValue(device);
                                mAddStaff.child("topics_id").setValue(topics_id);
                                mAddStaff.child("staffPassword").setValue(newAccountPasswrod.getText().toString());


                                mAddStaff = FirebaseDatabase.getInstance().getReference(companyId + "/device/table/" + userNameReplace + '/' + device);
                                mAddStaff.setValue("friend");
                            }else
                                Toast.makeText(getActivity(),"密碼不相同",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getActivity(),"請依照Email格式填寫",Toast.LENGTH_LONG).show();

                    }
                });
        return builder.create();
    }



    @Override
    public void onClick(DialogInterface dialog, int which) {
        
    }



}
