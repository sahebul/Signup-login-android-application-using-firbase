package getnow.com.mydairy;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import getnow.com.mydairy.Datasets.User;

public class SignupActivity extends AppCompatActivity {
    TextInputLayout etname,etemail,etphone,etpassword,etre_password;
    Button btnSignUp;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    FirebaseDatabase database;
    DatabaseReference collection_users;

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etname=(TextInputLayout)findViewById(R.id.txtName);
        etemail=(TextInputLayout)findViewById(R.id.txtEmail);
        etphone=(TextInputLayout)findViewById(R.id.txtPhone);
        etpassword=(TextInputLayout)findViewById(R.id.txtPassword);
        etre_password=(TextInputLayout)findViewById(R.id.txtConPassword);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        btnSignUp=(Button) findViewById(R.id.btnSignup);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        collection_users=database.getReference("Users"); //collection name Users


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate_field()){

                    registerUser();
                }
            }
        });

    }
    private void registerUser()
    {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(etemail.getEditText().getText().toString(),etpassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    final User user=new User(
                            etname.getEditText().getText().toString(),
                            etemail.getEditText().getText().toString(),
                            etphone.getEditText().getText().toString(),
                            etpassword.getEditText().getText().toString()

                    );

                    collection_users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignupActivity.this,"User registered successfully",Toast.LENGTH_LONG).show();
                            }else{
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(SignupActivity.this,"User already registered",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    });

                }else{
//                           Log.e(TAG, "onComplete: Failed=" + task.getException().getMessage());
                    Toast.makeText(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    boolean validate_field(){
                String name=etname.getEditText().getText().toString().trim();
                String email=etemail.getEditText().getText().toString().trim();
                String phone=etphone.getEditText().getText().toString().trim();
                String password=etpassword.getEditText().getText().toString().trim();
                String re_password=etre_password.getEditText().getText().toString().trim();
                if(name.isEmpty()){
                    etname.setError("Please enter your name");
                    etname.requestFocus();
                    return  false;
                }else{
                    etname.setError("");
                }

        if(phone.isEmpty()){
            etphone.setError("Please enter your phone number");
            etphone.requestFocus();
            return  false;
        }
        else if(phone.length() != 10){
            etphone.setError("Please enter a valid phone number");
            etphone.requestFocus();
            return  false;
        }else {
            etphone.setError("");
        }

        if(email.isEmpty()){
            etemail.setError("Please enter your email");
            etemail.requestFocus();
            return  false;
        }
        else if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()){
            etemail.setError("Please enter a valid email");
            etemail.requestFocus();
            return  false;
        }else {
            etemail.setError("");
        }


        if(password.isEmpty()){
            etpassword.setError("Please enter a password");
            etpassword.requestFocus();
            return  false;
        }
        else if(password.length() < 6){
            etpassword.setError("Password must contain 6 alpha-numeric");
            etpassword.requestFocus();
            return  false;
        }else{
            etpassword.setError("");
        }
        if(re_password.isEmpty()){
            etre_password.setError("Please re-enter password");
            etre_password.requestFocus();
            return  false;
        }
        else{
            etre_password.setError("");
        }
        //TODO : password matching
//        if(!email.matches(re_password)){
//            etre_password.setError("Password does not match");
//            etre_password.requestFocus();
//            return  false;
//        }

        return  true;
    }
}
