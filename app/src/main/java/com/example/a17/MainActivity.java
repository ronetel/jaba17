package com.example.a17;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private EditText titleText, authorText;
    private Button addButton, updateButton, deleteButton, choosePhotoButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String selectedProductTitle;
    private Uri photoUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        titleText = findViewById(R.id.titleText);
        authorText = findViewById(R.id.authorText);
        addButton = findViewById(R.id.addButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        choosePhotoButton = findViewById(R.id.choosePhotoButton);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getProductsTitles());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProductTitle = adapter.getItem(position);
                Product product = Paper.book().read(selectedProductTitle, null);
                if (product != null) {
                    titleText.setText(product.getTitle());
                    authorText.setText(product.getAuthor());
                    photoUri = Uri.parse(product.getPhotoPath());
                }
            }
        });

        choosePhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        addButton.setOnClickListener(v -> {
            String title = titleText.getText().toString();
            String author = authorText.getText().toString();
            if (!title.isEmpty() && !author.isEmpty() && photoUri != null) {
                Product product = new Product(title, title, author, "", photoUri.toString());
                Paper.book().write(title, product);
                updateProductList();
                clearInputs();
            }
        });

        updateButton.setOnClickListener(v -> {
            if (selectedProductTitle == null) {
                Toast.makeText(MainActivity.this, "Пожалуйста, сначала выберите товар", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = titleText.getText().toString();
            String author = authorText.getText().toString();
            if (!title.isEmpty() && !author.isEmpty() && photoUri != null) {
                Paper.book().delete(selectedProductTitle);
                Product updatedProduct = new Product(title, title, author, "", photoUri.toString());
                Paper.book().write(selectedProductTitle, updatedProduct);
                updateProductList();
                clearInputs();
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (selectedProductTitle == null) {
                Toast.makeText(MainActivity.this, "Пожалуйста, сначала выберите товар", Toast.LENGTH_SHORT).show();
                return;
            }

            Paper.book().delete(selectedProductTitle);
            updateProductList();
            clearInputs();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photoUri = data.getData();
        }
    }

    private void updateProductList() {
        adapter.clear();
        adapter.addAll(getProductsTitles());
        adapter.notifyDataSetChanged();
    }

    private List<String> getProductsTitles() {
        return new ArrayList<>(Paper.book().getAllKeys());
    }

    private void clearInputs() {
        titleText.setText("");
        authorText.setText("");
        photoUri = null;
        selectedProductTitle = null;
    }
}
