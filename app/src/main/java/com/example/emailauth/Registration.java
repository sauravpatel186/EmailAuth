package com.example.emailauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private EditText r_pass, r_conpass, r_mobile, r_dob, r_mail, r_address, r_fullname;
    private String g, pass, conpass, mail, address, fname, mobile,dob,date;
    private static final String PASSWORD_PATTERN = "(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=](?=\\S+$)).{7,}$)";
    private static final String PHONE_PATTERN = "^[6-9][0-9]{9}$";
    Button reg_btn;
    String str;
    RadioButton r1,r2,radiobtn;
    RadioGroup rg;
    TextView gender;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



            mAuth = FirebaseAuth.getInstance();
            progressDialog =new ProgressDialog(Registration.this);
            r_address=(EditText)findViewById(R.id.r_address);
            r_fullname=(EditText)findViewById(R.id.r_fname);

            gender=(TextView)findViewById(R.id.gender);
            rg=(RadioGroup)findViewById(R.id.r_grp);
            r1=(RadioButton)findViewById(R.id.radio1);
            r2=(RadioButton)findViewById(R.id.radio2);
            r_pass = (EditText) findViewById(R.id.r_password);
            r_conpass = (EditText) findViewById(R.id.r_confirmpassword);
            r_mobile = (EditText) findViewById(R.id.r_mobileno);
            r_mail = (EditText) findViewById(R.id.r_email);
            reg_btn = (Button) findViewById(R.id.signup_btn);

            db= FirebaseFirestore.getInstance();
            rg.setOnCheckedChangeListener(this);


            reg_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register();

                }
            });
        }

        public void register() {

            intialize(); //initialize the input to  string variable
            if (!validate()) {
                Toast.makeText(this, "SignUp Has Failed", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setTitle("Registering");
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        String uid = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("Customer").document(uid);
                        Map<String, Object> reg = new HashMap<>();
                        reg.put("address", address);

                        reg.put("fname", fname);
                        reg.put("mobile", mobile);
                        reg.put("mail", mail);

                        documentReference.set(reg).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                startActivity(new Intent(Registration.this,Home.class));

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Registration.this, "Registration Unsuccessfull", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        }




        public boolean validate() {
            boolean valid = true;


            if (pass.isEmpty()) {
                r_pass.setError("Please Enter Password"); //Password is Empty
                valid = false;
            } else if (pass.length() < 7) {
                r_pass.setError("Password Cannot Be Less Than 8 Characters ");//Password Not Contained 7 Letters
                valid = false;
            } else {

                if (!pass.matches(PASSWORD_PATTERN)) {
                    r_pass.setError("Password Should Contain atleast one Lowercase letter,one Uppercase,one Special Character and number ");
                    valid = false;
                }


            }


            if (mail.isEmpty()) {//EMail is EMpty
                r_mail.setError("Email Cannot be Empty");
                valid = false;
            } else {//EMail not Empty
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!mail.matches(emailPattern))//Check Pattern
                {
                    r_mail.setError("Not Valid Email Address");
                    valid = false;
                }
            }
            if (!conpass.equals(pass))//Password and Confirm password MAtch {
            {
                r_conpass.setError("Password Not Matched");
                valid = false;
            }
            if(!mobile.matches(PHONE_PATTERN))

            {
                r_mobile.setError("Mobile Number Cannot Less Than 10 or More Than 10");
                valid=false;
            }
            if(fname.isEmpty())
            {
                r_fullname.setError("Cannot be Empty");
                valid=false;
            }
            if(address.isEmpty())
            {
                r_address.setError("Cannot Be Empty");
                valid=false;
            }



            return valid;

        }


        public void intialize()
        {
            address=r_address.getText().toString();
            fname=r_fullname.getText().toString();
            pass=r_pass.getText().toString().trim();
            conpass=r_conpass.getText().toString().trim();
            mail=r_mail.getText().toString().trim();
            mobile=r_mobile.getText().toString().trim();
            //  g=radiobtn.getText().toString().trim();
        }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId)
        {
            case R.id.radio1:
                str="Male";
                Log.d("Male","male");
                break;
            case R.id.radio2:
                str="Female";
                break;
            default:
                gender.setError("Please select any on option");

        }
    }
}