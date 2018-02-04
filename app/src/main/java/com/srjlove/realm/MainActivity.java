package com.srjlove.realm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.srjlove.realm.model.SocialAccount;
import com.srjlove.realm.model.User;

import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText etPersonName, etAge, etSocialAccountName, etStatus;

    private Realm myRealm;
    private RealmAsyncTask realmAsyncTask;

    private Realm mRealm;
    private TextView disply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPersonName = findViewById(R.id.etPersonName);
        etAge = findViewById(R.id.etAge);
        etSocialAccountName = findViewById(R.id.etSocialAccount);
        etStatus = findViewById(R.id.etStatus);
        disply = findViewById(R.id.tvData);
        disply.setMovementMethod(new ScrollingMovementMethod());

        mRealm = Realm.getDefaultInstance();
    }

    // Add data to Realm using Main UI Thread. Be Careful: As it may BLOCK the UI.
    public void addUserToRealm_Synchronously(View view) {

        final String id = UUID.randomUUID().toString();   // will create random unique Id in string
        final String name = etPersonName.getText().toString();
        final int age = Integer.parseInt(etAge.getText().toString());
        final String socialAName = etSocialAccountName.getText().toString();
        final String status = etStatus.getText().toString();

	   /* try{
            mRealm.beginTransaction();
            SocialAccount mAccount = new SocialAccount();
            mAccount.setName(name);
            mAccount.setStatus(status);

            User mUser = mRealm.createObject(User.class, id); // creating user model in Realm db
            // mUser.setId(id); // will throw exception don't use, instead use it in createObject 2nd parameter
            mUser.setName(name);
            mUser.setAge(age);
            mUser.setmAccount(mAccount);
            mRealm.commitTransaction();
        }catch (Exception e){
	        mRealm.cancelTransaction(); // if some error occurred cancel the transactions
        }*/

        // best way to do all transaction in this Realm(). instead of, try catch :)
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SocialAccount mAccount = realm.createObject(SocialAccount.class);// creating user model in Realm db
                mAccount.setName(socialAName);
                mAccount.setStatus(status);

                User mUser = realm.createObject(User.class, id); // creating user model in Realm db
                // mUser.setId(id); // will throw exception don't use, instead use it in createObject 2nd parameter
                mUser.setName(name);
                mUser.setAge(age);
                mUser.setmAccount(mAccount);
                clearExitText();
                Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Add Data to Realm in the Background Thread.
    public void addUserToRealm_ASynchronously(View view) {

        final String id = UUID.randomUUID().toString();   // will create random unique Id in string
        final String name = etPersonName.getText().toString();
        final int age = Integer.parseInt(etAge.getText().toString());
        final String socialAName = etSocialAccountName.getText().toString();
        final String status = etStatus.getText().toString();

        realmAsyncTask = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SocialAccount mAccount = realm.createObject(SocialAccount.class);// creating user model in Realm db
                mAccount.setName(socialAName);
                mAccount.setStatus(status);

                User mUser = realm.createObject(User.class, id); // creating user model in Realm db
                // mUser.setId(id); // will throw exception don't use, instead use it in createObject 2nd parameter
                mUser.setName(name);
                mUser.setAge(age);
                mUser.setmAccount(mAccount);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                clearExitText();
                Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.this, "Added Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sampleQueryExample(View view) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {

                                            /*Updating Realm db*/
                /*try{
                    User mUser = realm.where(User.class).findFirst();   // modify in this way
                    if (mUser != null) {
                        mUser.setName("Ashish Mishra");
                        mUser.setAge(23);
                    }


                    SocialAccount mSocialAccount = mUser.getmAccount();
                    if (mSocialAccount != null){
                        mSocialAccount.setStatus("going to watch Movies");
                        mSocialAccount.setName("Snapshot");
                    }
                }catch (Exception e){
                    e.getMessage();
                }*/


                                                /*Deleting Realm db*/
                /*try{
                    User mUser = realm.where(User.class).findFirst();
                    if (mUser != null) {
                        mUser.deleteFromRealm(); // for deleting a specific entry
                    }

                    RealmResults<User> mUserRealmResults = realm.where(User.class).findAll();
                    if (mUserRealmResults != null) {
                        mUserRealmResults.deleteAllFromRealm(); // delete all data
                        mUserRealmResults.deleteFirstFromRealm(); // delete 1st data
                        mUserRealmResults.deleteLastFromRealm(); // delete last data
                        mUserRealmResults.deleteFromRealm(3); // delete specific data
                    }
                }catch (Exception e){
                    e.getMessage();
                }*/


                                        /*Searching in Realm*/
                try {
                     /*Breaking Realm db*/
                    RealmQuery<User> realmQuery = realm.where(User.class); // for specific query
                    realmQuery.greaterThan("age", 21);      // condition 1
                    realmQuery.contains("name", "ashish", Case.INSENSITIVE);
                    RealmResults<User> mUserList = realmQuery.findAll();
                    displayUserList(mUserList);

                                        /*Alternately, use Fluid Interface*/

                    RealmResults<User> fluidList = realm.where(User.class)
                            .greaterThan("age", 21)
                            .contains("name", "Anup")
                            .findAll();
                    displayUserList(fluidList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RealmResults<User> findDistinclList= realm.where(User.class).distinct("age");
                displayUserList(findDistinclList);

            }
        });
        //   displayUserList(userlist2);
    }

    private void displayUserList(RealmResults<User> userList) {


        final StringBuilder mBuilder = new StringBuilder();
        for (User mUser : userList) {
            mBuilder.append("ID: ").append(mUser.getId());
            mBuilder.append("\n Name: ").append(mUser.getName());
            mBuilder.append("Age: ").append(mUser.getAge());

            // getSocialAccount model like this way
            SocialAccount mSocialAccount = mUser.getmAccount();
            mBuilder.append("\n Social Account:").append(mSocialAccount.getName());
            mBuilder.append("Status: ").append(mSocialAccount.getStatus()).append("\n\n");
        }

        Log.i(TAG, "displayAllUsers: " + mBuilder.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: "+mBuilder.toString());
                disply.setText(mBuilder.toString()+"\n");
            }
        });

    }

    public void displayAllUsers(View view) {

        // find all data which are in  type of User
        RealmResults<User> userList = mRealm.where(User.class).findAll();
        displayUserList(userList);
    }

    public void openDisplayActivity(View view) {
        startActivity(new Intent(this,DisplayActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // cancel task ifin mid of any phone call come, instead of get errors
        if (realmAsyncTask != null && !realmAsyncTask.isCancelled()) {
            realmAsyncTask.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close(); // if don't do that will get memory leak exceptions
    }

    public void clearExitText() {
        etStatus.setText("");
        etSocialAccountName.setText("");
        etAge.setText("");
        etPersonName.setText("");
    }



}
