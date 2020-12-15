package co.edu.unal.tiendabd.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


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

import java.util.ArrayList;

import co.edu.unal.tiendabd.MainActivity;
import co.edu.unal.tiendabd.R;
import co.edu.unal.tiendabd.ui.class_bd.Category;
import co.edu.unal.tiendabd.ui.class_bd.Product;

import static android.app.Activity.RESULT_OK;

public class ProductFragment extends Fragment implements View.OnClickListener {
    private EditText mName, mId, mAvailability;
    private Button btnUploadImage;
    private FloatingActionButton fabtnAgregar;
    private ImageView imgUpload;
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final int GALLERY_INTENT = 1;
    DatabaseReference myRef = database.getReference();
    Product mProduct;
    Uri imgLocation;
    String uri;
    String name, id;
    String availability;
    Spinner mSpinner;
  String seleccionSpinner;
    ArrayList<Category> listCategory=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_product, container, false);
        DatabaseReference myRef = database.getReference();
        mName = (EditText) root.findViewById(R.id.etxtNameValue);
        mId = (EditText) root.findViewById(R.id.etxtIdValue);
        mAvailability = (EditText) root.findViewById(R.id.etxtAvailabilityValue);
        btnUploadImage = (Button) root.findViewById(R.id.btnUploadImageProduct);
        btnUploadImage.setOnClickListener(this::onClick);
        fabtnAgregar = (FloatingActionButton) root.findViewById(R.id.addButtonProduct);
        fabtnAgregar.setOnClickListener(this::onClick);
        imgUpload = (ImageView) root.findViewById(R.id.imgCardViewUploadProduct);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mSpinner= (Spinner) root.findViewById(R.id.spinner);
        mProduct = new Product();

        spinner(myRef);
        return root;
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
                      ArrayAdapter<Category> adapter= new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,listCategory);
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

    // Method to charge images
    private void chargeImage() {
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
            imgUpload.setImageURI(imgLocation);

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


    // Method to load the data
    private void uploadData() {
        id = mId.getText().toString();
        name = mName.getText().toString();
        availability = mAvailability.getText().toString();

        String key=myRef.child("product").push().getKey();
        if (!name.isEmpty() && !id.isEmpty() && imgLocation != null) {

           Product mProduct = new Product(name,availability, id, seleccionSpinner, uri,key);
            myRef.child("product").child(key).setValue(mProduct);

            mId.setText("");
            mName.setText("");
            mAvailability.setText("");
            imgUpload.setImageIcon(null);
            Toast.makeText(getActivity(), "The " + mProduct.getName() + " product was added", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getActivity(), "Product could not be added", Toast.LENGTH_SHORT).show();
        }

    }
     @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUploadImageProduct:
                chargeImage();
                break;
            case R.id.addButtonProduct:
                uploadData();
                break;


        }
    }
}