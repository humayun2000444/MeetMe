package com.example.meetme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetme.utils.AndroidUtil;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginOtp extends AppCompatActivity {

    String phoneNumber;
    EditText otpInput;
    Button nextBtn;
    ProgressBar progressBar;
    TextView resendOtptext;
    Long timeoutSeconds = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        otpInput = findViewById(R.id.login_otp);
        nextBtn = findViewById(R.id.login_next);
        resendOtptext = findViewById(R.id.resend_otp_text);
        progressBar = findViewById(R.id.login_progress_bar);

        phoneNumber = Objects.requireNonNull(getIntent().getExtras()).getString("phone");

        sendOtp(phoneNumber);

    }

    void sendOtp(String phoneNumber){
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getApplicationContext(),"OPT verification faild");
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode =s;
                                resendingToken = forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(),"OPT sent successfully");
                                setInProgress(false);
                            }
                        });

        if(false){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential){

    }
}
