package cc.liyaya.mylove.tool;

public class StringUtil {
    public static String append2(String s){
        String regex = "(.{2})";
        s = s.replaceAll(regex,"$1\n");
        return s.substring(0,s.length() - 1);
    }
}
