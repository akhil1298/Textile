package fourever.textile.miscs;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by akhil on 24/8/15.
 */
public class Validation {

    public static boolean name(String names)
    {
        if(!TextUtils.isEmpty(names))
            return true;
        else
            return false;
    }

    public static boolean mobno(String mobno)
    {
        if(!TextUtils.isEmpty(mobno)) {
            if (mobno.length() == 10)
                return true;
            else
                return false;
        }else {
            return false;
        }
    }

    public static boolean emailCheck(String email)
    {
        if(!TextUtils.isEmpty(email)) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
                return true;
            else
                return false;
        }else {
            return false;
        }
    }

    public static boolean passwordmatcher(String pass, String Conformpass){
        if(!TextUtils.isEmpty(pass) && !TextUtils.isEmpty(Conformpass)){
            if(pass.equals(Conformpass)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

}
