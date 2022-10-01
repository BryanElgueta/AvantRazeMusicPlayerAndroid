package com.example.Avantrz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    //Variables
    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn;
    DatabaseReference reference;
    FirebaseDatabase rootNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Para esconder el status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //
        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNo = findViewById(R.id.reg_phoneno);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);

        //Boton para regresar al login
        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });

        //guardar informacion en firebase onclick
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validando la informacion registrada
                if (!validateName() | !validateUsername() | !validateEmail() | !validatePhoneno() | !validatePassword()) {
                    return;
                }

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Users");

                //Obtener todos los valores en la cadena
                String name = regName.getEditText().getText().toString();
                String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneno = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();

                UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneno, password);
                reference.child(username).setValue(helperClass);
                Toast.makeText(SignUp.this, "Cuenta creada! Por favor inicia sesion. ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //validar funciones para el form de registro
    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();
        if (val.isEmpty()) {
            regName.setError("El nombre no puede estar vacio!");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        if (val.isEmpty()) {
            regUsername.setError("El nombre de usuario no puede estar vacio!");
            return false;
        } else if (val.length() >= 15) {
            regUsername.setError("Nombre de usuario muy largo!");
            return false;
        } else if (val.length() <= 4) {
            regUsername.setError("Nombre de usuario muy corto...");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            regEmail.setError("El email no puede estar vacio!");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Email invalido...");
            return false;
        } else {
            regEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneno() {
        String val = regPhoneNo.getEditText().getText().toString();
        String phonenoPattern = "(0/91)?[7-9][0-9]{8}";
        if (val.isEmpty()) {
            regPhoneNo.setError("El numero de telefono no puede estar vacio!");
            return false;
        } else if (!val.matches(phonenoPattern)) {
            regPhoneNo.setError("Numero de telefono invalido o no existe.");
            return false;
        } else {
            regPhoneNo.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        if (val.isEmpty()) {
            regPassword.setError("La contraseña no puede estar vacia!");
            return false;
        } else if (!val.matches(passwordPattern)) {
            regPassword.setError("Contraseña insegura. Vuelve a intentarlo con una mas segura.");
            return false;
        } else {
            regPassword.setError(null);
            return true;
        }
    }

}