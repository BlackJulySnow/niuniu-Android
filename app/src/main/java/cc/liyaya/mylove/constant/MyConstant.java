package cc.liyaya.mylove.constant;

public class MyConstant {
    public static boolean ACCESS_FINE_LOCATION = false;
    public static String[] WEEK = {"","一","二","三","四","五","六","日"};
    public static int[] CLASS_EMBEDDING = {0,2,3,5,6,7};
    public static String GetClassURL = "http://10.0.38.246:8080/schedule/getClass";//1.0加入
    public static String TokenURL = "http://10.0.38.246:8080/user/add";//1.0加入
    public static String PushToken = "";
    public static String PushTestURL = "http://10.0.38.246:8080/push/pushOne";
    public static String MEMO_QUERY = "http://10.0.38.246:8080/memo/queryAll";
    public static String MEMO_ADD = "http://10.0.38.246:8080/memo/add";
    public static String MEMO_UPDATE = "http://10.0.38.246:8080/memo/update";
    public static String DORM_QUERY = "http://10.0.38.246:8080/dorm/queryAll";
    public static String DORM_UPDATE = "http://10.0.38.246:8080/dorm/update";
}
