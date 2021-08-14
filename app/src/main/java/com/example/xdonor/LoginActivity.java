package com.example.xdonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //initialized varible
    private LinearLayout lytLogin, signUpLyt, lytforgot;
    private TextView tvSignUp, tvGoToLongIn, tvForgotPass, rePassMessage, fpassHad, dateOfBirthDisplay;
    private Button btnrecover;
    private ProgressBar progressBar;
    private EditText loginId, loginPass, userName, userMobile, userPass,
            userAlternativeMobile, userEmail, userUnion, userPostOffice,
            userPOliceStation, userDistrict, bloodGroup, religion, weight;
    private ImageView regProfileImage;
    private View view;
    private Spinner bloodGroupSpinner;
    String year, month, day;
    //camera variable initialized
    private static final String TAG = "Login activity";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    private static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Uri imageUri;
    String encodeImage;
    private String bloodGroupValue;
    //firebase variables initialized
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    //userID
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //call Assign variable method
        // to assign variable data
        assignVariable();
        setWidgets();
    }

    /**
     * Assing variables method
     */
    private void assignVariable() {
        progressBar = findViewById(R.id.progressBar);

        signUpLyt = findViewById(R.id.signUpLyt);
        lytforgot = findViewById(R.id.lytforgot);
        tvForgotPass = findViewById(R.id.tvForgotPass);

        bloodGroupSpinner = findViewById(R.id.blood_group_spinner);
        //login views
        lytLogin = findViewById(R.id.lytLogin);
        loginId = findViewById(R.id.loginId);
        loginPass = findViewById(R.id.loginPass);
        //singUp views
        userName = findViewById(R.id.user_name);
        userMobile = findViewById(R.id.user_mobile);
        userAlternativeMobile = findViewById(R.id.user_alt_mobile);
        userUnion = findViewById(R.id.user_address_union);
        userPOliceStation = findViewById(R.id.user_address_police_station);
        userPostOffice = findViewById(R.id.user_address_post_office);
        userDistrict = findViewById(R.id.user_address_district);
        userEmail = findViewById(R.id.user_email);
        userPass = findViewById(R.id.user_pass);
        religion = findViewById(R.id.user_religion);
        weight = findViewById(R.id.user_weight);
        dateOfBirthDisplay = findViewById(R.id.date_of_birth_display);
        regProfileImage = findViewById(R.id.regProfileImage);
        //firebase variable assign
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

    }

    /**
     * @param view OnBtnClick for handle button clicks
     */
    public void OnBtnClick(View view) {
        int id = view.getId();
        // TODO: 8/10/2021 OnClick handeler
        switch (id) {
            case R.id.tvSignUp:
                signUpLyt.setVisibility(View.VISIBLE);
                lytLogin.setVisibility(View.GONE);
                break;
            case R.id.tvGoToLogin:
                signUpLyt.setVisibility(View.GONE);
                lytLogin.setVisibility(View.VISIBLE);
                break;
            case R.id.btnLogin:
                if (validLoginData()) {
                    login();
                }
                break;
            case R.id.btnSubmit:
                if (validSingupData()) {
                    singUp();
                }
                break;

            case R.id.regProfileImage:
                openGallery();
                break;
            case R.id.date_of_birth_input:
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LoginActivity.this, this, year, month, day);
                datePickerDialog.show();
                break;
        }
    }

    /**
     * @implNote suing up mehtod
     * @apiNote get method used for check authentication data
     */
    private void singUp() {
        // TODO: 8/10/2021 store data in database and start main activity
        String user_name = userName.getText().toString();
        String user_mobile = userMobile.getText().toString();
        String user_alterMobile = userAlternativeMobile.getText().toString();
        String user_union = userUnion.getText().toString();
        String user_police_station = userPOliceStation.getText().toString();
        String user_post_office = userPostOffice.getText().toString();
        String user_district = userDistrict.getText().toString();
        String user_email = userEmail.getText().toString();
        String user_relegion = religion.getText().toString();
        String user_weight = weight.getText().toString();
        String user_blood_group = bloodGroupValue;
        String user_pass = userPass.getText().toString();
        String date_of_birth = dateOfBirthDisplay.getText().toString();
        if (imageUri != null) {
            //display progressbar for store data to firebase database
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(user_email, user_pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //check profile image is empty or not
                    if (task.isSuccessful()) {
                        //if not empty upload image in firebase storgage
                        // and get image url
                        Uri file = imageUri;
                        StorageReference storageRef = storage.getReference();
                        StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
                        riversRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //after Successfull upload image in firebase storage
                                        //get that image link
                                        //and store user data including generated image link
                                        //to firestore database
                                        String imageLink = String.valueOf(uri);
                                        userId = mAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
                                        //create a user data amp
                                        //for storing that data to firestore
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("name", user_name.toUpperCase());
                                        userData.put("email", user_email);
                                        userData.put("password", user_pass);
                                        userData.put("mobile", user_mobile);
                                        userData.put("alterMobile", user_alterMobile);
                                        userData.put("union", user_union);
                                        userData.put("policeStation", user_police_station);
                                        userData.put("postOffice", user_post_office);
                                        userData.put("district", user_district);
                                        userData.put("relegion", user_relegion);
                                        userData.put("weight", user_weight);
                                        userData.put("bloodGroup", user_blood_group);
                                        userData.put("dateOfBirth", date_of_birth);
                                        userData.put("details", "Aenean dignissim molestie ligulatempus urna ullamcorper eget." +
                                                " Proin venenatis non elit ac feugiat. Aliquam et nibh quis " +
                                                "justo semper tristique vel in nisi. Ut tincidunt enim metus,\n" +
                                                "et consectetur mauris vestibulum in. Donec\n" +
                                                "eu iaculis eros.Maecenas odio lectus, varius\n" +
                                                "id lobortis sit amet, tempor et elit.");
                                        userData.put("profilePic", imageLink);
                                        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "User account has been created for: " + userId);
                                            }
                                        });
                                        Log.d(TAG, "onSuccess: " + imageLink);
                                        DocumentReference documentReference1 = firebaseFirestore.collection("Users")
                                                .document(userId).collection("activity")
                                                .document("initial");
                                        documentReference1.set(new HashMap<String, Object>());


                                        //after sucessfull upload data in database
                                        //hide progressbar and let user go to dash bord
                                        progressBar.setVisibility(View.GONE);
                                        // Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                        finish();
                                    }
                                });
                            }
                        });

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Image filed is empty", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * @return false if data is nbot valid
     * @implNote check user data empty or not
     */
    private boolean validSingupData() {
        dateOfBirthDisplay = findViewById(R.id.date_of_birth_display);
        if (userName.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Empty name field", Toast.LENGTH_SHORT).show();
            return false;
        } else if (userMobile.getText().toString().isEmpty()
                || userMobile.getText().toString().length() < 11
                || (!userAlternativeMobile.getText().toString().isEmpty()
                && userAlternativeMobile.getText().toString().length() < 11)) {
            Toast.makeText(getApplicationContext(), "Invalid Mobile NUmber", Toast.LENGTH_SHORT).show();
            return false;
        } else if (userUnion.getText().toString().isEmpty()
                || userPostOffice.getText().toString().isEmpty()
                || userPOliceStation.getText().toString().isEmpty() ||
                userDistrict.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid Address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (invalidEmail()) {
            return false;
        } else if (userPass.getText().toString().isEmpty() || userPass.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "Empty or invalid password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (weight.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Empty Weight", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dateOfBirthDisplay.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Empty date of birth", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    /**
     * @return true if Email is empty or invalid
     */
    private boolean invalidEmail() {
        String email = userEmail.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Empty Email Field", Toast.LENGTH_SHORT).show();
            return true;
        } else if (!email.matches(EMAIL_REGEX)) {
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return true;
        } else
            return false;
    }

    /**
     * @implNote login when login button pressed
     * @apiNote get method used for check authentication data
     */
    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        String email = loginId.getText().toString();
        String password = loginPass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //if login Successful
                                    //then store login time, and date in database
                                    storeLoginActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            task.getException().toString(),
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                        });

    }

    /**
     * store login activity in database
     */
    private void storeLoginActivity() {
        userId = mAuth.getCurrentUser().getUid();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        DocumentReference documentReference = firebaseFirestore
                .collection("Users").document(userId)
                .collection("activity").document();
        Map<String, Object> activityLog = new HashMap<>();
        activityLog.put("date", currentDate);
        activityLog.put("time", currentTime);
        activityLog.put("activity", "Login");
        documentReference.set(activityLog).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });
    }

    /**
     * @return false if data is nbot valid
     * @implNote check user login data validate or not
     */
    private boolean validLoginData() {
        if (loginId.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "invalide used id", Toast.LENGTH_LONG).show();
            return false;
        } else if (loginPass.getText().toString().isEmpty() || loginPass.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "invalide Password", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    /**
     * @implNote setup blood group data for dropdown menu
     */
    private void setWidgets() {
        String[] bloodGroupList = {"A+", "O+", "B+", "AB+", "A-", "O-", "B-", "AB-"};
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, bloodGroupList);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView1 = (TextView) view;
                if (textView1 != null) {
                    textView1.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                    //passing selected blood group
                    //with getBloodGroup() method
                    //with a selected blood group
                    getBloodGroup(textView1.getText().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * this methos for get selected blood group
     *
     * @param toString
     */
    private void getBloodGroup(String toString) {
        bloodGroupValue = toString;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DecimalFormat format = new DecimalFormat("0000");
        this.year = format.format(year);
        DecimalFormat formatter = new DecimalFormat("00");
        this.day = formatter.format(dayOfMonth);
        this.month = formatter.format(month + 1);
        dateOfBirthDisplay.setText(day + "/" + month + "/" + year);
    }

    /**
     * pick image from gallery
     */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    //todo camera and gallery permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            regProfileImage.setImageURI(imageUri);
            /*try {
                //Initialize bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                //Initialize byte stream
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //Compressed bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                //Initialized byte array
                byte[]bytes = stream.toByteArray();
                //get base64 encoded string
                encodeImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                //Initialized bytes for decode
                byte[] bytes1 = Base64.decode(encodeImage,Base64.DEFAULT);
                //Initialized bitmap for decode
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
                regProfileImage.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }

    }
}