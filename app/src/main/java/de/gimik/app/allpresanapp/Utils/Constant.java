package de.gimik.app.allpresanapp.Utils;

import java.util.TimeZone;

/**
 * Created by QuyenDT on 2/10/15.
 */
public class Constant {

    public static final TimeZone CONFERENCE_TIME_ZONE = TimeZone.getTimeZone("Europe/Paris");
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_EVENT = "EEE, dd-MM-yyyy";
    public static final String DATE_EVENT_DETAIL = "dd MMM yyyy";
    /**
     * PREFS FILE
     */
    public static final String PREFS_NAME = "AllpresanPrefsFile";
    /**
     * PREFS KEY *
     */
    public static final String PREF_DB_TIMESTAMP = "db_timestamp";
    public static final String EMAIL_SEND_FEEDBACK = "dtquyen2002@yahoo.com";

    public static final String DATE_DOWNLOAD = "updateDate";
    public static final String NEWDATA = "newData";
    public static final String ZIP_FILE = "zipFile";
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_WORLD_ID = "productWorldId";
    public static final String PRODUCT_GROUP_ID = "productGroupId";
    public static final String PRODUCT_GROUP_NAME = "productGroupName";

    public static final String EVENT_ID = "eventId";
    public static final String EVENT_TYPE = "eventType";
    public static final String INGREDIENT_ID = "ingredientId";
    public static final String COLOR = "color";

    public static final String PRODUCT_RECOMMEND_ID = "productRecommendId";
    public static final String IMAGE_CAMERA_PATH = "imagePath";
    public static final String SUBJECT_EMAIL = "AllPresan send image";
    public static final String SUBJECT_EMAIL_FEEDBACK = "User send feedback";

    /*
    public static final String EAN_TABLE_NAME = "ean";
    public static final String EVENT_TABLE_NAME = "event";
    public static final String INGREDIENT_TABLE_NAME = "ingredient";
    public static final String PRODUCT_INGREDIENT_TABLE_NAME = "product_ingredient";
    public static final String PRODUCT_TABLE_NAME = "product";
    public static final String PRODUCT_GROUP_TABLE_NAME = "product_group";

    public static final String PRODUCT_RECOMMENDATION_PRODUCT_TABLE_NAME = "product_recommendation_product";
    public static final String PRODUCT_RECOMMENDATION_TABLE_NAME = "product_recommendation";
    public static final String PRODUCT_WORLD_TABLE_NAME = "product_world";
    public static final String SERVICE_QUESTION_TABLE_NAME = "service_question";
    public static final String PRODUCT_REMOVED_TABLE_NAME = "data_removed";
*/

    public static final Object IMAGE = "image";
    public static final String APP_AREA_ID = "app_area_id";
    public static final String PRODUCT_WORLD_PEDIONE = "pedione";
    public static final String PRODUCT_WORLD_COLOR = "#707173";

    public static final String NOT_FRIST_TIME = "not_frist_time";
    public static final String SPLASH_IMAGE_POSITION = "splash_image_position";

    public interface ExtraKey {
        public static final String PREF_KEEP_LOGIN = "keep_login";
        String MESSAGE = "message";
        String MESSAGE_COUNT = "message_count";
        String LAYOUT_ID = "layout_id";
        String TITLE_ID = "title_id";
        String IS_ACADEMY_NEWS = "is_academy_news";
        String IS_OPEN_FROM_NOTIFICATION = "is_open_from_notification";
    }


    public interface GcmPreferences {
        public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
        public static final String REGISTRATION_COMPLETE = "registrationComplete";
    }
    public static final int takePhoto = 200;
    public static final int CAMERA_REQUEST_CODE = 10;
    public static final String URL_SEARCH_FOOT = "http://www.fussprofi-suche.de/";
}
