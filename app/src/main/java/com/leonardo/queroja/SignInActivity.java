package com.leonardo.queroja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.leonardo.queroja.entities.UserEntity;
import com.leonardo.queroja.global.UserSession;
import com.leonardo.queroja.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout emailField, passwordField;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);

        emailField = findViewById(R.id.username_layout);
        passwordField = findViewById(R.id.password_layout);
    }

    public void navigateToSignUp(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void navigateToListWishes(View v) {
        Intent intent = new Intent(this, ListWishesActivity.class);
        startActivity(intent);
    }

    public void signIn(View v) {
        emailField.setError(null);
        passwordField.setError(null);

        String email = Optional.ofNullable(emailField.getEditText())
                .map(EditText::getText)
                .map(CharSequence::toString)
                .orElse("");

        String password = Optional.ofNullable(passwordField.getEditText())
                .map(EditText::getText)
                .map(CharSequence::toString)
                .orElse("");

        Map<TextInputLayout, String> errors = new HashMap<>();

        if (email.isEmpty()) {
            errors.put(emailField, "O e-mail é obrigatório");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.put(emailField, "E-mail inválido");
        }

        if (password.isEmpty()) {
            errors.put(passwordField, "A senha é obrigatória");
        }

        if (!errors.isEmpty()) {
            for (Map.Entry<TextInputLayout, String> entry : errors.entrySet()) {
                entry.getKey().setError(entry.getValue());
            }
            return;
        }

        UserEntity userExists = userRepository.findByEmail(email);

        if (userExists == null) {
            emailField.setError("Usuário não encontrado");
            return;
        }

        if (!userExists.getPassword().equals(password)) {
            passwordField.setError("Senha incorreta");
            return;
        }

        UserSession.getInstance(this).setUser(userExists);

        navigateToListWishes(v);
    }
}