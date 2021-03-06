package com.yunkuent.sdk;

import com.yunkuent.sdk.upload.UploadCallBack;
import com.yunkuent.sdk.utils.Util;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Brandon on 2014/8/14.
 */
public class EntFileManager extends SignAbility implements HostConfig {

    private static final int UPLOAD_LIMIT_SIZE = 52428800;
    private static final String URL_API_FILELIST = LIB_HOST + "/1/file/ls";
    private static final String URL_API_UPDATE_LIST = LIB_HOST + "/1/file/updates";
    private static final String URL_API_FILE_INFO = LIB_HOST + "/1/file/info";
    private static final String URL_API_CREATE_FOLDER = LIB_HOST + "/1/file/create_folder";
    private static final String URL_API_CREATE_FILE = LIB_HOST + "/1/file/create_file";
    private static final String URL_API_DEL_FILE = LIB_HOST + "/1/file/del";
    private static final String URL_API_MOVE_FILE = LIB_HOST + "/1/file/move";
    private static final String URL_API_LINK_FILE = LIB_HOST + "/1/file/link";
    private static final String URL_API_SENDMSG = LIB_HOST + "/1/file/sendmsg";
    private static final String URL_API_GET_LINK = LIB_HOST + "/1/file/links";
    private static final String URL_API_UPDATE_COUNT = LIB_HOST + "/1/file/updates_count";
    private static final String URL_API_GET_SERVER_SITE = LIB_HOST + "/1/file/servers";
    private static final String URL_API_CREATE_FILE_BY_URL = LIB_HOST + "/1/file/create_file_by_url";
    private static final String URL_API_UPLOAD_SERVERS = LIB_HOST + "/1/file/upload_servers";


    private String mOrgClientId;

    public EntFileManager(String orgClientId, String orgClientSecret) {
        mOrgClientId = orgClientId;
        mClientSecret = orgClientSecret;
    }

    /**
     * 获取根目录文件列表
     *
     * @return
     */
    public String getFileList() {
        return this.getFileList("", 0, 100, false);
    }

    /**
     * 获取文件列表
     *
     * @param fullPath 路径, 空字符串表示根目录
     * @return
     */
    public String getFileList(String fullPath) {
        return this.getFileList(fullPath, 0, 100, false);
    }

    /**
     * 获取文件列表
     *
     * @param fullPath 路径, 空字符串表示根目录
     * @param start 起始下标, 分页显示
     * @param size 返回文件/文件夹数量限制
     * @param dirOnly 只返回文件夹
     * @return
     */
    public String getFileList(String fullPath, int start, int size, boolean dirOnly) {
        String method = "GET";
        String url = URL_API_FILELIST;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("fullpath", fullPath));
        params.add(new BasicNameValuePair("start", start + ""));
        params.add(new BasicNameValuePair("size", size + ""));
        if (dirOnly) {
            params.add(new BasicNameValuePair("dir", "1"));
        }
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);

    }

    /**
     * 获取更新列表
     *
     * @param isCompare
     * @param fetchDateline
     * @return
     */
    public String getUpdateList(boolean isCompare, long fetchDateline) {
        String method = "GET";
        String url = URL_API_UPDATE_LIST;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        if (isCompare) {
            params.add(new BasicNameValuePair("mode", "compare"));
        }
        params.add(new BasicNameValuePair("fetch_dateline", fetchDateline + ""));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }

    /**
     * 获取文件信息
     *
     * @param fullPath
     * @param net
     * @return
     */
    public String getFileInfo(String fullPath, NetType net) {
        String method = "GET";
        String url = URL_API_FILE_INFO;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("fullpath", fullPath));
        switch (net) {
            case DEFAULT:
                break;
            case IN:
                params.add(new BasicNameValuePair("net", net.name().toLowerCase()));
                break;
        }
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }

    /**
     * 创建文件夹
     *
     * @param fullPath
     * @param opName
     * @return
     */
    public String createFolder(String fullPath, String opName) {
        String method = "POST";
        String url = URL_API_CREATE_FOLDER;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("fullpath", fullPath));
        params.add(new BasicNameValuePair("op_name", opName));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }

    /**
     * 通过文件流上传
     *
     * @param fullPath
     * @param opName
     * @param stream
     * @return
     */
    public String createFile(String fullPath, String opName, FileInputStream stream) {
        try {
            if (stream.available() > UPLOAD_LIMIT_SIZE) {
                LogPrint.print("文件大小超过50MB");
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = Util.getNameFromPath(fullPath);

        try {
            long dateline = Util.getUnixDateline();

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
            params.add(new BasicNameValuePair("dateline", dateline + ""));
            params.add(new BasicNameValuePair("fullpath", fullPath));
            params.add(new BasicNameValuePair("op_name", opName));
            params.add(new BasicNameValuePair("filefield", "file"));

            MsMultiPartFormData multipart = new MsMultiPartFormData(URL_API_CREATE_FILE, "UTF-8");
            multipart.addFormField("org_client_id", mOrgClientId);
            multipart.addFormField("dateline", dateline + "");
            multipart.addFormField("fullpath", fullPath);
            multipart.addFormField("op_name", opName);
            multipart.addFormField("filefield", "file");
            multipart.addFormField("sign", generateSign(paramSorted(params)));

            multipart.addFilePart("file", stream, fileName);

            return multipart.finish();

        } catch (IOException ex) {
            System.err.println(ex);
        }
        return "";
    }


    /**
     * @param fullPath
     * @param opName
     * @param opId
     * @param localFilePath
     * @param overWrite
     * @param callBack
     */
    public UploadRunnable uploadByBlock(String fullPath, String opName, int opId, String localFilePath,
                                        boolean overWrite, UploadCallBack callBack) {
        UploadRunnable uploadRunnable = new UploadRunnable(URL_API_CREATE_FILE, localFilePath, fullPath, opName, opId, mOrgClientId, Util.getUnixDateline(), callBack, mClientSecret, overWrite);
        Thread thread = new Thread(uploadRunnable);
        thread.start();
        return uploadRunnable;
    }

    /**
     * 通过本地路径上传
     *
     * @param fullPath
     * @param opName
     * @param localPath
     * @return
     */
    public String createFile(String fullPath, String opName, String localPath) {
        File file = new File(localPath.trim());
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                return createFile(fullPath, opName, inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            LogPrint.print("file not exist");
        }

        return "";

    }

    /**
     * 删除文件
     *
     * @param fullPaths
     * @param opName
     * @return
     */
    public String del(String fullPaths, String opName) {
        String method = "POST";
        String url = URL_API_DEL_FILE;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("fullpaths", fullPaths));
        params.add(new BasicNameValuePair("op_name", opName));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }

    /**
     * 移动文件
     *
     * @param fullPath
     * @param destFullPath
     * @param opName
     * @return
     */
    public String move(String fullPath, String destFullPath, String opName) {
        String method = "POST";
        String url = URL_API_MOVE_FILE;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("fullpath", fullPath));
        params.add(new BasicNameValuePair("dest_fullpath", destFullPath));
        params.add(new BasicNameValuePair("op_name", opName));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }

    /**
     * 获取文件链接
     *
     * @param fullPath
     * @param deadline
     * @param authType
     * @param password
     * @return
     */
    public String link(String fullPath, int deadline, AuthType authType, String password) {
        String method = "POST";
        String url = URL_API_LINK_FILE;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("fullpath", fullPath));

        if (deadline != 0) {
            params.add(new BasicNameValuePair("deadline", deadline + ""));
        }

        if (!authType.equals(AuthType.DEFAULT)) {
            params.add(new BasicNameValuePair("auth", authType.toString().toLowerCase()));
        }
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }


    /**
     * 发送消息
     *
     * @param title
     * @param text
     * @param image
     * @param linkUrl
     * @param opName
     * @return
     */
    public String sendmsg(String title, String text, String image, String linkUrl, String opName) {
        String method = "POST";
        String url = URL_API_SENDMSG;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("text", text));
        params.add(new BasicNameValuePair("image", image));
        params.add(new BasicNameValuePair("url", linkUrl));
        params.add(new BasicNameValuePair("op_name", opName));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }


    /**
     * 获取当前库所有外链
     *
     * @return
     */
    public String links(boolean fileOnly) {
        String method = "GET";
        String url = URL_API_GET_LINK;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        if (fileOnly) {
            params.add(new BasicNameValuePair("file", "1"));
        }
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }


    /**
     * 文件更新数量
     *
     * @param beginDateline
     * @param endDateline
     * @param showDelete
     * @return
     */
    public String getUpdateCounts(long beginDateline, long endDateline, boolean showDelete) {
        String method = "GET";
        String url = URL_API_UPDATE_COUNT;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("begin_dateline", beginDateline + ""));
        params.add(new BasicNameValuePair("end_dateline", endDateline + ""));
        params.add(new BasicNameValuePair("showdel", (showDelete ? 1 : 0) + ""));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }

    /**
     * 通过链接上传文件
     *
     * @param fullPath
     * @param opId
     * @param opName
     * @param overwrite
     * @param fileUrl
     * @return
     */
    public String createFileByUrl(String fullPath, int opId, String opName, boolean overwrite, String fileUrl) {
        String method = "POST";
        String url = URL_API_CREATE_FILE_BY_URL;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("fullpath", fullPath));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        if (opId > 0) {
            params.add(new BasicNameValuePair("op_id", opId + ""));
        } else {
            params.add(new BasicNameValuePair("op_name", opName + ""));
        }
        params.add(new BasicNameValuePair("overwrite", (overwrite ? 1 : 0) + ""));
        params.add(new BasicNameValuePair("url", fileUrl));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }

    /**
     * 获取上传地址
     *
     * (支持50MB以上文件的上传)
     *
     * @return
     */
    public String getUploadServers(){
        String method = "GET";
        String url = URL_API_UPLOAD_SERVERS;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }


    public String getServerSite(String type) {
        String method = "POST";
        String url = URL_API_GET_SERVER_SITE;
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("org_client_id", mOrgClientId));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("dateline", Util.getUnixDateline() + ""));
        params.add(new BasicNameValuePair("sign", generateSign(paramSorted(params))));
        return NetConnection.sendRequest(url, method, params, null);
    }


    /**
     * 复制一个EntFileManager对象
     *
     * @return
     */
    public EntFileManager clone() {
        return new EntFileManager(mOrgClientId, mClientSecret);
    }

    public enum AuthType {
        DEFAULT,
        PREVIEW,
        DOWNLOAD,
        UPLOAD
    }

    public enum NetType {
        DEFAULT,
        IN
    }

}
