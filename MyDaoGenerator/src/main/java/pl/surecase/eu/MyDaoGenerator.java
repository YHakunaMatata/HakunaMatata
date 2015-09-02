package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(15, "com.yahoo.hakunamatata.dao");
        Entity post = schema.addEntity("Post");
        post.addLongProperty("internalId").primaryKey().autoincrement();
        post.addStringProperty("id");
        post.addDateProperty("created_time");
        post.addStringProperty("message");
        post.addStringProperty("type");
        post.addStringProperty("picture");
        post.addStringProperty("full_picture");
        post.addStringProperty("link");


        Entity user = schema.addEntity("User");
        user.addLongProperty("internalId").primaryKey().autoincrement();
        user.addStringProperty("id");
        user.addStringProperty("name");


        Entity like = schema.addEntity("Like");
        like.addLongProperty("internalId").primaryKey().autoincrement();
        like.addStringProperty("id");
        like.addIntProperty("total_count");

        Entity picture = schema.addEntity("Picture");
        picture.addLongProperty("internalId").primaryKey().autoincrement();
        picture.addStringProperty("id");
        picture.addStringProperty("url");


        // post to user => one to one rel
        Property postUserIdMapping = post.addLongProperty("postUserIdMapping").getProperty();
        post.addToOne(user, postUserIdMapping);

        // post to like => one to one rel
        Property postLikeIdMapping = post.addLongProperty("postLikeIdMapping").getProperty();
        post.addToOne(like, postLikeIdMapping);


        // user to picture => one to one rel
        Property userPictureIdMapping = user.addLongProperty("userPictureIdMapping").getProperty();
        user.addToOne(picture, userPictureIdMapping);


//        Entity media = schema.addEntity("Media");
//        media.addLongProperty("internalId").primaryKey().autoincrement();
//        media.addLongProperty("id");
//        media.addStringProperty("type");
//        media.addStringProperty("media_url");
//        Property inTweets = media.addLongProperty("inTweets").getProperty();
//        twitter.addToMany(media, inTweets);
//
//        // twitter to user => one to one rel
//        Property twitterUserIdMapping = twitter.addLongProperty("twitterUserIdMapping").getProperty();
//        twitter.addToOne(user, twitterUserIdMapping);
//
//        // twit replied to twit
//        Property in_reply_to_user_id = twitter.addLongProperty("in_reply_to_user_id").getProperty();
//        twitter.addToMany(twitter, in_reply_to_user_id);

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
