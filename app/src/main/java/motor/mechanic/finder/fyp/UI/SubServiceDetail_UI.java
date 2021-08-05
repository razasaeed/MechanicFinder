package motor.mechanic.finder.fyp.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import motor.mechanic.finder.fyp.Adapters.ServiceProductsAdapter;
import motor.mechanic.finder.fyp.Adapters.SubServiceProductsAdapter;
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

public class SubServiceDetail_UI extends AppCompatActivity {

    TextView headingTxt;
    RecyclerView subProductsRV;
    DatabaseReference reference;
    List<ProductsDataModel> productsData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub_service_ui);

        headingTxt = findViewById(R.id.headingTxt);
        headingTxt.setText("Select Oil Filter");
        subProductsRV = findViewById(R.id.subProductsRV);
        subProductsRV.setLayoutManager(new GridLayoutManager(this, 2));

        ProductsDataModel dataModel = new ProductsDataModel("1", "OEM", "https://5.imimg.com/data5/TH/KQ/MY-9404943/oem-filters-500x500.png", "750");
        productsData.add(dataModel);
        ProductsDataModel dataModel1 = new ProductsDataModel("1", "LOCAL", "https://i.ebayimg.com/images/g/eqoAAOSw1~JZR9kA/s-l300.png", "290");
        productsData.add(dataModel1);

        SubServiceProductsAdapter serviceProductsAdapter = new SubServiceProductsAdapter(SubServiceDetail_UI.this, productsData);
        subProductsRV.setAdapter(serviceProductsAdapter);

    }
}
