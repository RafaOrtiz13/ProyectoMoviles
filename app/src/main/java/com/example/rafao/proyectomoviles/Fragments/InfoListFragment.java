package com.example.rafao.proyectomoviles.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafao.proyectomoviles.Camera;
import com.example.rafao.proyectomoviles.Models.Productos;
import com.example.rafao.proyectomoviles.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InfoListFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference root;
    private ArrayList<Productos> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        root = database.getReference("/Productos");

        Bundle data = this.getArguments();
        int codigo = data.getInt("codigo");

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    Productos product = item.getValue(Productos.class);
                    if(product.Dependencia == codigo)
                        list.add(product);
                }

                RecyclerView recyclerView = getActivity().findViewById (R.id.recview);
                recyclerView.setLayoutManager (new LinearLayoutManager(getContext (),  LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter (new InfoListAdapter(getContext (), list));

                Log.i("Prueba",""+codigo+" "+list.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        root.addValueEventListener(event);
    }
}

class InfoListAdapter extends RecyclerView.Adapter<InfoListViewHolder>{

    private Context context;
    private ArrayList<Productos> lista;

    public InfoListAdapter(Context context, ArrayList<Productos> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public InfoListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from (context).inflate (R.layout.recycler_list_item, viewGroup, false);
        return new InfoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoListViewHolder infoListViewHolder, int i) {
        infoListViewHolder.bind(lista.get(i));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

class InfoListViewHolder extends RecyclerView.ViewHolder{

    TextView text;

    public InfoListViewHolder(@NonNull View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.textView);
    }

    public void bind(Productos productos) {
        text.setText(productos.Descripcion);
        text.setOnClickListener(v -> {
            Intent intent = new Intent(itemView.getContext(),Camera.class);
            intent.putExtra("Codigo",productos.Codigo);
            itemView.getContext().startActivity(intent);
        });
    }
}
