package com.srjlove.realm;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.srjlove.realm.model.SocialAccount;
import com.srjlove.realm.model.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private static final String TAG = UsersAdapter.class.getSimpleName();
    private Context mContext;
    private RealmResults<User> mRealmUserList;
    private LayoutInflater mInflater;
    private Realm mRealm;

     UsersAdapter(Context context, Realm realm, RealmResults<User> realmUserList) {

        this.mContext = context;
        this.mRealm = realm;
        this.mRealmUserList = realmUserList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.list_item, parent, false);
      //  MyViewHolder viewHolder = new MyViewHolder(itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        User user = mRealmUserList.get(position);
        holder.setData(user, position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return mRealmUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private int position;
        private TextView txvNameAge, txvSocialAccount;
        private ImageView imgDelete, imgEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            txvNameAge = itemView.findViewById(R.id.txvNameAge);
            txvSocialAccount = itemView.findViewById(R.id.txvSocialAccount);
            imgDelete = itemView.findViewById(R.id.ivRowDelete);
            imgEdit = itemView.findViewById(R.id.ivRowEdit);
        }

         void setData(User user, int position) {
            this.position = position;

            String userDetail = user.getName() + " : " + user.getAge() + " years old";
            txvNameAge.setText(userDetail);

            SocialAccount account = user.getmAccount();
            if (account != null) {
                String accountDetail = account.getName() + " : " + account.getStatus();
                txvSocialAccount.setText(accountDetail);
            }
        }

         void setListeners() {

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            mRealmUserList.deleteFromRealm(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,mRealmUserList.size());
                        }
                    });
                }
            });

            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, EditActivity.class);
                    i.putExtra("position",position);
                    mContext.startActivity(i);
                }
            });
        }
    }
}
