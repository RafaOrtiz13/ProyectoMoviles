package com.example.rafao.proyectomoviles.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rafao.proyectomoviles.MainActivity;
import com.example.rafao.proyectomoviles.Models.Usuario;
import com.example.rafao.proyectomoviles.PrincipalPage;
import com.example.rafao.proyectomoviles.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button btn;
    private EditText users, password;
    private String user, pass;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference root;
    private SharedPreferences sp;
    private Intent intent;

    //SessionManagement session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loginuv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        root = database.getReference("/Usuarios");
        sp = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        users = view.findViewById(R.id.editText);
        password = view.findViewById(R.id.editText2);

        btn = view.findViewById(R.id.btn2);
        btn.setOnClickListener(this);

    }

    public MainActivity a;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



        if (context instanceof Activity){
            a=(MainActivity) context;
        }

    }

    public void singIn() {
        user = users.getText().toString();
        pass = password.getText().toString();
        if (pass.equals("") || user.equals("")) {
            Toast.makeText(getContext(), "Introduzca la información solicitada.",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            root.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                                        Usuario userLogin = item.getValue(Usuario.class);
                                        if (userLogin.correo.equals(user)) {
                                            if (userLogin.habilitado != 0) {
                                                //session.createLoginSession(userLogin.nombre,user);
                                                intent = new Intent(a, PrincipalPage.class);
                                                intent.putExtra("user", userLogin);
                                                save();
                                                startActivity(intent);
                                                //getActivity().finish();
                                            } else {
                                                Toast.makeText(getContext(), "No puedes iniciar sesion.\nContacta al administrador.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getContext(), "No existe este usuario en la base de datos.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void save() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", user);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn2) {
            singIn();
        }
    }
}
