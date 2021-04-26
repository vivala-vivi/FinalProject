package com.example.vtradeversion3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText pw;
    private EditText un;
    private Button login;
    private Button signup;
    private TextView error;
    FirebaseUser mUser;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "GoogleActivity";
    private Button btnSignOut;
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Attach views
        signup = findViewById(R.id.signup);
        pw = findViewById(R.id.pw);
        un = findViewById(R.id.un);
        login = findViewById(R.id.login);
        error = findViewById(R.id.error);
        signInButton = findViewById(R.id.sign_in_button);
        btnSignOut = findViewById(R.id.sign_out_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Init firebase
        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             mAuth.signOut();
               btnSignOut.setVisibility(View.INVISIBLE);
         }
      });
        getSupportActionBar().hide();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(MainActivity.this, "Signed In Successfully", Toast.LENGTH_LONG).show();
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e);
            Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_LONG).show();
            firebaseAuthWithGoogle(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Signed In Successfully", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(MainActivity.this, MarketActivity.class));


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Sign In Fail", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }

                    private void updateUI(FirebaseUser fUser) {
                        btnSignOut.setVisibility(View.VISIBLE);
                       GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    }
                });
    }

    public void onLoginClick(View v){
        String email = un.getText().toString();
        String password = pw.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(MainActivity.this, "Must enter email and password before logging in.",
                    Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            mUser = mAuth.getCurrentUser();
                            //error.setText("signInWithEmail:success " +  mUser.getEmail().toString());

                            startActivity(new Intent(MainActivity.this, MarketActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            error.setText("signInWithEmail:failure" +  task.getException().toString());

                        }
                    }
                });
    }

    public void onSignupClick(View v){
        String email = un.getText().toString();
        String password = pw.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(MainActivity.this, "Enter email and password, then click REGISTER.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
         //  error.setText("Enter email and password, then click register. ");
            return;
        }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(MainActivity.this, "Account created. Now click LOGIN.",
                                        Toast.LENGTH_LONG).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                            } else {
                                // If sign in fails, display a message to the user.
                                error.setText("createUserWithEmail:failure" + task.getException().toString());
                                Toast.makeText(MainActivity.this, "Failed Attempt. Enter email and password.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }

}