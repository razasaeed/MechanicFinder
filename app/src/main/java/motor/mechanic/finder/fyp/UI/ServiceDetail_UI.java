package motor.mechanic.finder.fyp.UI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import motor.mechanic.finder.fyp.Adapters.ServiceProductsAdapter;
import motor.mechanic.finder.fyp.DataModels.ProductsDataModel;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.ProductsSharedClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetail_UI extends AppCompatActivity {

    RecyclerView productsRV;
    DatabaseReference reference;
    List<ProductsDataModel> productsData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail__ui);

        reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Products").child(ProductsSharedClass.product_type);
        productsRV = findViewById(R.id.productsRV);
        productsRV.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    protected void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (productsData.size() > 0) {
                    productsData.clear();
                }
                for (DataSnapshot products : dataSnapshot.getChildren()) {
                    ProductsDataModel productsDataModel = products.getValue(ProductsDataModel.class);
                    productsData.add(productsDataModel);
                }
                ServiceProductsAdapter serviceProductsAdapter = new ServiceProductsAdapter(ServiceDetail_UI.this, productsData);
                productsRV.setAdapter(serviceProductsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
