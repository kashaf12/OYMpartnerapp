package www.kfstudio.com.oympartner;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class BookerAdapter extends FirestoreRecyclerAdapter<Booking ,BookerAdapter.BookerHolder> {
    private OnItemClickListener listener;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Uri us_profile_image_url;
    private String ph_phone;
    private Context mContext;
    View v;
    private int all;
    public BookerAdapter(Context mContext,@NonNull FirestoreRecyclerOptions<Booking> options, String ph_phone) {
        super(options);
        this.mContext=mContext;
        this.ph_phone = ph_phone;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookerHolder bookerHolder, int i, @NonNull Booking booking) {
    if(ph_phone.equals(booking.getBooking_for())){
        all=convertDpToPx(mContext,8);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(all,all,all,all);
        downloadimage(booking.getBooker_phone(),bookerHolder);
        bookerHolder.itemView.setVisibility(View.VISIBLE);
        bookerHolder.itemView.setLayoutParams(layoutParams);
     bookerHolder.name.setText(booking.getBooker_name());
     bookerHolder.email.setText(booking.getBooker_email());
     bookerHolder.phone.setText(booking.getBooker_phone());
     bookerHolder.time.setText(booking.getBooking_time());
     if(booking.getBooking_done()){
         bookerHolder.end.setText("DONE");
         bookerHolder.end.setClickable(false);
     }else{
         bookerHolder.end.setText("END");
         bookerHolder.end.setClickable(true);
     }
    }else{

        bookerHolder.itemView.setVisibility(View.GONE);
        bookerHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
    }
    }

    @NonNull
    @Override
    public BookerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookerHolder(v);
    }

    class BookerHolder extends RecyclerView.ViewHolder{
    TextView name,email,phone,time;
    CircularImageView profile_image;
    TextView end;
    CardView cardview;
    public BookerHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.name);
        email=itemView.findViewById(R.id.email);
        phone=itemView.findViewById(R.id.phone);
        time=itemView.findViewById(R.id.time);
        end=itemView.findViewById(R.id.end);
        cardview=itemView.findViewById(R.id.cardview);
        profile_image=itemView.findViewById(R.id.profile_image1);
        end.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        int position =getAdapterPosition();
        if(position != RecyclerView.NO_POSITION && listener!=null){
            listener.onItemClick(getSnapshots().getSnapshot(position),position);
    }
    }
});

    }
}
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener ){
        this.listener=listener;

    }
    private void downloadimage(String phone, final BookerHolder bookerHolder){

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://oym-1e52d.appspot.com/"+phone+"/");
        storageReference.child("profile_picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(mContext.getApplicationContext()).load(uri)
                        .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                        .apply(RequestOptions.centerCropTransform())
                        .into(bookerHolder.profile_image);
            }
        });

    }
    public int convertDpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
