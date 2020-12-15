package co.edu.unal.tiendabd.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.edu.unal.tiendabd.R;
import co.edu.unal.tiendabd.ui.CRUD.CrudCategory;
import co.edu.unal.tiendabd.ui.adapters.AdapterCategory;
import co.edu.unal.tiendabd.ui.class_bd.Category;

public class ListCategories extends Fragment {
    public ListCategories()
    {

    }
    private TextView mName, mId;
    private ImageView imgList;
    private RecyclerView recyclerView;
    private AdapterCategory adapterCategory;

    ArrayList<Category> listCategory = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_products, container, false);
        DatabaseReference myRef = database.getReference();

        mName = (TextView) root.findViewById(R.id.txtNameValueCategory);
        mId = (TextView) root.findViewById(R.id.txtIdValueCategory);
        imgList = (ImageView) root.findViewById(R.id.imgCardViewCategory);


        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterCategory = new AdapterCategory(getActivity(), listCategory);
        recyclerView.setAdapter(adapterCategory);
        getDataCardview();
        ListCategory(myRef);

        return root;
    }

    private void getDataCardview() {
        adapterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category position = listCategory.get(recyclerView.getChildAdapterPosition(v));
                Intent i = new Intent(getActivity(), CrudCategory.class);
                i.putExtra("crudId",position.getId());
                i.putExtra("crudName",position.getName());
                i.putExtra("crudImg",position.getImageCategory());
                i.putExtra("key",position.getKey());
                startActivity(i);


            }
        });
    }

    private void ListCategory(DatabaseReference myRef) {
        myRef.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    listCategory.removeAll(listCategory);
                    for (DataSnapshot dateCategory : snapshot.getChildren()) {
                        Category category = dateCategory.getValue(Category.class);
                        listCategory.add(category);

                    }
                    adapterCategory.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}