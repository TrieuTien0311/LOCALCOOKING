package com.example.localcooking_v3t.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleSignInHelper {
    
    private static final String TAG = "GoogleSignInHelper";
    private final Context context;
    private final GoogleSignInClient googleSignInClient;
    
    public GoogleSignInHelper(Context context, String webClientId) {
        this.context = context;
        
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .requestProfile()
                .build();
        
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }
    
    public Intent getSignInIntent() {
        return googleSignInClient.getSignInIntent();
    }
    
    public GoogleSignInAccount handleSignInResult(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            return task.getResult(ApiException.class);
        } catch (ApiException e) {
            Log.e(TAG, "Google sign in failed", e);
            return null;
        }
    }
    
    public void signOut() {
        googleSignInClient.signOut();
    }
    
    public GoogleSignInAccount getLastSignedInAccount() {
        return GoogleSignIn.getLastSignedInAccount(context);
    }
}
