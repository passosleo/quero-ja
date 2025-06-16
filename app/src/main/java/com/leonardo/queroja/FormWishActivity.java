package com.leonardo.queroja;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.leonardo.queroja.entities.UserEntity;
import com.leonardo.queroja.entities.WishEntity;
import com.leonardo.queroja.enums.Priority;
import com.leonardo.queroja.enums.Status;
import com.leonardo.queroja.global.UserSession;
import com.leonardo.queroja.repositories.WishRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FormWishActivity extends AppCompatActivity {

    private TextInputLayout titleField, descriptionField, linkField;
    private Spinner prioritySpinner;
    private WishRepository wishRepository;
    private TextView formTitle;
    private Integer wishId = null;
    private WishEntity wishToEdit = null;

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
        formTitle = findViewById(R.id.form_title);

        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Priority.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        wishId = getIntent().hasExtra("wish_id") ? getIntent().getIntExtra("wish_id", -1) : null;

        if (wishId != null && wishId != -1) {
            wishToEdit = wishRepository.findById(wishId).orElse(null);
            if (wishToEdit != null) {
                formTitle.setText("Editar Desejo");
                populateFields(wishToEdit);
            } else {
                Toast.makeText(this, "Erro ao carregar o desejo para edição", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            formTitle.setText("Novo Desejo");
        }
    }

    private void populateFields(WishEntity wish) {
        Optional.ofNullable(titleField.getEditText()).ifPresent(e -> e.setText(wish.getTitle()));
        Optional.ofNullable(descriptionField.getEditText()).ifPresent(e -> e.setText(wish.getDescription()));
        Optional.ofNullable(linkField.getEditText()).ifPresent(e -> e.setText(wish.getLink()));

        Priority[] priorities = Priority.values();
        for (int i = 0; i < priorities.length; i++) {
            if (priorities[i] == wish.getPriority()) {
                prioritySpinner.setSelection(i);
                break;
            }
        }

    }

    public void navigateToListWishes(View v) {
        Intent intent = new Intent(this, ListWishesActivity.class);
        startActivity(intent);
        finish();
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

        if (link.isEmpty()) {
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
        Priority selectedPriority = (Priority) prioritySpinner.getSelectedItem();

        if (wishToEdit != null) {
            wishToEdit.setTitle(title);
            wishToEdit.setDescription(description);
            wishToEdit.setLink(link);
            wishToEdit.setPriority(selectedPriority);

            wishRepository.update(wishToEdit);
            Toast.makeText(this, "Desejo atualizado com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            WishEntity newWish = new WishEntity();
            newWish.setUserId(loggedUser.getUserId());
            newWish.setTitle(title);
            newWish.setDescription(description);
            newWish.setLink(link);
            newWish.setPriority(selectedPriority);
            newWish.setStatus(Status.NOT_OWNED);

            wishRepository.create(newWish);
            Toast.makeText(this, "Desejo cadastrado com sucesso", Toast.LENGTH_SHORT).show();
        }

        navigateToListWishes(v);
    }
}