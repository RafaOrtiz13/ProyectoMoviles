package com.example.rafao.proyectomoviles.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafao.proyectomoviles.MainActivity;
import com.example.rafao.proyectomoviles.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.support.constraint.Constraints.TAG;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private Button btn;
    private EditText users,password;
    private String user, pass;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loginuv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        users = view.findViewById(R.id.editText);
        password = view.findViewById(R.id.editText2);

        btn = view.findViewById(R.id.btn2);
        btn.setOnClickListener(this);

    }

    public void singIn(){
        user = users.getText().toString();
        pass = password.getText().toString();

        mAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn2){
            singIn();
        }
    }
}
