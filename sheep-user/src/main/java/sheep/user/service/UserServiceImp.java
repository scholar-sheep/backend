package sheep.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.user.entity.Paper;
import sheep.user.entity.UserCollect;
import sheep.user.mapper.UserMapper;
import sheep.user.entity.User;
import sheep.user.util.HttpUtil;
import sheep.user.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service @Slf4j
public class UserServiceImp implements UserService{
    @Autowired
    private UserMapper userMapper;
    //@Autowired
    //private PaperMapper paperMapper;

    //apikey
    //@Value("${sms.yunpian.apiKey}")
    private final String apiKey = "6982fb8a80dfac6af94d87e45c51e45d";
    //‘单发模板短信’的url
    //@Value("${sms.yunpian.tplVerUrl}")
    private final String tplVerUrl = "https://sms.yunpian.com/v2/sms/tpl_single_send.json";
    //短信模板的id
    //@Value("${sms.yunpian.tplVerId}")
    private final String tplVerId = "3922606";
    //urlencode编码的格式
    private static final String URLENCODING = "UTF-8";

    @Override
    public List<User> getUserList() {
        List<User> users = userMapper.getUserList();
        return users;
    }

    @Override
    public User getUserById(int ID) {
        User user = userMapper.getUserById(ID);
        return user;
    }

    @Override
    public User getUserByTel(String tel)
    {
        return userMapper.getUserByTel(tel);
    }

    @Override
    public int updateUserInfo(User user) {
        return userMapper.updateUserInfo(user);
    }

   @Override
   public int updateUserPassword(int ID,String password){
        return userMapper.updateUserPassword(ID,password);
   }

    @Override
    public int addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public User getUserByName(String username) {
        User user = userMapper.getUserByName(username);
        return user;
    }

    @Override
    public int getUserByMobile(String mobile) {
        int result = userMapper.getUserByMobile(mobile);
        return result;
    }

    @Override
    public int changeAvatar(int ID, String avatar) {
        return userMapper.changeAvatar(ID,avatar);
    }

    @Override
    public int follow(int currentID, int followID) {
        return userMapper.follow(currentID,followID);
    }

    @Override
    public List<Integer> getFollow(int ID) {
        return userMapper.getFollow(ID);
    }

    @Override
    public String getCode(String mobile) {
        return this.sendVerificationCodeSms(mobile);
    }

    @Override
    public String sendVerificationCodeSms(String mobile) {
        //验证码
        String verificationCode = new String();

        try {
            //生成六位数的验证码。
            verificationCode = StringUtil.generateVerificationCode(6);
            log.info("生成验证码{}" , verificationCode);
            String tpl_value = URLEncoder.encode("#code#", URLENCODING) + "="
                    + URLEncoder.encode(verificationCode, URLENCODING);
            Map<String,String> params = new HashMap<>();
            params.put("apikey",apiKey);
            params.put("mobile",mobile);
            params.put("tpl_id",tplVerId);
            params.put("tpl_value",tpl_value);
            //发送验证码
            HttpUtil.post(tplVerUrl,params);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        //new UserServiceImp().updateUserCode(mobile,verificationCode);
        System.out.println("sendVerificationCodeSms"+mobile+" "+verificationCode);
        if(userMapper.getCodeByMobile(mobile)==0){
            userMapper.insertUserCode(mobile,verificationCode);
        }
        else userMapper.updateUserCode(mobile,verificationCode);
        return verificationCode;
    }

    @Override
    public int getCodeByMobile(String mobile) {
        return userMapper.getCodeByMobile(mobile);
    }

    @Override
    public String getCodeSaved(String mobile){
        return userMapper.getCodeSaved(mobile);
    }

    @Override
    public int updateUserCode(String mobile, String code) {
        //if(getCodeByMobile(mobile)!=null)
        //    userMapper.updateUserCode(mobile,code);
        System.out.println(mobile);
        System.out.println(code);
        //MobileCode mobileCode = new MobileCode();
        //mobileCode.setMobile(mobile);
        //mobileCode.setCode(code);
        return userMapper.insertUserCode(mobile,code);
    }

    @Override
    public int collect(int userId, int paperId, int infoId) {
        return userMapper.collect(userId,paperId,infoId);
    }

    @Override
    public List<UserCollect> getCollect(int ID) {
        return userMapper.getCollect(ID);
    }

    @Override
    public Paper getPaperById(int paperId,int infoId) {
        return userMapper.getPaperById(paperId,infoId);
//        return userMapper.getPaperById(paper_id);
    }

    /**
    public User selectById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public void register(UserRegisterVo userRegisterVo)
    {
        // 检查用户名是否唯一
        checkUserNameUnique(userRegisterVo.getUserName());
        //该用户信息唯一，进行插入
        User entity = new User();
        //保存基本信息
        entity.setUsername(userRegisterVo.getUserName());
        entity.setCreate_time(new Date());
        //使用加密保存密码 --待实现
        entity.setPassword(userRegisterVo.getPassword());

        // 保存用户信息
        userMapper.insert(entity);
    }
    private void checkUserNameUnique(String userName) {
        Integer count = userMapper.selectCount(new QueryWrapper<User>().eq("username", userName));
        if (count > 0) {
            throw new UserExistException();
        }
    }
    */
}
