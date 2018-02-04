package com.srjlove.realm;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.srjlove.realm.model.SocialAccount;
import com.srjlove.realm.model.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditActivity extends AppCompatActivity {

    private EditText etPersonName, etAge, etSocialAccountName, etStatus;
    private Realm myRealm;

    Bundle bundle;
    int position;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("position", 0);
        }

        etPersonName = findViewById(R.id.etPersonName);
        etAge = findViewById(R.id.etAge);
        etSocialAccountName = findViewById(R.id.etSocialAccount);
        etStatus = findViewById(R.id.etStatus);

        myRealm = Realm.getDefaultInstance();
        RealmResults<User> realmResults = myRealm.where(User.class).findAll();
        user = realmResults.get(position);
        setupViews(user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViews(User user) {

        etPersonName.setText(user.getName());
        etAge.setText(String.valueOf(user.getAge()));

        SocialAccount socialAccount = user.getmAccount();
        if (socialAccount != null) {
            etSocialAccountName.setText(socialAccount.getName());
            etStatus.setText(socialAccount.getStatus());
        }
    }

    public void update(View view) {


            myRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    SocialAccount socialAccount = user.getmAccount();
                    if (socialAccount != null) {
                        socialAccount.setName(etSocialAccountName.getText().toString());
                        socialAccount.setStatus(etStatus.getText().toString());
                    }
                    user.setName(etPersonName.getText().toString());
                    user.setAge(Integer.valueOf(etAge.getText().toString()));
                    user.setmAccount(socialAccount);
                }
            });
        Toast.makeText(this, "Account Updated ", Toast.LENGTH_SHORT).show();

    }

    public void cancel(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}
