package com.srjlove.realm.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Only 1 @PrimaryKey allowed in one model class
 * can't be invoke any annotation from realm it's up to your code
 * can use @RealmClass by implementing RealmModel interface but the more convenient way is
 * by extending th RealmObject
 */
public class User extends RealmObject {

    /**
     * Only 1 @PrimaryKey allowed in one model class
     * String or integral [byte, short, int  or long]
     * Boxed [byte, short, int  or long]
     * @Index         is automaticcally applied to primary key
     */
    @PrimaryKey // in Realm there is no Auto increment features, we have to do it manually.
    private String id;
    private String name;
    private int age;
    private SocialAccount mAccount;

    /* @Index: - this annotation adds a search index to the field, which can be used for search or filter purpose */

    /**
     * @Ignore // this will create a field variable for temporary purpose, but w'd not save in Realm database
     * private String temp;
     * /*
     * <p>
     * /**
     * @Required it's simply disallows Null values
     */
   /* @Required // this will ensures that this variable can not be NULL
    private String important;*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public SocialAccount getmAccount() {
        return mAccount;
    }

    public void setmAccount(SocialAccount mAccount) {
        this.mAccount = mAccount;
    }
}
