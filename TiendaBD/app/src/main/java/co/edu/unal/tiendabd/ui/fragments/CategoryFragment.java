package co.edu.unal.tiendabd.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import co.edu.unal.tiendabd.R;
import co.edu.unal.tiendabd.ui.class_bd.Category;

import static android.app.Activity.RESULT_OK;

public class CategoryFragment extends Fragment implements View.OnClickListener {
    // Edit text of the category data
    private EditText mName, mId;
    // Button to load the image
    private Button btnUploadImage;
    // Button to load the data
    private FloatingActionButton fabtnAgregar;
    // Image view to load the images
    private ImageView imgUpload;
    final int GALLERY_INTENT = 1;
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();;
    Category category;
    Uri imgLocation;
    String uri;
    String name,id;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_category, container, false);
        // Edit text of the category data
        mName = (EditText) root.findViewById(R.id.etxtNameValueCategory);
        mId = (EditText) root.findViewById(R.id.etxtIdValueCategory);
        // Button to load the image
        btnUploadImage = (Button) root.findViewById(R.id.btnUploadCrudCategory);
        btnUploadImage.setOnClickListener(this::onClick);
        // Button to load the data
        fabtnAgregar = (FloatingActionButton) root.findViewById(R.id.addButtonCategory);
        fabtnAgregar.setOnClickListener(this::onClick);
        // Image view to load the images
        imgUpload = (ImageView) root.findViewById(R.id.imgCardViewUploadCategory);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        DatabaseReference myRef = database.getReference();
        category = new Category();
        return root;
    }

    // Method to charge images
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
                imgUpload.setImageURI(imgLocation);

                uploadImage();
            }
    }

    // Method to load the data
    private void uploadImage() {

            if (imgLocation != null) {
                StorageReference riversRef = mStorageRef.child("category/"+imgLocation.getLastPathSegment());

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
        id= mId.getText().toString();
         name = mName.getText().toString();
        String key=myRef.child("category").push().getKey();
        if(!name.isEmpty() && !id.isEmpty() && imgLocation!=null)
        {
           Category category =new Category(id,name,uri,key);


           myRef.child("category").child(key).setValue(category);

                mId.setText("");
                mName.setText("");
                imgUpload.setImageIcon(null);
                Toast.makeText(getActivity(), "The " + category.getName() + " category was added", Toast.LENGTH_LONG).show();

        }else
        {
            Toast.makeText(getActivity(), "Category could not be added", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUploadCrudCategory:
                chargeImage();

                break;
            case R.id.addButtonCategory:
                uploadData();
                break;


        }
    }


}