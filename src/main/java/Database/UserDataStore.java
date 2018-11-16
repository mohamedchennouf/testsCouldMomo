package Database;

import Entity.Users;
import utils.JsonGenerator;

import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.cloud.datastore.*;

import java.util.ArrayList;


public class UserDataStore {
    // Mail,Point,Rank
    private static Datastore datastore;
    private static KeyFactory keyFactory;
    static private UserDataStore instance;

    private UserDataStore() {
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("User2");
    }

    static public UserDataStore getInstance() {
        if (instance == null) {
            instance = new UserDataStore();
        }
        return instance;
    }


    public static void updateUser(Users user){
        Key key = keyFactory.newKey(user.getId());
        Entity entity = Entity.newBuilder(key)
                .set(Users.EMAIL, user.getEmail())
                .set(Users.PASSWORD, user.getPassword())
                .set(Users.LEVEL, user.getLevel())
                .set(Users.POINT, user.getPoint())
                .build();
        datastore.update(entity);
    }

    public static ArrayList<Users> getAllUser(){
        ArrayList<Users> res = new ArrayList<>();
        EntityQuery datastore_query = Query.newEntityQueryBuilder()
                .setKind("User2")
                .build();
        QueryResults<Entity> datastore_users = datastore.run(datastore_query);
        while (datastore_users.hasNext()) {
            Entity potencial_user = datastore_users.next();
            Users newUser = new Users(potencial_user.getString(Users.EMAIL), potencial_user.getString(Users.PASSWORD), Integer.parseInt(potencial_user.getString(Users.POINT)), potencial_user.getString(Users.LEVEL));
            res.add(newUser);
        }
        return res;
    }

    public static void addUser(Users user){
        IncompleteKey key = keyFactory.newKey();
        FullEntity<IncompleteKey> userData = Entity.newBuilder(key)
                .set(Users.EMAIL,user.getEmail())
                .set(Users.LEVEL,user.getLevel())
                .set(Users.POINT,user.getPoint())
                .set(Users.PASSWORD,user.getPassword())
                .build();
        Entity newUser = datastore.add(userData);
    }

    public static Users getUser(String mail,String mdp){
        Users res = null;
        Entity potencial_user;

        EntityQuery datastore_query = Query.newEntityQueryBuilder()
                .setKind("User2")
                .build();
        QueryResults<Entity> datastore_users = datastore.run(datastore_query);

        while (datastore_users.hasNext()) {
            potencial_user = datastore_users.next();
            if (mail.equals(potencial_user.getString("mail")) && mdp.equals(potencial_user.getString("pass"))) {
                res = new Users(potencial_user.getString("mail"), potencial_user.getString("password"), Integer.parseInt(potencial_user.getString("point")), potencial_user.getString("level"));
            }
        }

        return res;
    }
}