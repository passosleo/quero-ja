package com.leonardo.queroja;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.leonardo.queroja.entities.UserEntity;
import com.leonardo.queroja.global.UserSession;

public class ListWishesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_wishes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        UserEntity user = UserSession.getInstance(this).getUser();

        Log.d("ListWishesActivity", "User: " + user.getEmail());
    }

    public void navigateToWishForm(View v) {
        Intent intent = new Intent(this, FormWishActivity.class);
        startActivity(intent);
    }
}