package com.leonardo.queroja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.leonardo.queroja.entities.UserEntity;
import com.leonardo.queroja.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout nameField, emailField, passwordField, confirmPasswordField;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);

        nameField = findViewById(R.id.name_layout);
        emailField = findViewById(R.id.username_layout);
        passwordField = findViewById(R.id.password_layout);
        confirmPasswordField = findViewById(R.id.confirm_password_layout);
    }

    public void navigateToSignIn(View v) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void signUp(View v) {
        nameField.setError(null);
        emailField.setError(null);
        passwordField.setError(null);
        confirmPasswordField.setError(null);

        String name = Optional.ofNullable(nameField.getEditText())
                .map(EditText::getText)
                .map(CharSequence::toString)
                .orElse("");

        String email = Optional.ofNullable(emailField.getEditText())
                .map(EditText::getText)
                .map(CharSequence::toString)
                .orElse("");

        String password = Optional.ofNullable(passwordField.getEditText())
                .map(EditText::getText)
                .map(CharSequence::toString)
                .orElse("");

        String confirmPassword = Optional.ofNullable(confirmPasswordField.getEditText())
                .map(EditText::getText)
                .map(CharSequence::toString)
                .orElse("");

        Map<TextInputLayout, String> errors = new HashMap<>();

        if (name.isEmpty()) {
            errors.put(nameField, "O nome é obrigatório");
        } else if (name.length() < 3) {
            errors.put(nameField, "O nome deve ter pelo menos 3 caracteres");
        } else if (name.length() > 50) {
            errors.put(nameField, "O nome deve ter no máximo 50 caracteres");
        }

        if (email.isEmpty()) {
            errors.put(emailField, "O e-mail é obrigatório");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.put(emailField, "E-mail inválido");
        }

        if (password.isEmpty()) {
            errors.put(passwordField, "A senha é obrigatória");
        } else if (password.length() < 6) {
            errors.put(passwordField, "A senha deve ter pelo menos 6 caracteres");
        } else if (password.length() > 20) {
            errors.put(passwordField, "A senha deve ter no máximo 20 caracteres");
        }

        if (confirmPassword.isEmpty()) {
            errors.put(confirmPasswordField, "A confirmação de senha é obrigatória");
        } else if (confirmPassword.length() < 6) {
            errors.put(confirmPasswordField, "A confirmação de senha deve ter pelo menos 6 caracteres");
        } else if (confirmPassword.length() > 20) {
            errors.put(confirmPasswordField, "A confirmação de senha deve ter no máximo 20 caracteres");
        }

        if (!password.equals(confirmPassword)) {
            errors.put(confirmPasswordField, "As senhas não coincidem");
        }

        if (!errors.isEmpty()) {
            for (Map.Entry<TextInputLayout, String> entry : errors.entrySet()) {
                entry.getKey().setError(entry.getValue());
            }
            return;
        }

        UserEntity userWithSameEmail = userRepository.findByEmail(email);

        if (userWithSameEmail != null) {
            emailField.setError("E-mail já cadastrado");
            return;
        }

        UserEntity newUser = new UserEntity();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);

        userRepository.save(newUser);

        Toast.makeText(this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
        navigateToSignIn(v);
    }
}