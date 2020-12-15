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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;

import co.edu.unal.tiendabd.MainActivity;
import co.edu.unal.tiendabd.R;
import co.edu.unal.tiendabd.ui.class_bd.Category;
import co.edu.unal.tiendabd.ui.class_bd.Product;

public class CrudProduct extends AppCompatActivity implements View.OnClickListener {
    final int GALLERY_INTENT = 1;
    String recibirId, recibirName, recibirImg, recibirKey, recibirAv, recibirSpinner;
    private StorageReference mStorageRef;
    EditText name, id,ava;
    ImageView img;
    Context context;
    String uri;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Button updateCrudProduct;
    FloatingActionButton floatUpdateCrudProduct,floatDeleteCrudProduct;
    Spinner mSpinner;
    String seleccionSpinner;
    Uri imgLocation;
    String availability;
    Product mProduct;
    ArrayList<Category> listCategory=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_product);
        DatabaseReference myRef = database.getReference();
        recibirId = getIntent().getStringExtra("crudId");
        recibirName = getIntent().getStringExtra("crudName");
        recibirImg = getIntent().getStringExtra("crudImg");
        recibirKey = getIntent().getStringExtra("key");
        recibirAv = getIntent().getStringExtra("av");
        recibirSpinner = getIntent().getStringExtra("spinner");

        name = (EditText) findViewById(R.id.etxtCrudName);
        id = (EditText) findViewById(R.id.etxtCrudId);
        ava = (EditText) findViewById(R.id.etxtCrudAva);
        img = (ImageView) findViewById(R.id.imgCrudProduct);



        updateCrudProduct = (Button) findViewById(R.id.btnCrudProduct);
        updateCrudProduct.setOnClickListener(this::onClick);
        floatUpdateCrudProduct = (FloatingActionButton) findViewById(R.id.floatCrudProduct);
        floatUpdateCrudProduct.setOnClickListener(this::onClick);
        floatDeleteCrudProduct = (FloatingActionButton) findViewById(R.id.floatCrudProductDelete);
        floatDeleteCrudProduct.setOnClickListener(this::onClick);
        name.setText(recibirName);
        id.setText(recibirId);
        ava.setText(recibirAv);
        Picasso.with(context).load(recibirImg).into(img);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mSpinner= (Spinner) findViewById(R.id.spinnerCrud);
        mProduct = new Product();
        spinner(myRef);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCrudProduct:

                chargeImage();

                break;
            case R.id.floatCrudProduct:
                updateData();

                break;
            case R.id.floatCrudProductDelete:
                alertDialog();

                break;

        }
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You are sure to delete this product")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myRef.child("product").child(recibirKey).removeValue();
                        Toast.makeText(CrudProduct.this, "The "+recibirName+" product was eliminated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CrudProduct.this,MainActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(CrudProduct.this, MainActivity.class));
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
    }
    private void spinner(DatabaseReference myRef) {
        myRef.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    listCategory.removeAll(listCategory);
                    for(DataSnapshot dateCategory:snapshot.getChildren())
                    {
                        Category category=dateCategory.getValue(Category.class);
                        listCategory.add(category);


                    }
                    ArrayAdapter<Category> adapter= new ArrayAdapter(CrudProduct.this, android.R.layout.simple_list_item_1,listCategory);
                    mSpinner.setAdapter(adapter);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            seleccionSpinner=parent.getItemAtPosition(position).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateData() {
        String mName = name.getText().toString().trim();
        String mId = id.getText().toString().trim();
        availability = ava.getText().toString();
        if (!mName.isEmpty() && !mId.isEmpty()) {
            if (imgLocation != null) {
                Product product = new Product();

                product.setName(name.getText().toString().trim());
                product.setId(id.getText().toString().trim());
                product.setKey(recibirKey);
                product.setImageProduct(uri);
                product.setAvailability(availability);
                myRef.child("product").child(recibirKey).setValue(product);

                id.setText("");
                name.setText("");
                img.setImageIcon(null);
                Toast.makeText(CrudProduct.this, "The " + product.getName() + " product was updated", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CrudProduct.this, MainActivity.class));
            } else {
                Toast.makeText(CrudProduct.this, "You must upload the image", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(CrudProduct.this,"Enter all data", Toast.LENGTH_SHORT).show();
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
            StorageReference riversRef = mStorageRef.child("product/" + imgLocation.getLastPathSegment());

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