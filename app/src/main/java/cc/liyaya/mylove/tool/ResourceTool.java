package cc.liyaya.mylove.tool;

import cc.liyaya.mylove.MyApplication;

public class ResourceTool {
    public static int getDrawableId(String var) {
        try {
            int imageId = MyApplication.getContext().getResources().getIdentifier(var, "drawable", MyApplication.getContext().getPackageName());
            return imageId;
        } catch (Exception e) {
            return 0;
        }
    }
}
