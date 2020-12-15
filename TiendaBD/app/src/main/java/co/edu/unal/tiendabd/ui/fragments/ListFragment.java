package co.edu.unal.tiendabd.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import co.edu.unal.tiendabd.ui.CRUD.CrudProduct;
import co.edu.unal.tiendabd.ui.adapters.AdapterCategory;
import co.edu.unal.tiendabd.ui.adapters.AdapterProduct;
import co.edu.unal.tiendabd.ui.class_bd.Category;
import co.edu.unal.tiendabd.ui.class_bd.Product;

public class ListFragment extends Fragment {
    public ListFragment()
    {

    }
private TextView mCategory,mName,mId,mAvailability;
private ImageView imgList;
    private RecyclerView recyclerView;
    private AdapterProduct adapterProduct;
    ArrayList<Product> listProduct=new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_products, container, false);


        mName=(TextView) root.findViewById(R.id.txtNameValueProduct);
        mId=(TextView)root.findViewById(R.id.txtIdValueProduct);
        mAvailability=(TextView) root.findViewById(R.id.txtAvailabilityValueProduct);
        imgList=(ImageView) root.findViewById(R.id.imgCardViewProduct);

        recyclerView=(RecyclerView)root.findViewById(R.id.recycler_view);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterProduct=new AdapterProduct(getActivity(),listProduct);
        recyclerView.setAdapter(adapterProduct);
        getDataCardview();
        listProduct();
        return root;
    }
    private void getDataCardview() {
        adapterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product position = listProduct.get(recyclerView.getChildAdapterPosition(v));
                Intent i = new Intent(getActivity(), CrudProduct.class);
                i.putExtra("crudId",position.getId());
                i.putExtra("crudName",position.getName());
                i.putExtra("crudImg",position.getImageProduct());
                i.putExtra("key",position.getKey());
                i.putExtra("av",position.getAvailability());
                i.putExtra("spinner",position.getCategory());
                startActivity(i);


            }
        });
    }
    private void listProduct() {
        myRef.child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    listProduct.removeAll(listProduct);
                    for(DataSnapshot dateProduct:snapshot.getChildren())
                    {
                        Product product=dateProduct.getValue(Product.class);
                        listProduct.add(product);

                    }
                    adapterProduct.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}