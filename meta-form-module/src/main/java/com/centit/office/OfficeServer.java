package com.centit.office;


import com.alibaba.fastjson.JSONObject;
import com.centit.fileserver.client.FileClientImpl;
import com.centit.fileserver.client.po.FileInfo;
import com.centit.framework.appclient.AppSession;
import com.centit.framework.components.CodeRepositoryUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 后台Servlet
 */
@WebServlet(loadOnStartup = 1, urlPatterns = {"/OfficeServer"})
public class OfficeServer extends HttpServlet {
    private iMsgServer2015 MsgObj = new iMsgServer2015();
    private iDBManager2000 DbaObj = new iDBManager2000();
    String mOption;
    String mUserName;
    String mRecordID;
    String mFileName;
    String mFileType;
    String mCommand;
    String mInfo;
    String mTemplate;
    String mContent;
    String mRemoteFile;
    String mImageName;
    String mDataBase;
    byte[] mFileBody;
    int mFileSize = 0;
    String mFilePath; //取得服务器路径
    String mDirectory;
    String mDescript;
    String mFileDate;
    //数据库相关功能开始
    //打印控制
    private String mOfficePrints;
    private int mCopies;


    //    private DefaultFileClient fileClient;
    private HttpServletRequest request;
    /*@Resource
    private FileStore fileStore;
    @Resource
    FileClientImpl fileClient;*/


    //更新打印份数
    private boolean UpdataCopies(int mLeftCopies) {
        boolean mResult = true;
        //该函数可以把打印减少的次数记录到数据库
        //根据自己的系统进行扩展该功能
        return mResult;
    }

    public static String TransactSQLInjection(String str) {
        return str.replaceAll(".*([';]+|(--)+).*", " ");
    }

    //////////////////////////////////数据库打开保存文档/////////////////////////////////////////////
    //调出文档，将文档内容保存在mFileBody里，以便进行打包
    private boolean LoadFile() {
        boolean mResult = false;
        String Sql = "SELECT FileBody,FileSize FROM Document_File WHERE RecordID='" + mRecordID + "'";
        try {
            if (DbaObj.OpenConnection()) {
                try {
                    ResultSet result = DbaObj.ExecuteQuery(Sql);
                    if (result.next()) {
                        try {
                            mFileSize = result.getInt("FileSize");
                            GetAtBlob(result.getBlob("FileBody"), mFileSize);
                            mResult = true;
                        } catch (IOException ex) {
                            System.out.println(ex.toString());
                        }
                    }
                    result.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    mResult = false;
                }
            }
        } finally {
            DbaObj.CloseConnection();
        }
        return (mResult);
    }

    private void LoadFileMySql() {
        this.mRecordID = TransactSQLInjection(this.mRecordID);
        String Sql = "SELECT FileBody,FileSize FROM Document_File WHERE RecordID='" + this.mRecordID + "'";
        try {
            if (this.DbaObj.OpenConnection())
                try {
                    ResultSet result = this.DbaObj.ExecuteQuery(Sql);
                    if (result.next()) {
                        try {
                            this.mFileBody = result.getBytes("FileBody");
                            if (result.wasNull())
                                this.mFileBody = null;
                        } catch (Exception ex) {
                            System.out.println(ex.toString());
                        }
                    }
                    result.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
        } finally {
            this.DbaObj.CloseConnection();
        }
    }

    private boolean SaveFileMySql() {
        this.mRecordID = TransactSQLInjection(this.mRecordID);
        boolean mResult = false;
        int iFileId = -1;
        String Sql = "SELECT * FROM Document_File WHERE RecordID='" + this.mRecordID + "'";
        try {
            if (this.DbaObj.OpenConnection()) {
                try {
                    ResultSet result = this.DbaObj.ExecuteQuery(Sql);
                    if (result.next()) {
                        Sql = "update Document_File set RecordID=?,FileName=?,FileType=?,FileSize=?,FileDate=?,FileBody=?,FilePath=?,UserName=?,Descript=? WHERE RecordID='" + this.mRecordID + "'";
                    } else {
                        Sql = "insert into Document_File (RecordID,FileName,FileType,FileSize,FileDate,FileBody,FilePath,UserName,Descript) values (?,?,?,?,?,?,?,?,? )";
                    }
                    result.close();
                } catch (SQLException e) {
                    System.out.println(e.toString());
                    mResult = false;
                }
                PreparedStatement prestmt = null;
                try {
                    prestmt = this.DbaObj.Conn.prepareStatement(Sql);
                    prestmt.setString(0, this.mRecordID);
                    prestmt.setString(0, this.mRecordID);
                    prestmt.setString(1, this.mRecordID);
                    prestmt.setString(2, this.mFileName);
                    prestmt.setString(3, this.mFileType);
                    prestmt.setInt(4, this.mFileSize);
                    prestmt.setString(5, this.mFileDate);
                    prestmt.setBytes(6, this.mFileBody);
                    prestmt.setString(7, this.mFilePath);
                    prestmt.setString(8, this.mUserName);
                    prestmt.setString(9, this.mDescript);
                    prestmt.execute();
                    prestmt.close();
                    mResult = true;
                } catch (SQLException e) {
                    System.out.println(e.toString());
                    mResult = false;
                }
            }
        } finally {
            this.DbaObj.CloseConnection();
        }
        return mResult;
    }


    /*//保存文档，如果文档存在，则覆盖，不存在，则添加
    private boolean SaveFile() {
        boolean mResult = false;
        int iFileId = -1;
        String Sql = "SELECT * FROM Document_File WHERE RecordID='" + mRecordID + "'";
        try {
            if (DbaObj.OpenConnection()) {
                try {
                    ResultSet result = DbaObj.ExecuteQuery(Sql);
                    if (result.next()) {
                        Sql = "update Document_File set FileID=?,RecordID=?,FileName=?,FileType=?,FileSize=?,FileDate=?,FileBody=EMPTY_BLOB(),FilePath=?,UserName=?,Descript=? WHERE RecordID='" + mRecordID + "'";
                        iFileId = result.getInt("FileId");
                    } else {
                        Sql = "insert into Document_File (FileID,RecordID,FileName,FileType,FileSize,FileDate,FileBody,FilePath,UserName,Descript) values (?,?,?,?,?,?,EMPTY_BLOB(),?,?,? )";
                        iFileId = DbaObj.GetMaxID("Document_File", "FileId");
                    }
                    result.close();
                } catch (SQLException e) {
                    System.out.println(e.toString());
                    mResult = false;
                }
                java.sql.PreparedStatement prestmt = null;
                try {
                    prestmt = DbaObj.Conn.prepareStatement(Sql);
                    prestmt.setInt(1, iFileId);
                    prestmt.setString(2, mRecordID);
                    prestmt.setString(3, mFileName);
                    prestmt.setString(4, mFileType);
                    prestmt.setInt(5, mFileSize);
                    prestmt.setDate(6, DbaObj.GetDate());
                    prestmt.setString(7, mFilePath);
                    prestmt.setString(8, mUserName);
                    prestmt.setString(9, mDescript); //"通用版本"
                    DbaObj.Conn.setAutoCommit(true);
                    prestmt.execute();
                    DbaObj.Conn.commit();
                    prestmt.close();
                    Statement stmt = null;
                    DbaObj.Conn.setAutoCommit(false);
                    stmt = DbaObj.Conn.createStatement();
                    OracleResultSet update = (OracleResultSet) stmt.executeQuery("select FileBody from Document_File where Fileid=" + String.valueOf(iFileId) + " for update");
                    if (update.next()) {
                        try {
                            PutAtBlob(((oracle.jdbc.OracleResultSet) update).getBLOB("FileBody"), mFileSize);
                        } catch (IOException e) {
                            System.out.println(e.toString());
                            mResult = false;
                        }
                    }
                    update.close();
                    stmt.close();
                    DbaObj.Conn.commit();
                    mFileBody = null;
                    mResult = true;
                } catch (SQLException e) {
                    System.out.println(e.toString());
                    mResult = false;
                }
            }
        } finally {
            DbaObj.CloseConnection();
        }
        return (mResult);
    }

    //向数据库写文档数据内容
    private void PutAtBlob(BLOB vField, int vSize) throws IOException {
        try {
            OutputStream outstream = vField.getBinaryOutputStream();
            outstream.write(mFileBody, 0, vSize);
            outstream.close();
        } catch (SQLException e) {
        }
    }*/

    //从数据库取文档数据内容
    private void GetAtBlob(Blob blob, int vSize) throws IOException {
        try {
            mFileBody = new byte[vSize];
            InputStream instream = blob.getBinaryStream();
            instream.read(mFileBody, 0, vSize);
            instream.close();
        } catch (SQLException e) {
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    //保存书签
    private boolean SaveBookMarks() {
        boolean mResult = false;
        String mBookMarkName;
        int mIndex;
        try {
            if (DbaObj.OpenConnection()) {
                try {
                    java.sql.PreparedStatement prestmt = null;
                    String Sql = "DELETE FROM Template_BookMarks Where RecordID='" + mTemplate + "'";
                    prestmt = DbaObj.Conn.prepareStatement(Sql);

                    prestmt.execute();
                    int getFieldCount = MsgObj.GetFieldCount();
                    prestmt.close();
                    for (mIndex = 0; mIndex < getFieldCount - 2; mIndex++) {
                        java.sql.PreparedStatement prestmtx = null;
                        mBookMarkName = MsgObj.GetFieldName(mIndex);
                        Sql = "insert into Template_BookMarks (RecordId,BookMarkName) values ('" + mTemplate + "','" + mBookMarkName + "')";
                        prestmtx = DbaObj.Conn.prepareStatement(Sql);
                        prestmtx.execute();
                        prestmtx.close();
                    }
                    mResult = true;
                } catch (SQLException e) {
                    System.out.println(e.toString());
                    mResult = false;
                }
            }
        } finally {
            DbaObj.CloseConnection();
        }
        return (mResult);
    }

    //装入书签
    private boolean LoadBookMarks() {
        boolean mResult = false;
        String Sql = " select b.BookMarkName,b.BookMarkText from Template_BookMarks a,BookMarks b where a.BookMarkname=b.BookMarkName and a.RecordID='" + mTemplate + "'";
        try {
            if (DbaObj.OpenConnection()) {
                try {
                    ResultSet result = DbaObj.ExecuteQuery(Sql);
                    while (result.next()) {
                        try {
                            //说明：我们测试程序把SQL语句直接写到替换标签内容
                            //实际使用中，这个标签内容是通过Sql语句得到的。
                            //生成SQL查询语句  result.getString("BookMarkText") & "条件"
                            //当前纪录号位 mRecordID
                            //BookMarkValue=生成SQL运行结果
                            String mBookMarkName = result.getString("BookMarkName");
                            String mBookMarkValue = result.getString("BookMarkText");
                            MsgObj.SetMsgByName(mBookMarkName, mBookMarkValue);
                        } catch (Exception ex) {
                            System.out.println(ex.toString());
                        }
                    }
                    result.close();
                    mResult = true;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    mResult = false;
                }
            }
        } finally {
            DbaObj.CloseConnection();
        }
        return (mResult);
    }

    //数据库相关功能结束
    // char[]转byte[]
    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mCommand = "";
        System.out.println("OfficeServer.service Start");
        System.out.println("OfficeServer.service --- request.getSession().getId() = " + request.getSession().getId());
        mFilePath = request.getSession().getServletContext().getRealPath("");       //取得服务器路径

        String htmlHttp = "";
        try {
            if (request.getMethod().equalsIgnoreCase("POST")) {//判断请求方式
                MsgObj.setSendType("JSON");
                MsgObj.Load(request); //解析请求

                mOption = MsgObj.GetMsgByName("OPTION");//请求参数
                mUserName = MsgObj.GetMsgByName("USERNAME");  //取得系统用户
                System.out.println("htmlHttp:" + htmlHttp);
                if (mOption.equalsIgnoreCase("LOADFILE")) {
                    mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
                    mFileName = MsgObj.GetMsgByName("FILENAME");//取得文档名称
                    mDataBase = MsgObj.GetMsgByName("DATABASE");//数据库类型
                    String ExtParam = MsgObj.GetMsgByName("EXTPARAM");//数据库类型
                    if (mDataBase.equals("MYSQL")) {
                        this.MsgObj.MsgTextClear();
                        LoadFileMySql();
                        this.MsgObj.MsgFileBody(this.mFileBody);
                        if (this.mFileBody == null) {
                            System.out.println(this.mFileName + "文档加载失败");
                        } else {
                            System.out.println(this.mFileName + "文档加载成功");
                        }
                    } else if (mDataBase.equals("ORACLE")) {
                        this.MsgObj.MsgTextClear();
                        LoadFile();
                        this.MsgObj.MsgFileBody(this.mFileBody);
                        if (this.mFileBody == null) {
                            System.out.println(this.mFileName + "文档加载失败");
                        } else {
                            System.out.println(this.mFileName + "文档加载成功");
                        }
                    } else {
                        System.out.println("ExtParam:" + ExtParam);
                        if (MsgObj.MsgFileLoad(mFilePath + "Document" + File.separator + mFileName)) {
                            MsgObj.MsgTextClear();
                            System.out.println("文件路径：" + mFilePath + "Document" + File.separator + mFileName);
                            //MsgObj.SetMsgByName("string1", "测试 内容");
                            System.out.println(mFileName + "文档已经加载");
                        }
                    }

                } else if (mOption.equalsIgnoreCase("SAVEFILE")) {
                    System.out.println(mRecordID + "文档上传中");
                    mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
                    mFileName = MsgObj.GetMsgByName("FILENAME");//取得文档名称
                    mDataBase = MsgObj.GetMsgByName("DATABASE");//数据库类型


                    // 金格自带的，将文件保存到数据库
                    if (mDataBase.equals("MYSQL")) {
                        mFileBody = this.MsgObj.MsgFileBody();
                        mFileSize = this.MsgObj.MsgFileSize();
                        mFileDate = this.DbaObj.GetDateTime();
                        this.MsgObj.MsgTextClear();
                        if (SaveFileMySql()) {
                            System.out.println("文档mRecordID" + this.mRecordID);
                        }
                    } else if (mDataBase.equals("ORACLE")) {
                        mFileBody = this.MsgObj.MsgFileBody();
                        mFileSize = this.MsgObj.MsgFileSize();
                        mFileDate = this.DbaObj.GetDateTime();
                        this.MsgObj.MsgTextClear();
                        if (SaveFileMySql()) {
                            System.out.println("文档mRecordID" + this.mRecordID);
                        }
                    } else {
                        String outputFile = mFilePath + "Document" + File.separator + mFileName;
                        System.out.println("mFilePath+mFileName = " + outputFile);
                        MsgObj.MsgTextClear();//清除文本信息
                        if (MsgObj.MsgFileSave(outputFile)) {
                            System.out.println(mFileName + " 文档已经保存成功");
                        }

                        // 通过文件服务器，文件上传
                        FileClientImpl fileClient = new FileClientImpl();
                        String fileServerUrl = CodeRepositoryUtil.getSysConfigValue("fileserver.url");
                        AppSession appSession = new AppSession(fileServerUrl);//配置文件文件服务器路径
                        fileClient.setAppSession(appSession);
                        fileClient.setFileServerExportUrl(fileServerUrl);
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFileName(mFileName);
                        File f = new File(outputFile);
                        FileInfo uploadFile = fileClient.uploadFile(fileInfo, f);
                        System.out.println("通过文件服务器，文件上传  文件名：" + uploadFile.getFileName() + "  fileServer： " + fileServerUrl + "  fileId: " + uploadFile.getFileId() + "  fileMd5: " + uploadFile.getFileMd5());
                        response.setHeader("fileId", uploadFile.getFileId());

                    }
                } else if (mOption.equalsIgnoreCase("SAVEPDF")) {
                    System.out.println(mRecordID + "文档转PDF");
                    mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
                    mFileName = MsgObj.GetMsgByName("FILENAME");//取得文档名称
                    MsgObj.MsgTextClear();//清除文本信息
                    mFilePath = mFilePath + File.separator + "Document";
                    MsgObj.MakeDirectory(mFilePath);
                    if (MsgObj.MsgFileSave(mFilePath + File.separator + mFileName)) {
                        System.out.println(mRecordID + "文档已经转换成功");
                    }

                } else if (mOption.equalsIgnoreCase("SAVEHTML")) {
                    System.out.println(mRecordID + "文档上传中");
                    mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
                    mFileName = MsgObj.GetMsgByName("FILENAME");//取得文档名称
                    mDirectory = MsgObj.GetMsgByName("DIRECTORY"); //获取目录名称
                    MsgObj.MsgTextClear();//清除文本信息
                    System.out.println("mDirectory==" + mDirectory);
                    if (mDirectory.equalsIgnoreCase("")) {
                        mFilePath = mFilePath + File.separator + "HTML";
                    } else {
                        mFilePath = mFilePath + File.separator + "HTML" + File.separator + mDirectory;
                        System.out.println("mFilePath===" + mFilePath);
                    }
                    MsgObj.MakeDirectory(mFilePath);
                    System.out.println("开始MsgFileSave" + mFilePath);
                    if (MsgObj.MsgFileSave(mFilePath + File.separator + mFileName)) {
                        System.out.println(mRecordID + "文档已经保存成功");
                    }
                } else if (mOption.equalsIgnoreCase("SAVEIMAGE")) {                     //下面的代码为将OFFICE存为HTML图片页面
                    mFileName = MsgObj.GetMsgByName("FILENAME");                        //取得文件名称
                    mDirectory = MsgObj.GetMsgByName("DIRECTORY");                      //取得目录名称
                    MsgObj.MsgTextClear();
                    if (mDirectory.trim().equalsIgnoreCase("")) {
                        mFilePath = mFilePath + File.separator + "HTMLIMAGE";
                    } else {
                        mFilePath = mFilePath + File.separator + "HTMLIMAGE" + File.separator + mDirectory;
                    }
                    MsgObj.MakeDirectory(mFilePath);                                    //创建路径
                    if (MsgObj.MsgFileSave(mFilePath + File.separator + mFileName)) {             //保存HTML文件
                        System.out.println(mRecordID + "文档已经保存成功");               //设置状态信息
                    } else {
                        System.out.println("保存HTML图片失败!");
                    }
                } else if (mOption.equalsIgnoreCase("LOADTEMPLATE")) {                  //下面的代码为打开服务器数据库里的模板文件
                    mTemplate = MsgObj.GetMsgByName("TEMPLATE");                        //取得模板文档类型
                    //本段处理是否调用文档时打开模版，还是套用模版时打开模版。
                    MsgObj.MsgTextClear();//清除文本信息
                    String filePath = mFilePath + "Document" + File.separator + mTemplate;
                    System.out.println("加载模板 LOADTEMPLATE" + filePath);
                    if (MsgObj.MsgFileLoad(filePath)) { //从服务器文件夹中调入模板文档
                        System.out.println(mTemplate + "文档已经转换成功");                              //清除错误信息
                    }

                } else if (mOption.equalsIgnoreCase("SAVETEMPLATE")) {                  //下面的代码为打开服务器数据库里的模板文件
                    mTemplate = MsgObj.GetMsgByName("TEMPLATE");//取得模板名称
                    System.out.println(mTemplate + "模板上传中");
                    MsgObj.MsgTextClear();//清除文本信息
                    if (MsgObj.MsgFileSave(mFilePath + "Document" + File.separator + mTemplate)) {
                        System.out.println(mTemplate + "模板保存成功");
                    }
                } else if (mOption.equalsIgnoreCase("INSERTFILE")) {                  //下面的代码为打开服务器数据库里的模板文件
                    System.out.println("进入INSERTFILE");
                    mTemplate = MsgObj.GetMsgByName("TEMPLATE");//取得模板名称
                    System.out.println(mTemplate + "模板上传中");
                    MsgObj.MsgTextClear();//清除文本信息
                    if (MsgObj.MsgFileLoad(mFilePath + "Document" + File.separator + mTemplate)) {
                        System.out.println(mRecordID + "模板保存成功");
                    }
                } else if (mOption.equalsIgnoreCase("LOADBOOKMARKS")) {                 //下面的代码为取得文档标签
                    mTemplate = MsgObj.GetMsgByName("RECORDID");                        //取得模板编号
                    mFileName = MsgObj.GetMsgByName("FILENAME");                        //取得文档名称
                    mFileType = MsgObj.GetMsgByName("FILETYPE");                        //取得文档类型
                    MsgObj.MsgTextClear();
                    System.out.println("mTemplate:=" + mTemplate);
                    if (LoadBookMarks()) {
                        System.out.println("获取书签信息成功");                                          //清除错误信息
                    } else {
                        System.out.println("获取书签信息失败");                            //设置错误信息
                    }
                } else if (mOption.equalsIgnoreCase("GETFILE")) {
                    System.out.println("开始下载文档");
                    mRecordID = MsgObj.GetMsgByName("RECORDID");                 //取得文档编号
                    mRemoteFile = MsgObj.GetMsgByName("REMOTEFILE");                //取得远程文件名称
                    MsgObj.MsgTextClear();//清除文本信息
                    System.out.println(mFilePath + "Document" + File.separator + mRemoteFile);
                    if (MsgObj.MsgFileLoad(mFilePath + "Document" + File.separator + mRemoteFile)) {
                        System.out.println(mRemoteFile + "文档已经下载");
                    }
                } else if (mOption.equalsIgnoreCase("PUTFILE")) {
                    System.out.println("开始下载文档");
                    mRemoteFile = MsgObj.GetMsgByName("REMOTEFILE");                //取得远程文件名称
                    MsgObj.MsgTextClear();//清除文本信息
                    System.out.println(mFilePath + "Document" + File.separator + mRemoteFile);
                    if (MsgObj.MsgFileSave(mFilePath + "Document" + File.separator + mRemoteFile)) {
                        System.out.println(mRemoteFile + "文档已经上传成功");
                    }
                } else if (mOption.equalsIgnoreCase("DELFILE")) {
                    mRemoteFile = MsgObj.GetMsgByName("REMOTEFILE");                //取得远程文件名称
                    MsgObj.MsgTextClear();                                    //清除文本信息
                    if (MsgObj.DelFile(mFilePath + "Document" + File.separator + mRemoteFile)) {                            //删除文档
                        System.out.println("删除文件成功");
                    } else {
                        System.out.println("删除文件失败");                            //设置错误信息
                    }
                } else if (mOption.equalsIgnoreCase("SENDMESSAGE")) {                   //下面的代码为Web页面请求信息[扩展接口]
                    mCommand = MsgObj.GetMsgByName("COMMAND");                          //取得自定义的操作类型
                    mOfficePrints = MsgObj.GetMsgByName("OFFICEPRINTS");                //取得Office文档的打印次数
                    mUserName = MsgObj.GetMsgByName("USERNAME");
                    mInfo = MsgObj.GetMsgByName("TESTINFO");
                    MsgObj.MsgTextClear();
                    if (mCommand == null) {
                        mCommand = "INPORTTEXT";
                    }
                    if (mCommand.equalsIgnoreCase("COPIES")) {                     //打印份数控制功能
                        System.out.println("mOfficePrints:" + mOfficePrints);
                        mCopies = Integer.parseInt(mOfficePrints);                        //获得客户需要打印的份数
                        System.out.println("mCopies:" + mCopies);
                        if (mCopies <= 2) {                                               //比较打印份数，拟定该文档允许打印的总数为2份，注：可以在数据库中设置好文档允许打印的份数
                            if (UpdataCopies(2 - mCopies)) {                                //更新打印份数
                                MsgObj.SetMsgByName("STATUS", "1");                           //设置状态信息，允许打印
                                System.out.println("在打印范围内开始打印");
                            }
                        } else {
                            MsgObj.SetMsgByName("STATUS", "0");                             //不允许打印
                            System.out.println("超过打印限度不允许打印");
                        }
                    } else if (mCommand.equalsIgnoreCase("INPORTTEXT")) {
                        mInfo = "服务器端收到客户端传来的信息：“" + mInfo + "” | ";
                        mInfo = mInfo + "当前服务器时间：" + DbaObj.GetDateTime();        //组合返回给客户端的信息
                        MsgObj.SetMsgByName("RETURNINFO", mInfo);
                        System.out.println("发送数据到前台名为:" + mInfo);
                    }

                } else if (mOption.equalsIgnoreCase("SAVEBOOKMARKS")) {                 //下面的代码为取得标签文档内容
                    mTemplate = MsgObj.GetMsgByName("TEMPLATE");                        //取得模板编号
                    if (SaveBookMarks()) {
                        System.out.println("保存书签信息成功!");                                              //清除错误信息
                    } else {
                        System.out.println("保存书签信息失败!");                             //设置错误信息
                    }
                    MsgObj.MsgTextClear();                                              //清除文本信息
                    MsgObj.ListClear();
                } else if (mOption.equalsIgnoreCase("INSERTIMAGE")) {                   //下面的代码为插入服务器图片
                    mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
                    mImageName = MsgObj.GetMsgByName("IMAGENAME");                      //图片名
                    mFilePath = mFilePath + "Document" + File.separator + mImageName;                //图片在服务器的完整路径
                    MsgObj.MsgTextClear();
                    if (MsgObj.MsgFileLoad(mFilePath)) {                                //调入图片
                        System.out.println("插入图片成功!");
                    } else {
                        System.out.println("插入图片失败!");                                    //设置错误信息
                    }
                }
                System.out.println("SendPackage");

                int codec = 0;
                if (htmlHttp == "BASE64")
                    codec = 1;
                MsgObj.Send(response, codec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("OfficeServer.service End");
    }

    private void uploadDocFile() throws IOException {
        String saveUrl = "";
        String extName = ".doc";
        File pathFile = new File(mFilePath + "Document");
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        //重写方法，调用接口保存文件
//                    String recordID = this.mRecordID;
        byte[] fileBody = this.MsgObj.MsgFileBody();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBody);//将文件二进制数组转换为流
        FileOutputStream outputStream = new FileOutputStream(mFilePath + "Document" + File.separator + mFileName + extName);
        byte[] by = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(by)) != -1) {
            outputStream.write(by, 0, len);
        }
        outputStream.close();
        inputStream.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("multipart/form-data"));

        Map<String, Object> form = new LinkedMultiValueMap();
        form.put("file", new FileSystemResource(mFilePath + "Document" + File.separator + mFileName + extName));
        String post = new RestTemplate().postForEntity(saveUrl, new HttpEntity<>(form, headers), String.class).getBody();
        JSONObject postJson = JSONObject.parseObject(post);
        if (0 == (int) postJson.get("code")) {
            String postJsonStr = postJson.get("data").toString();
            JSONObject jsonObject = JSONObject.parseObject(postJsonStr);
            /*SysDocumentModule sysDocumentModule = sysDocumentModuleService.getObjectById(mRecordID);
            if (null != sysDocumentModule) {
                sysDocumentModule.setFileId(jsonObject.getString("fileId"));
                sysDocumentModule.setFileName(mFileName);
                sysDocumentModuleService.updateObject(sysDocumentModule);
            }*/
            File file = new File(mFilePath + "Document" + File.separator + mFileName + extName);
            if (file.exists()) {
                file.delete(); //删除文件
            }
        }
    }

    /*private JSONObject SaveFile() {
        boolean mResult = false;
        JSONObject jsonObject = new JSONObject();
        String fileId = "";
        try {
            fileClient = FileUploadUtil.getFileClient();
            FileStoreInfo fileStoreInfo = new FileStoreInfo();
            String fileName = this.mFileName;
            fileStoreInfo.setFileName(fileName);//设置文件名称
            fileStoreInfo.setFileType(mFileType);//设置文件类型
            fileStoreInfo.setOptTag("king");//标记金格业务文档
            //上传文件服务器(转换前的文件上传到服务器，可选择根据需要)
            String path = request.getSession().getServletContext().getRealPath("")
                    + File.separator + "ofd" + File.separator + "King" + File.separator;
            FileUploadUtil.CreateMultilayerFile(path);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(path + fileName);
                out.write(this.mFileBody);
                out.close();
                File file = new File(path + fileName);
                fileStoreInfo = fileClient.uploadFile(fileClient.getHttpClient(), fileStoreInfo, file);

                fileId = fileStoreInfo.getFileId();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            mResult = true;
        } finally {
            jsonObject.put("mResult", mResult);
            jsonObject.put("recordid", fileId);
            jsonObject.put("fileName", this.mFileName);
        }
        return jsonObject;
    }

    public FileStoreInfo uploadFile(CloseableHttpClient httpClient, FileStoreInfo fsi, File file) throws IOException {
        this.appSession.checkAccessToken(httpClient);
        String jsonStr = HttpExecutor.fileUpload(HttpExecutorContext.create(httpClient), this.appSession.completeQueryUrl("/service/upload/file"), JSON.parseObject(JSON.toJSONString(fsi)), file);
        HttpReceiveJSON resJson = null;

        try {
            resJson = HttpReceiveJSON.valueOfJson(jsonStr);
        } catch (Exception var7) {
            this.logger.error("解析返回json串失败", var7);
            throw new ObjectException(jsonStr, "解析返回json串失败");
        }

        if (resJson == null) {
            throw new ObjectException(jsonStr, "请求失败！");
        } else {
            return (FileStoreInfo)resJson.getDataAsObject(FileStoreInfo.class);
        }
    }*/
}
