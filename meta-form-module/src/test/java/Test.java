import com.centit.support.security.AESSecurityUtils;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

public class Test {
    public static void main(String[] args) throws GeneralSecurityException {
        String passwd = "fdemo2";
        String key = "0123456789abcdefghijklmnopqrstuvwxyzABCDEF";
        Cipher cipher = AESSecurityUtils.createEncryptCipher("0123456789abcdefghijklmnopqrstuvwxyzABCDEF");
        String encodePwd = AESSecurityUtils.encryptAndBase64(passwd,key);
        System.out.println("encodePwd:"+encodePwd);
        String decodePwd = AESSecurityUtils.decryptBase64String(encodePwd,key);
        System.out.println("decodePwd:"+decodePwd);
    }

}
