package www.kfstudio.com.oympartner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Register_Activity extends AppCompatActivity {
    private File actualImage;
    private File compressedImage;
    private File compressedImage1 = null;
    private File compressedImage2 = null;
    private File compressedImage3 = null;
    private File compressedImage4 = null;
    private File compressedImage5 = null;
    Uri downloadUri;
    Uri uriTask1,uriTask2,uriTask3,uriTask_profile,uriTask4;
    Map<String,Object> photo;
    private FirebaseStorage storage;
    int calling;
    private StorageReference storageReference;
    Map<String, Object> user;
    FirebaseFirestore db;
    private static final String MyPREFERENCES = "MyPrefs";
    private static final String Phone = "phoneKey";
    private SharedPreferences sharedpreferences;
    private int GALLERY = 1;
    private CircularImageView ph_imageView;
    private ImageView ph_image1, ph_image2, ph_image3, ph_image4;
    private EditText ph_name, ph_email, ph_location, ph_experience, ph_description, ph_website,
    ph_rating,ph_speciality,ph_rate,ph_no_of_pic,ph_extra;
    String ph_name_string, ph_email_string, ph_phone_string,
            ph_location_string, ph_experience_string, ph_rating_string,
            ph_website_string, ph_description_string
            ,ph_speciality_string,ph_rate_string,ph_no_of_pic_string,ph_extra_string;
    private TextView ph_phone;
    private Button register;
    private String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        db = FirebaseFirestore.getInstance();
        ph_name = findViewById(R.id.ph_name);
        ph_email = findViewById(R.id.ph_email);
        ph_phone = findViewById(R.id.ph_phone);
        ph_location = findViewById(R.id.ph_location);
        ph_experience = findViewById(R.id.ph_experience);
        ph_description = findViewById(R.id.ph_description);
        ph_website = findViewById(R.id.ph_website);
        ph_rating = findViewById(R.id.ph_rating);
        ph_speciality = findViewById(R.id.ph_speciality);
        ph_rate = findViewById(R.id.ph_rate);
        ph_no_of_pic = findViewById(R.id.ph_number_of_pics);
        ph_extra = findViewById(R.id.ph_extra_pics);
        register = findViewById(R.id.ph_register_upload);
        InputFilter[] textfilters = new InputFilter[1];
        textfilters[0] = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    char[] acceptedChars = new char[]{' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.'};

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }

        };
        ph_name.setFilters(textfilters);
        ph_imageView = findViewById(R.id.ph_profile_image);
        ph_image1 = findViewById(R.id.ph_image_1);
        ph_image2 = findViewById(R.id.ph_image_2);
        ph_image3 = findViewById(R.id.ph_image_3);
        ph_image4 = findViewById(R.id.ph_image_4);
        storage = FirebaseStorage.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        ph_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calling = 1;
                choosePhotoFromGallary();
            }
        });
        ph_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calling = 2;
                choosePhotoFromGallary();
            }
        });
        ph_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calling = 3;
                choosePhotoFromGallary();
            }
        });
        ph_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calling = 4;
                choosePhotoFromGallary();
            }
        });
        ph_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calling = 5;
                choosePhotoFromGallary();
            }
        });
        if (bundle != null) {
            String phoneNumber = (String) bundle.get("phoneNumber");
            Boolean main = (Boolean) bundle.getBoolean("Main");
            ph_phone.setFocusable(false);
            ph_phone.setEnabled(false);
            ph_phone.setCursorVisible(false);
            ph_phone.setKeyListener(null);
            ph_phone.setText(phoneNumber);
            ph_phone.setTextColor(Color.WHITE);
            editor.putString(Phone, phoneNumber);
            editor.apply();
            if (main) {
                ph_name.setText(bundle.getString("Name"));
                ph_email.setText(bundle.getString("Email"));
                ph_website.setText(bundle.getString("Website"));
                ph_extra.setText(bundle.getString("Extra"));
                ph_speciality.setText(bundle.getString("Speciality"));
                ph_rate.setText(bundle.getString("Rate"));
                ph_rating.setText(bundle.getString("Rating"));
                ph_description.setText(bundle.getString("Description"));
                ph_no_of_pic.setText(bundle.getString("NoPic"));
                ph_experience.setText(bundle.getString("Experience"));
                ph_location.setText(bundle.getString("Location"));
                Glide.with(this)
                        .load(Uri.parse(bundle.getString("Image")))
                        .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                        .apply(RequestOptions.centerCropTransform())
                        .into(ph_imageView);
                Glide.with(this)
                        .load(Uri.parse(bundle.getString("Image1")))
                        .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                        .apply(RequestOptions.centerCropTransform())
                        .into(ph_image1);
                Glide.with(this)
                        .load(Uri.parse(bundle.getString("Image2")))
                        .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                        .apply(RequestOptions.centerCropTransform())
                        .into(ph_image2);
                Glide.with(this)
                        .load(Uri.parse(bundle.getString("Image3")))
                        .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                        .apply(RequestOptions.centerCropTransform())
                        .into(ph_image3);
                Glide.with(this)
                        .load(Uri.parse(bundle.getString("Image4")))
                        .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                        .apply(RequestOptions.centerCropTransform())
                        .into(ph_image4);

                register.setText("Save");

            }
        }
        phone = sharedpreferences.getString(Phone, null);
        if (phone == null) {
            sendToAuth();
        } else {
            ph_phone.setFocusable(false);
            ph_phone.setEnabled(false);
            ph_phone.setCursorVisible(false);
            ph_phone.setKeyListener(null);
            ph_phone.setText(phone);
            ph_phone.setTextColor(Color.BLACK);
        }

//        register.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View v) {
//                if (validateForm()) {
//                    register.setText("Sending .... ");
//                    register.setClickable(false);
//                    String Name = ph_name.getText().toString().trim();
//                    String Email = ph_email.getText().toString().trim();
//                    user = new HashMap<>();
//                    user.put("us_name", Name);
//                    user.put("us_email", Email);
//                    db.collection("OYM").document("Users")
//                            .collection(phone).document("ProfileInformation")
//                            .set(user);
//                    uploadImage();
//
//                } else {
//                    Toast.makeText(Register_Activity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ph_name_string = ph_name.getText().toString().trim();
                ph_email_string = ph_email.getText().toString().trim();
                ph_experience_string = ph_experience.getText().toString().trim() + " yrs";
                ph_phone_string = ph_phone.getText().toString().trim();
                ph_location_string = ph_location.getText().toString().trim();
                ph_rating_string = ph_rating.getText().toString().trim()+ "/5";
                ph_description_string = ph_description.getText().toString().trim();
                ph_website_string = ph_website.getText().toString().trim();
                ph_speciality_string = ph_speciality.getText().toString().trim();
                ph_rate_string = "₹"+ph_rate.getText().toString().trim()+"/hr";
                ph_extra_string = "₹"+ph_extra.getText().toString().trim()+"/pic (Softcopy)";
                ph_no_of_pic_string = ph_no_of_pic.getText().toString().trim()+" pics (softcopy)";

                if (!(TextUtils.isEmpty(ph_name_string) ||
                        TextUtils.isEmpty(ph_email_string) ||
                        TextUtils.isEmpty(ph_experience_string) ||
                        TextUtils.isEmpty(ph_phone_string) ||
                        TextUtils.isEmpty(ph_location_string) ||
                        TextUtils.isEmpty(ph_rating_string) ||
                        TextUtils.isEmpty(ph_description_string) ||
                        TextUtils.isEmpty(ph_website_string) ||
                        TextUtils.isEmpty(ph_speciality_string) ||
                        TextUtils.isEmpty(ph_rate_string) ||
                        TextUtils.isEmpty(ph_extra_string) ||
                        TextUtils.isEmpty(ph_no_of_pic_string) ||
                        compressedImage1 == null ||
                        compressedImage2 == null ||
                        compressedImage3 == null ||
                        compressedImage4 == null ||
                        compressedImage5 == null)) {
                    if (compressedImage == null) {
                        new Compressor(getApplicationContext())
                                .setMaxWidth(60)
                                .setMaxHeight(60)
                                .setQuality(50)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .compressToFileAsFlowable(new File(getURLForResource(R.drawable.blank_profile_picture)))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<File>() {
                                    @Override
                                    public void accept(File file) {
                                        compressedImage = file;
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) {
                                        throwable.printStackTrace();
                                        showError(throwable.getMessage());
                                    }
                                });
                    }

                    uploadPoll(ph_name_string,
                            ph_phone_string, ph_email_string, ph_experience_string,
                            ph_location_string, ph_rating_string,
                            ph_website_string, ph_description_string,ph_speciality_string,
                            ph_rate_string,ph_extra_string,ph_no_of_pic_string);
                    uploadImage("ph_profile_image_url");
                    uploadImage("ph_image_1");
                    uploadImage("ph_image_2");
                    uploadImage("ph_image_3");
                    uploadImage("ph_image_4");
                } else {
                    Toast.makeText(Register_Activity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void uploadImage(final String name) {
        switch (name) {
            case "ph_profile_image_url":
                if (compressedImage1 != null) {
                    compressedImage = compressedImage1;
                }
                break;
            case "ph_image_1":
                if (compressedImage2 != null) {
                    compressedImage = compressedImage2;
                }

                break;
            case "ph_image_2":
                if (compressedImage3 != null) {
                    compressedImage = compressedImage3;
                }

                break;
            case "ph_image_3":
                if (compressedImage4 != null) {
                    compressedImage = compressedImage4;
                }

                break;
            case "ph_image_4":
                if (compressedImage5 != null) {
                    compressedImage = compressedImage5;
                }

                break;
        }
        if (!name.isEmpty()) {
            storageReference = storage.getReferenceFromUrl
                    ("gs://oym-1e52d.appspot.com/"
                            + phone + "/photographer");
            final StorageReference ref = storageReference.child(name);
            if (compressedImage != null) {
                ref.putFile(Uri.fromFile(compressedImage)).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadUri = task.getResult();
                            photo = new HashMap<>();
                            switch (name) {
                                case "ph_profile_image_url":
                                    uriTask_profile = downloadUri;
                                    photo.put("ph_profile_image_url", uriTask_profile.toString());
                                    db.collection("Photographer")
                                            .document(phone).update(photo);
                                    break;
                                case "ph_image_1":
                                    uriTask1 = downloadUri;
                                    photo.put("ph_photo", FieldValue.arrayUnion(uriTask1.toString()));
                                    db.collection("Photographer")
                                            .document(phone).update(photo);
                                    break;
                                case "ph_image_2":
                                    uriTask2 = downloadUri;
                                    photo.put("ph_photo", FieldValue.arrayUnion(uriTask2.toString()));
                                    db.collection("Photographer")
                                            .document(phone).update(photo);
                                    break;
                                case "ph_image_3":
                                    uriTask3 = downloadUri;
                                    photo.put("ph_photo", FieldValue.arrayUnion(uriTask3.toString()));
                                    db.collection("Photographer")
                                            .document(phone).update(photo);
                                    break;
                                case "ph_image_4":
                                    uriTask4 = downloadUri;
                                    photo.put("ph_photo", FieldValue.arrayUnion(uriTask4.toString()));
                                    db.collection("Photographer")
                                            .document(phone).update(photo);
                                    break;
                            }
                        }
                    }
                });
            }
        }
        String path = compressedImage1.getAbsolutePath();
        String path1 = compressedImage2.getAbsolutePath();
        String path2 = compressedImage3.getAbsolutePath();
        String path3 = compressedImage4.getAbsolutePath();
        String path4 = compressedImage5.getAbsolutePath();
        Intent intent = new Intent(Register_Activity.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("image",path);
        intent.putExtra("image1",path1);
        intent.putExtra("image2",path2);
        intent.putExtra("image3",path3);
        intent.putExtra("image4",path4);

        startActivity(intent);
        finish();
    }

    private void sendToAuth() {
        sharedpreferences.edit().clear().apply();
        Intent intent = new Intent(Register_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void choosePhotoFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                try {
                    actualImage = FileUtil.from(this, data.getData());
                    customCompressImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void customCompressImage() {
        if (actualImage == null) {
            showError("Please choose an image!");
        } else {
            new Compressor(this)
                    .setMaxWidth(80)
                    .setMaxHeight(80)
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            switch (calling) {
                                case 1:
                                    compressedImage1 = file;
                                    Glide.with(getApplicationContext())
                                            .load(compressedImage1)
                                            .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(ph_imageView);
                                    break;
                                case 2:
                                    compressedImage2 = file;
                                    Glide.with(getApplicationContext())
                                            .load(compressedImage2)
                                            .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(ph_image1);
                                    break;
                                case 3:
                                    compressedImage3 = file;
                                    Glide.with(getApplicationContext())
                                            .load(compressedImage3)
                                            .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(ph_image2);
                                    break;
                                case 4:
                                    compressedImage4 = file;
                                    Glide.with(getApplicationContext())
                                            .load(compressedImage4)
                                            .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(ph_image3);
                                    break;
                                case 5:
                                    compressedImage5 = file;
                                    Glide.with(getApplicationContext())
                                            .load(compressedImage5)
                                            .apply(new RequestOptions().placeholder(R.drawable.progress_animation))
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(ph_image4);
                                    break;

                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            showError(throwable.getMessage());
                        }
                    });
        }
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void uploadPoll(String up_name, String up_phone, String up_email, String up_experience,
                            String up_location, String up_rating, String up_website, String up_description,String up_speciality,
                            String up_rate,String up_extra,String up_no_pic) {
        Map<String, Object> photographer = new HashMap<>();
        photographer.put("ph_name", up_name);
        photographer.put("ph_email", up_email);
        photographer.put("ph_phone_number", up_phone);
        photographer.put("ph_experience", up_experience);
        photographer.put("ph_location", up_location);
        photographer.put("ph_rating", up_rating);
        photographer.put("ph_description", up_description);
        photographer.put("ph_website", up_website);
        photographer.put("ph_speciality", up_speciality);
        photographer.put("ph_rate", up_rate);
        photographer.put("ph_extra", up_extra);
        photographer.put("ph_no_pic", up_no_pic);
        photographer.put("ph_likes","0");
        db.collection("Photographer")
                .document(phone).set(photographer);
    }
    public String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }
}







//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private boolean validateForm() {
//        boolean alldone;
//        String Name = ph_name.getText().toString().trim();
//        String Email = ph_email.getText().toString().trim();
//        if (TextUtils.isEmpty(Name)) {
//            ph_name.setError("Enter your name", getDrawable(R.drawable.ic_error_white_24dp));
//            return false;
//        } else {
//            ph_name.setError(null);
//        }
//        if (TextUtils.isEmpty(Email)) {
//            ph_email.setError("Enter your Email", getDrawable(R.drawable.ic_error_white_24dp));
//            return false;
//        } else {
//
//            ph_email.setError(null);
//        }
//        return true;
//    }
//
//    private void showPictureDialog() {
//        choosePhotoFromGallary();
//    }
//
//    public void choosePhotoFromGallary() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,
//                "Select Picture"), GALLERY);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_CANCELED) {
//            return;
//        }
//        if (requestCode == GALLERY) {
//            if (data != null) {
//                try {
//                    actualImage = FileUtil.from(this, data.getData());
//                    customCompressImage(false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        }
//    }
//
//
//    public void showError(String errorMessage) {
//        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
//    }
//
//    public void customCompressImage(final Boolean type) {
//        if (actualImage == null) {
//            showError("Please choose an image!");
//        } else {
//            new Compressor(this)
//                    .setMaxWidth(60)
//                    .setMaxHeight(60)
//                    .setQuality(50)
//                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                    .compressToFileAsFlowable(actualImage)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<File>() {
//                        @Override
//                        public void accept(File file) {
//                            compressedImage = file;
//                            if(!type) {
//                                setCompressedImage();
//                            }
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) {
//                            throwable.printStackTrace();
//                            showError(throwable.getMessage());
//                        }
//                    });
//        }
//    }
//
//    private void setCompressedImage() {
//        ph_imageView.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
//
//    }
//
//    private void uploadImage() {
//        storageReference = storage.getReferenceFromUrl("gs://oym-1e52d.appspot.com/" + phone + "/");
//        final StorageReference ref = storageReference.child("profile_picture");
//        if (compressedImage != null) {
//
//            ref.putFile(Uri.fromFile(compressedImage)).continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//                    return ref.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
//                @Override
//                public void onComplete(@NonNull Task<Task<Uri>> task) {
//                    if (task.isSuccessful()) {
//                        user.put("us_profile_image_url", task.getResult().toString());
//                        db.collection("Photographer").document("Users")
//                                .collection(phone).document("ProfileInformation")
//                                .update(user);
//
//                    }
//                }
//            });
//        } else {
//            actualImage = new File(getURLForResource(R.drawable.blank_profile_picture_973460_960_720));
//            customCompressImage(true);
//        }
//        String path = compressedImage.getAbsolutePath();
//        Intent intent = new Intent(Register_Activity.this, Home.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("image", path);
//        startActivity(intent);
//        finish();
//
//    }
//    public String getURLForResource(int resourceId) {
//        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
//    }
//}