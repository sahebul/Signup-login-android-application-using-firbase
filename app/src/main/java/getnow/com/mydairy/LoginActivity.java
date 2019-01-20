package getnow.com.mydairy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.regex.Pattern;

import getnow.com.mydairy.Utilities.Utils;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout etusername,etpassword;
    ProgressBar progress_bar;
    Button btnLogin;
    private FirebaseAuth mAuth;
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
        setContentView(R.layout.activity_login);
        etusername=(TextInputLayout)findViewById(R.id.txtUsername);
        etpassword=(TextInputLayout)findViewById(R.id.txtPassword);
        progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        if( mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),HomePage.class));
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate_field()){
                    chekLogin(etusername.getEditText().getText().toString(),etpassword.getEditText().getText().toString());
                }
            }
        });
    }
    public void signup(View v){
        Intent signup_page=new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(signup_page);
    }
    private void chekLogin(String email,String Password){
        progress_bar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress_bar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Login successfull",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                }else {
                    if(task.getException() instanceof FirebaseAuthInvalidUserException){
                        Toast.makeText(getApplicationContext(),"Invalid username or password",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
    boolean validate_field(){
        String email=etusername.getEditText().getText().toString().trim();
        String password=etpassword.getEditText().getText().toString().trim();


        if(email.isEmpty()){
            etusername.setError("Please enter your email");
            etusername.requestFocus();
            return  false;
        }
        else if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()){
            etusername.setError("Please enter a valid email");
            etusername.requestFocus();
            return  false;
        }
        else{
            etusername.setError("");
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
        }
        else {
            etpassword.setError("");
        }

        return  true;
    }
}
