package com.leonardo.queroja;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
import com.leonardo.queroja.enums.Status;
import com.leonardo.queroja.global.UserSession;
import com.leonardo.queroja.repositories.WishRepository;

import java.util.List;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;

public class ListWishesActivity extends AppCompatActivity {

    private WishRepository wishRepository;
    private RecyclerView recyclerView;
    private WishAdapter wishAdapter;
    private List<WishEntity> wishes;
    private UserEntity user;
    private TabLayout tabLayout;
    private TextView emptyListMessage;

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
        user = UserSession.getInstance(this).getUser();

        tabLayout = findViewById(R.id.tab_layout);
        recyclerView = findViewById(R.id.wishlist_recycler);
        emptyListMessage = findViewById(R.id.empty_list_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tabLayout.addTab(tabLayout.newTab().setText(Status.NOT_OWNED.getDescription()));
        tabLayout.addTab(tabLayout.newTab().setText(Status.OWNED.getDescription()));

        Status selectedStatusTab = Status.NOT_OWNED;
        if (getIntent().hasExtra("selected_status")) {
            int statusCode = getIntent().getIntExtra("selected_status", Status.NOT_OWNED.getCode());
            selectedStatusTab = Status.fromCode(statusCode);
        }

        tabLayout.getTabAt(selectedStatusTab.getCode()).select();
        loadWishesByStatus(selectedStatusTab);

        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                int position = tab.getPosition();
                loadWishesByStatus(Status.fromCode(position));
            }

            @Override
            public void onTabUnselected(Tab tab) { }

            @Override
            public void onTabReselected(Tab tab) { }
        });
    }

    private void loadWishesByStatus(Status status) {
        wishes = wishRepository.findByUserIdAndStatus(user.getUserId(), status);

        if (wishes.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListMessage.setVisibility(View.GONE);
        }

        if (wishAdapter == null) {
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
                                    if (wishes.isEmpty()) {
                                        recyclerView.setVisibility(View.GONE);
                                        emptyListMessage.setVisibility(View.VISIBLE);
                                    }
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

                @Override
                public void onCheckClick(WishEntity wish) {
                    new AlertDialog.Builder(ListWishesActivity.this)
                            .setTitle("Confirmar ação")
                            .setMessage(wish.getStatus() == Status.OWNED ?
                                    "Deseja marcar este desejo como não realizado?" :
                                    "Deseja marcar este desejo como realizado?")
                            .setPositiveButton("Sim", (dialog, which) -> {
                                int position = wishes.indexOf(wish);
                                if (position != -1) {
                                    Status newStatus = wish.getStatus() == Status.OWNED ? Status.NOT_OWNED : Status.OWNED;
                                    wish.setStatus(newStatus);
                                    wishRepository.update(wish);
                                    wishes.remove(position);
                                    wishAdapter.notifyItemRemoved(position);
                                    Toast.makeText(ListWishesActivity.this,
                                            newStatus == Status.OWNED ? "Desejo marcado como realizado" : "Desejo marcado como não realizado",
                                            Toast.LENGTH_SHORT).show();
                                    tabLayout.getTabAt(newStatus.getCode()).select();
                                    if (wishes.isEmpty()) {
                                        recyclerView.setVisibility(View.GONE);
                                        emptyListMessage.setVisibility(View.VISIBLE);
                                    }
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                }
            });
            recyclerView.setAdapter(wishAdapter);
        } else {
            wishAdapter.updateData(wishes);
        }
    }

    public void navigateToWishForm(View v) {
        Intent intent = new Intent(this, FormWishActivity.class);
        startActivity(intent);
    }

    public void shareWishlist(View v) {
        StringBuilder wishlistText = new StringBuilder("Meus Desejos:\n\n");
        for (WishEntity wish : wishes) {
            wishlistText.append(wish.getTitle())
                        .append(" - ")
                        .append(wish.getPriority().getDescription())
                        .append("\n");
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, wishlistText.toString());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}