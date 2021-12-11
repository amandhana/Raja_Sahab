package com.rajasahabacademy.api;

import android.annotation.SuppressLint;

import com.google.firebase.auth.PhoneAuthProvider;
import com.rajasahabacademy.fragment.CourseFragment;
import com.rajasahabacademy.fragment.HomeFragment;
import com.rajasahabacademy.fragment.LiveClassFragment;
import com.rajasahabacademy.fragment.LiveQuizFragment;
import com.rajasahabacademy.fragment.NotesFragment;
import com.rajasahabacademy.model.attempt_quiz.Question;
import com.rajasahabacademy.model.content.ContentResponse;

import java.util.List;

public class Constants {
    private static final String BASE_URL_DOMAIN = "https://rs.webseochicago.com/";
    private static final String BASE_URL = BASE_URL_DOMAIN + "api/";
    public static final String EBOOK_PATH = "https://drive.google.com/viewerng/viewer?embedded=true&url=";

    public static class FragmentReference {
        @SuppressLint("StaticFieldLeak")
        public static HomeFragment homeFragment = null;
        @SuppressLint("StaticFieldLeak")
        public static CourseFragment courseFragment = null;
        @SuppressLint("StaticFieldLeak")
        public static LiveQuizFragment liveQuizFragment = null;
        @SuppressLint("StaticFieldLeak")
        public static LiveClassFragment liveClassFragment = null;
        @SuppressLint("StaticFieldLeak")
        public static NotesFragment notesFragment = null;
    }

    public static class Preference {
        public static final String ISLOGIN = "isLogin";
        public static final String FROM_WHERE = "from_where";
        public static final String COURSE_IMAGE = "course_image";
    }

    public static class Quiz {
        public static final String QUIZ_NAME = "quiz_name";
        public static final String FROM_WHERE = "from_where";
        public static final String FROM_WHERE_VALUE = "notification";
    }
    public static class ResearchPaper {
        public static final String DESCRIPTION = "description";
        public static final String STATUS = "status";
        public static final String PATH = "path";
        public static final String THUMBNAIL = "thumbnail";
        public static final String TITLE = "title";
        public static final String IS_CART = "is_cart";
        public static final String RESEARCH_ID = "research_id";
    }

    public static class ViewSolution {
        public static final String LIST = "attempted_list";
    }

    public static class Params {
        public static final String DEVICE_ID = "device_id";
        public static final String DEVICE_TOKEN = "device_token";
        public static final String PHONE = "phone";
        public static final String USER_ID = "user_id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String IMAGE = "image";
        public static final String ADDRESS = "address";
        public static final String CITY = "city";
        public static final String STATE = "state";
        public static final String EDUCATION = "education";
        public static final String CATEGORY_ID = "category_id";
        public static final String COURSE_ID = "course_id";
        public static final String SUBJECT_ID = "subject_id";
        public static final String TEST_ID = "test_id";
        public static final String TEST_DURATION = "test_duration";
        public static final String PRICE = "price";
        public static final String PAGE = "page";
        public static final String CONTACT = "contact";
        public static final String MESSAGE = "message";
        public static final String QUESTION_ANS_DATA = "question_ans_data";
        public static final String QUESTION_ID = "question_id";
        public static final String OPTION = "option";
        public static final String ANSWERED = "answered";
        public static final String TIME_TAKEN = "time_taken";
        public static final String VIDEO_ID = "video_id";
        public static final String LAST_ID = "last_id";
        public static final String NOTE_ID = "note_id";
        public static final String NOTES = "notes";
        public static final String RESEARCH_ID = "research_id";
        public static final String RESEARCHS = "researches";

    }

    public static class Course {
        public static final String COURSE_ID = "course_id";
        public static final String COURSE_TITLE = "course_title";
        public static final String EXPIRE_AMOUNT = "expire_amount";
        public static final String TOTAL_AMOUNT = "total_amount";
        public static final String SUBJECT_ID = "subject_id";
        public static final String VIDEO_ID = "video_id";
        public static final String VIDEO_PATH = "video_path";
        public static final String VIDEO_DESCRIPTION = "video_description";
        public static final String VIDEO_BOOKMARK = "video_bookmark";
        public static final String VIDEO_TYPE = "video_type";
        public static final String SUBJECT = "subject";
        public static final String EBOOK_PATH = "ebook_path";
        public static final String EBOOK_NAME = "ebook_name";
        public static final String COURSE_BUY_STATUS = "course_buy_status";
        public static final String YOUTUBE_VIDEO_TYPE = "url";
        public static final String FROM_WHERE = "from_where";
        public static final String FROM_WHERE_VALUE = "notification";
        public static final String FROM_WHERE_VALUE_SLIDER = "slider";
        public static final String OFFLINE_FILE = "offline_file";
        public static final String WALLET_AMOUNT = "wallet_amount";
    }

    public static class Payment{
        public static final String TOTAL_AMOUNT = "total_amount";
    }

    public static class Apis {
        public static final String LOGIN = BASE_URL + "login";
        public static final String REGISTER = BASE_URL + "register";
        public static final String PROFILE_DETAIL = BASE_URL + "details";
        public static final String UPDATE_PROFILE = BASE_URL + "udpateprofile";
        public static final String HOME_SLIDER_BANNER = BASE_URL + "sliders";
        public static final String HOME_CATEGORY = BASE_URL + "category";
        public static final String HOME_LATEST_COURSE = BASE_URL + "dashboard_courses";
        public static final String COURSES = BASE_URL + "courses";
        public static final String COURSE_SUBJECT = BASE_URL + "subjects_by_course";
        public static final String COURSE_VIDEO = BASE_URL + "videos";
        public static final String COURSE_EBOOK = BASE_URL + "ebooks";
        public static final String QUIZ_TESTS = BASE_URL + "tests";
        public static final String QUIZ_QUESTION_ANS = BASE_URL + "testbyid";
        public static final String BUY_COURSE = BASE_URL + "sell_order";
        public static final String ORDER_HISTORY = BASE_URL + "order_history";
        public static final String GET_CONTENT = BASE_URL + "get_content";
        public static final String CONTENT_US = BASE_URL + "contact_us";
        public static final String CHATS = BASE_URL + "chats";
        public static final String SEND_MESSAGE = BASE_URL + "add_chat";
        public static final String NOTIFICATIONS = BASE_URL + "notifications";
        public static final String NOTES_EBOOK = BASE_URL + "notes";
        public static final String SUBMIT_TEST = BASE_URL + "sub_test";
        public static final String QUIZ_RANK = BASE_URL + "results";
        public static final String LOGOUT = BASE_URL + "logout";
        public static final String GET_VIDEO_ALL_CHAT = BASE_URL + "video_all_chat";
        public static final String SEND_VIDEO_MESSAGE = BASE_URL + "send_video_message";
        public static final String GET_UPDATE_CHAT_DATA = BASE_URL + "last_chats";
        public static final String GET_ATTEMPTED_QUIZ = BASE_URL + "get_attempted_quiz";
        public static final String QUIZ_INSTRUCTION = BASE_URL + "quiz_instruction";
        public static final String CONTACT_US = BASE_URL + "contact_us";
        public static final String ADD_BOOKMARK_QUESTION = BASE_URL + "add_bookmark_question";
        public static final String REMOVE_BOOKMARK_QUESTION = BASE_URL + "remove_bookmark_question";
        public static final String GET_BOOKMARK_QUESTION = BASE_URL + "get_bookmark_question";
        public static final String ADD_BOOKMARK_VIDEO = BASE_URL + "add_bookmark_video";
        public static final String REMOVE_BOOKMARK_VIDEO = BASE_URL + "remove_bookmark_video";
        public static final String GET_BOOKMARK_VIDEO = BASE_URL + "get_bookmark_video";
        public static final String GET_COUPONS = BASE_URL + "get_coupons";
        public static final String ADD_NOTE_CART = BASE_URL + "add_note_carts";
        public static final String REMOVE_NOTE_CART = BASE_URL + "remove_note_cart";
        public static final String GET_NOTE_CART = BASE_URL + "note_carts";
        public static final String ADD_NOTE_ORDER = BASE_URL + "add_note_order";
        public static final String RESEARCH_PAPER_LIST = BASE_URL + "researches";
        public static final String ADD_RESEARCH_CART = BASE_URL + "add_research_carts";
        public static final String REMOVE_RESEARCH_CART = BASE_URL + "remove_research_cart";
        public static final String RESEARCH_CART_LIST = BASE_URL + "researches_carts";
        public static final String ADD_RESEARCH_ORDER = BASE_URL + "add_research_order";
    }

    public static class AppSaveData {
        public static PhoneAuthProvider.ForceResendingToken token = null;
        public static ContentResponse contentResponse = null;
        public static List<Question> viewSolutionList = null;
        public static String homeCartCount = "0";
    }

}
