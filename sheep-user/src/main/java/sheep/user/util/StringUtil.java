package sheep.user.util;

public class StringUtil {
    /**
     * 生成验证码
     * @param length 验证码的长度
     * @return 返回需要生成的验证码
     */
    public static String generateVerificationCode(int length){
        StringBuilder sb = new StringBuilder();
        for(int i =0 ;i < length ;i ++){
            sb.append((int)(Math.random() * 9)  + 1);
        }
        return sb.toString();
    }
}
