package com.leonardo.queroja;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leonardo.queroja.adapters.WishAdapter;
import com.leonardo.queroja.entities.UserEntity;
import com.leonardo.queroja.entities.WishEntity;
import com.leonardo.queroja.global.UserSession;
import com.leonardo.queroja.repositories.WishRepository;

import java.util.List;

public class ListWishesActivity extends AppCompatActivity {

    private WishRepository wishRepository;
    private RecyclerView recyclerView;
    private WishAdapter wishAdapter;
    private List<WishEntity> wishes;

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

        wishRepository = new WishRepository(this);
        UserEntity user = UserSession.getInstance(this).getUser();
        wishes = wishRepository.findByUserId(user.getUserId());

        recyclerView = findViewById(R.id.wishlist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        wishAdapter = new WishAdapter(wishes, new WishAdapter.OnWishClickListener() {
            @Override
            public void onDeleteClick(WishEntity wish) {
                new AlertDialog.Builder(ListWishesActivity.this)
                        .setTitle("Confirmar exclusão")
                        .setMessage("Tem certeza que deseja remover este desejo?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            int position = wishes.indexOf(wish);
                            if (position != -1) {
                                wishRepository.delete(wish);
                                wishes.remove(position);
                                wishAdapter.notifyItemRemoved(position);
                                Toast.makeText(ListWishesActivity.this, "Desejo removido com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }

            @Override
            public void onLinkClick(String url) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }

            @Override
            public void onItemClick(WishEntity wish) {
                Intent intent = new Intent(ListWishesActivity.this, FormWishActivity.class);
                intent.putExtra("wish_id", wish.getWishId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(wishAdapter);
    }

    public void navigateToWishForm(View v) {
        Intent intent = new Intent(this, FormWishActivity.class);
        startActivity(intent);
    }
}
