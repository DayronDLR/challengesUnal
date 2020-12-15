package co.edu.unal.tiendabd.ui.CRUD;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import co.edu.unal.tiendabd.MainActivity;
import co.edu.unal.tiendabd.R;
import co.edu.unal.tiendabd.ui.class_bd.Category;
import co.edu.unal.tiendabd.ui.fragments.CategoryFragment;
import co.edu.unal.tiendabd.ui.fragments.ListCategories;

public class CrudCategory extends AppCompatActivity implements View.OnClickListener {
    final int GALLERY_INTENT = 1;
    String recibirId, recibirName, recibirImg, recibirKey;
    private StorageReference mStorageRef;
    EditText name, id;
    ImageView img;
    Context context;
    String uri;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Button updateCrudCategory;
    FloatingActionButton floatUpdateCrudCategory,floatDeleteCrudCategory;

    Uri imgLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_category);
        DatabaseReference myRef = database.getReference();
        recibirId = getIntent().getStringExtra("crudId");
        recibirName = getIntent().getStringExtra("crudName");
        recibirImg = getIntent().getStringExtra("crudImg");
        recibirKey = getIntent().getStringExtra("key");
        name = (EditText) findViewById(R.id.etxtNameCrudCategory);
        id = (EditText) findViewById(R.id.etxtIdCrudCategory);
        img = (ImageView) findViewById(R.id.imgCrudCategory);
        updateCrudCategory = (Button) findViewById(R.id.btnUpdateCrudCategory);
        updateCrudCategory.setOnClickListener(this::onClick);
        floatUpdateCrudCategory = (FloatingActionButton) findViewById(R.id.floatUpdateCrudCategory);
        floatUpdateCrudCategory.setOnClickListener(this::onClick);
        floatDeleteCrudCategory = (FloatingActionButton) findViewById(R.id.floatCrudCategoryDelete);
        floatDeleteCrudCategory.setOnClickListener(this::onClick);

        name.setText(recibirName);
        id.setText(recibirId);
        Picasso.with(context).load(recibirImg).into(img);
        mStorageRef = FirebaseStorage.getInstance().getReference();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateCrudCategory:

                chargeImage();

                break;
            case R.id.floatUpdateCrudCategory:
                updateData();

                break;
            case R.id.floatCrudCategoryDelete:
                alertDialog();

            break;

        }
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You are sure to delete this category")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myRef.child("category").child(recibirKey).removeValue();
                        Toast.makeText(CrudCategory.this, "The " + recibirName + " category was eliminated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CrudCategory.this, MainActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(CrudCategory.this, MainActivity.class));
                    }
                });
        // Create the AlertDialog object and return it
       builder.show();
    }

    private void updateData() {
        String mName = name.getText().toString().trim();
        String mId = id.getText().toString().trim();
        if (!mName.isEmpty() && !mId.isEmpty()) {
            if (imgLocation != null) {
                Category category = new Category();

                category.setName(name.getText().toString().trim());
                category.setId(id.getText().toString().trim());
                category.setKey(recibirKey);
                category.setImageCategory(uri);
                myRef.child("category").child(recibirKey).setValue(category);

                id.setText("");
                name.setText("");
                img.setImageIcon(null);
                Toast.makeText(CrudCategory.this, "The " + category.getName() + " category was updated", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CrudCategory.this, MainActivity.class));
            } else {
                Toast.makeText(CrudCategory.this, "You must upload the image", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(CrudCategory.this, "Enter all data", Toast.LENGTH_SHORT).show();
        }
    }

    public void chargeImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    // recibe el resultado del metodo chargeImagen
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            imgLocation = data.getData();
            img.setImageURI(imgLocation);

            uploadImage();
        }
    }

    // Method to load the data
    private void uploadImage() {

        if (imgLocation != null) {
            StorageReference riversRef = mStorageRef.child("category/" + imgLocation.getLastPathSegment());

            UploadTask uploadTask = riversRef.putFile(imgLocation);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        uri = task.getResult().toString();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }

    }
}