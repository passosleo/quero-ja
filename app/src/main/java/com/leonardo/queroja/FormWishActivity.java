package com.leonardo.queroja;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.leonardo.queroja.entities.UserEntity;
import com.leonardo.queroja.entities.WishEntity;
import com.leonardo.queroja.enums.Priority;
import com.leonardo.queroja.global.UserSession;
import com.leonardo.queroja.repositories.WishRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FormWishActivity extends AppCompatActivity {

    private TextInputLayout titleField, descriptionField, linkField;
    private Spinner prioritySpinner;
    private WishRepository wishRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_wish);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        wishRepository = new WishRepository(this);

        titleField = findViewById(R.id.title_layout);
        descriptionField = findViewById(R.id.description_layout);
        linkField = findViewById(R.id.link_layout);
        prioritySpinner = findViewById(R.id.priority_spinner);

        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Priority.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
    }

    public void navigateToListWishes(View v) {
        Intent intent = new Intent(this, ListWishesActivity.class);
        startActivity(intent);
    }

    public void saveWish(View v) {
        titleField.setError(null);
        descriptionField.setError(null);
        linkField.setError(null);

        String title = Optional.ofNullable(titleField.getEditText())
                .map(EditText::getText)
                .map(CharSequence::toString)
                .orElse("");

        String description = Optional.ofNullable(descriptionField.getEditText())
                .map(EditText::getText)
                .map(CharSequence::toString)
                .orElse("");

        String link = Optional.ofNullable(linkField.getEditText())
                .map(EditText::getText)
                .map(CharSequence::toString)
                .orElse("");

        Map<TextInputLayout, String> errors = new HashMap<>();

        if (title.isEmpty()) {
            errors.put(titleField, "Título é obrigatório");
        }

        if (link.isEmpty()){
            errors.put(linkField, "Link é obrigatório");
        } else if (!Patterns.WEB_URL.matcher(link).matches()) {
            errors.put(linkField, "Link inválido");
        }

        if (!errors.isEmpty()) {
            for (Map.Entry<TextInputLayout, String> entry : errors.entrySet()) {
                entry.getKey().setError(entry.getValue());
            }
            return;
        }
        UserEntity loggedUser = UserSession.getInstance(this).getUser();


        WishEntity newWish = new WishEntity();
        newWish.setUserId(loggedUser.getUserId());
        newWish.setTitle(title);
        newWish.setDescription(description);
        newWish.setLink(link);
        newWish.setPriority((Priority) prioritySpinner.getSelectedItem());

        wishRepository.save(newWish);

        Toast.makeText(this, "Desejo cadastrado com sucesso", Toast.LENGTH_SHORT).show();
        navigateToListWishes(v);
    }
}