package www.kfstudio.com.oympartner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Home extends AppCompatActivity {
    BottomAppBar bottomAppBar;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;

    DocumentReference documentReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference dbr = db.collection("Booking");
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String us_name="Loading";
    String us_email="Loading";
    String us_location ="Loading";
    String us_experience="Loading";
    String us_rating="Loading";
    String us_website="Loading";
    String us_description="Loading";
    String us_speciality="Loading";
    String us_rate="Loading";
    String us_no_of_pic="Loading";
    String us_extra="Loading";
    String us_likes = "Loading";
    Uri us_profile_image_url,us_image_1,us_image_2,us_image_3,us_image_4;
    private BookerAdapter bookerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bottomAppBar=findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database(currentUser.getPhoneNumber());
        if (bundle != null) {
            us_profile_image_url = Uri.fromFile(new File(bundle.getString("image")));
            us_image_1 = Uri.fromFile(new File(bundle.getString("image1")));
            us_image_2 = Uri.fromFile(new File(bundle.getString("image2")));
            us_image_3 = Uri.fromFile(new File(bundle.getString("image3")));
            us_image_4 = Uri.fromFile(new File(bundle.getString("image4")));
        }else{
            us_profile_image_url = Uri.parse(getURLForResource(R.drawable.blank_profile_picture));
            us_image_1 = Uri.parse(getURLForResource(R.drawable.blank_profile_picture));
            us_image_2 = Uri.parse(getURLForResource(R.drawable.blank_profile_picture));
            us_image_3 = Uri.parse(getURLForResource(R.drawable.blank_profile_picture));
            us_image_4 = Uri.parse(getURLForResource(R.drawable.blank_profile_picture));

            downloadimage(currentUser.getPhoneNumber());
        }
        setUpRecyclerView();
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(us_name.equals("Loading")){
//                    downloadimage(currentUser.getPhoneNumber());
                    database(currentUser.getPhoneNumber());
                }
                Intent intent = new Intent(Home.this, Register_Activity.class);
                Bundle extras = new Bundle();
                extras.putString("Name", us_name);
                extras.putString("Email", us_email);
                extras.putString("Experience", us_experience);
                extras.putString("Rate", us_rate);
                extras.putString("Rating", us_rating);
                extras.putString("Description", us_description);
                extras.putString("Location", us_location);
                extras.putString("Website", us_website);
                extras.putString("NoPic", us_no_of_pic);
                extras.putString("Speciality", us_speciality);
                extras.putString("Extra", us_extra);
                extras.putString("Image", us_profile_image_url.toString());
                extras.putString("Image1", us_image_1.toString());
                extras.putString("Image2", us_image_2.toString());
                extras.putString("Image3", us_image_3.toString());
                extras.putString("Image4", us_image_4.toString());
                extras.putString("phoneNumber", currentUser.getPhoneNumber());
                extras.putBoolean("Main",true);
                intent.putExtras(extras);
                startActivity(intent);

                return false;
            }

        });
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open bottom sheet
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance(us_name,currentUser.getPhoneNumber(),us_email,us_profile_image_url.toString(),us_likes);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });
    }
    private  void database(final String phone){

        documentReference = db.collection("Photographer")
                .document(phone);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    us_name = documentSnapshot.getString("ph_name");
                    us_email = documentSnapshot.getString("ph_email");
                    us_location = documentSnapshot.getString("ph_location");
                    us_experience = documentSnapshot.getString("ph_experience");
                    us_rate = documentSnapshot.getString("ph_rate");
                    us_rating = documentSnapshot.getString("ph_rating");
                    us_speciality = documentSnapshot.getString("ph_speciality");
                    us_extra = documentSnapshot.getString("ph_extra");
                    us_description = documentSnapshot.getString("ph_description");
                    us_website = documentSnapshot.getString("ph_website");
                    us_no_of_pic = documentSnapshot.getString("ph_no_pic");
                    us_likes=documentSnapshot.getString("ph_likes");



                }
            }
        });

    }
    private void downloadimage(String phone){
        storageReference = storage.getReferenceFromUrl
                ("gs://oym-1e52d.appspot.com/"
                        + phone + "/photographer");
        storageReference.child("ph_profile_image_url").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                us_profile_image_url = uri;
            }
        });
        storageReference.child("ph_image_1").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                us_image_1=uri;
            }
        });
        storageReference.child("ph_image_2").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                us_image_2=uri;
            }
        });
        storageReference.child("ph_image_3").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                us_image_3=uri;
            }
        });
        storageReference.child("ph_image_4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                us_image_4=uri;
            }
        });

    }
    public String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottomappbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void setUpRecyclerView() {
        Query query = dbr.orderBy("booking_time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Booking> options = new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query, Booking.class).build();
        bookerAdapter = new BookerAdapter(options,currentUser.getPhoneNumber());
        recyclerView = findViewById(R.id.recyclerv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookerAdapter);
        bookerAdapter.setOnItemClickListener(new BookerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Toast.makeText(Home.this, "Booking has been done!!", Toast.LENGTH_SHORT).show();
                db.collection("Booking").document(documentSnapshot.getString("booking_time")).update("booking_done",true);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        bookerAdapter.startListening();
    }
    @Override
    protected void onStart(){
        super.onStart();
        bookerAdapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        bookerAdapter.stopListening();
    }
    @Override
    protected void onPause(){
        super.onPause();
        bookerAdapter.stopListening();
    }
}
