import Model.OauthData;
import com.yunkuent.sdk.DebugConfig;
import com.yunkuent.sdk.EntLibManager;
import com.yunkuent.sdk.EntManager;
import com.yunkuent.sdk.data.ReturnResult;
import com.yunkuent.sdk.data.SyncMemberData;
import com.yunkuent.sdk.utils.Util;
import org.apache.http.HttpStatus;

import java.util.ArrayList;

/**
 * Created by Brandon on 2014/8/6.
 */
public class SDKTester {
    public static final String UESRNAME = "";
    public static final String PASSWORD = "";
    public static final String CLIENT_ID = "";
    public static final String CLIENT_SECRET = "";

    private static EntLibManager mEntLibManger;
    private static EntManager mEntManger;

    public static void main(String[] args) {
        DebugConfig.PRINT_LOG = true;
//        DebugConfig.LOG_PATH="D://LogPath";//默认在D盘根目录

 //==========================云库企业库操作==========================//
        mEntLibManger = new EntLibManager(UESRNAME, PASSWORD, CLIENT_ID, CLIENT_SECRET);
//        //获取认证
        mEntLibManger.accessToken(true);
        //创建云库
//        deserializeReturn(mEntLibManger.create("city2", 10, "city2", "test lib"));
        //获取库列表
//        deserializeReturn(mEntLibManger.getLibList());
        //获取库授权
//        deserializeReturn(mEntLibManger.bind(146540,"",""));
        //取消库授权
//        deserializeReturn(mEntLibManger.unBind("9affb8f78fd5914a7218d7561db6ddec"));
        //获取当前所有可用角色
//        deserializeReturn(mEntLibManger.getRoles());

        //添加库成员
//        deserializeReturn(mEntLibManger.addMembers(150998,2892,new int[]{4}));
        //获取某一个库的成员
//        deserializeReturn(mEntLibManger.getMembers(0, 2, 32662));

        //批量修改单库中成员角色
//        deserializeReturn(mEntLibManger.setMemberRole(150998,2894,new int[]{4}));

        //从库中删除成员
//        deserializeReturn(mEntLibManger.delMember(150998,new int[]{4}));

        //获取某一个企业分组列表
//        deserializeReturn(mEntLibManger.getGroups(32657));

        //库上添加分组
//        deserializeReturn(mEntLibManger.addGroup(150998,4448,2892));
        //库上删除分组
//        deserializeReturn(mEntLibManger.delGroup(150998,4448));
        //设置分组上的角色
//        deserializeReturn(mEntLibManger.setGroupRole(150998,4448,2894));

        //获取库中文件
//        deserializeReturn(mEntLibManger.getFileList("9affb8f78fd5914a7218d7561db6ddec", "0af31f7a64bff5d6ed2abf0bb7da1d6b",(int)Util.getUnixDateline(), 0, ""));

        //获取更新列表
//        deserializeReturn(mEntLibManger.getUpdateList("9affb8f78fd5914a7218d7561db6ddec", "0af31f7a64bff5d6ed2abf0bb7da1d6b", (int) Util.getUnixDateline(), false, 0));

        //获取文件(夹)信息
//        deserializeReturn(mEntLibManger.getFileInfo("9affb8f78fd5914a7218d7561db6ddec", "0af31f7a64bff5d6ed2abf0bb7da1d6b",(int) Util.getUnixDateline(),"test"));
        //创建文件夹
//        deserializeReturn(mEntLibManger.createFolder("9affb8f78fd5914a7218d7561db6ddec", "0af31f7a64bff5d6ed2abf0bb7da1d6b",(int) Util.getUnixDateline(),"test","Brandon"));
        //上传文件 文件不得超过50MB
//        deserializeReturn(mEntLibManger.createFile("9affb8f78fd5914a7218d7561db6ddec", "0af31f7a64bff5d6ed2abf0bb7da1d6b",(int) Util.getUnixDateline(),"test","Brandon","D:\\test.txt"));
        //删除文件
//        deserializeReturn(mEntLibManger.del("9affb8f78fd5914a7218d7561db6ddec", "0af31f7a64bff5d6ed2abf0bb7da1d6b",(int) Util.getUnixDateline(),"test","Brandon"));
        //移动文件
//        deserializeReturn(mEntLibManger.move("9affb8f78fd5914a7218d7561db6ddec", "0af31f7a64bff5d6ed2abf0bb7da1d6b",(int) Util.getUnixDateline(),"test","1/test","Brandon"));
        //文件连接
//        deserializeReturn(mEntLibManger.link("9affb8f78fd5914a7218d7561db6ddec", "0af31f7a64bff5d6ed2abf0bb7da1d6b", (int) Util.getUnixDateline(), "1/test"));
        //发送消息
//        deserializeReturn(mEntLibManger.sendmsg("9affb8f78fd5914a7218d7561db6ddec", "0af31f7a64bff5d6ed2abf0bb7da1d6b", (int) Util.getUnixDateline(), "msgTest", "msg", "", "", "Brandon"));


//==========================云库企业操作==========================//
        mEntManger = new EntManager(UESRNAME, PASSWORD, CLIENT_ID, CLIENT_SECRET);
        //获取认证
        mEntManger.accessToken(true);
//获取角色
//        deserializeReturn(mEntManger.getRoles());
//获取分组
//        deserializeReturn(mEntManger.getGroups());
//获取成员
//        deserializeReturn(mEntManger.getMembers(0, 2));
    }

    /**
     * 解析返回内容
     *
     * @param result
     */
    private static void deserializeReturn(String result) {

        ReturnResult returnResult = ReturnResult.create(result);
        //如果是auth内容，则解析获取认证结果
        OauthData data = OauthData.create(returnResult.getResult());
        if (data != null) {

            if (returnResult.getStatusCode() == HttpStatus.SC_OK) {
                //成功的结果
//                System.out.println(mEntLibManger.getToken());
//                System.out.println(mEntManger.getToken());
                System.out.println(data.getToken()); //两者方法都可以拿到

            } else {
                //解析result中的内容
                System.out.println(data.getError());

            }
        }

        //复制到剪贴板
        Util.copyToClipboard(returnResult.getResult());
        System.out.println(returnResult.getResult());
    }


}

