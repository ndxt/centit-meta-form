package com.centit.office;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class iMsgServer2015 {
    private Hashtable<String, String> saveFormParam = new Hashtable<String, String>();  //保存form表单数据
    private Hashtable<String, String> sendFormParam = new Hashtable<String, String>();  //保存form表单数据
    private List list = new ArrayList<String>();   //保存书签值
    private InputStream fileContentStream;
    private String fileName = "";
    public byte[] mFileBody = null;
    private String sendType = "";
    private int FFileSize = 0;

    private static final String MsgError = "404"; //设置常量404，说明没有找到对应的文档

    private String ReturnValue;
    private static final int BUFFER_SIZE = 1024;
    private static final int BUFFER_SIZE2 = 4096;


    public String getReturnValue() {
        return ReturnValue;
    }

    public void setReturnValue(String returnValue) {
        ReturnValue = returnValue;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    /**
     * @throws FileUploadException
     * @throws IOException
     * @deprecated:后台类解析接口
     * @time:2015-01-09
     */
    public void Load(HttpServletRequest request) throws FileUploadException, IOException {
        request.setCharacterEncoding("gb2312");
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
        //DefaultFileItemFactory diskFileItemFactory = new DefaultFileItemFactory();
        //DiskFileUpload fileUpload = new DiskFileUpload(diskFileItemFactory);
        List fileList = fileUpload.parseRequest(request);
        //List<FileItem> fileList =  fileUpload.parseRequest(request);
        //Iterator iter = fileList.iterator();
        System.out.println("iMsgServer2015.Load解析客户端参数");
        if (fileList != null && fileList.size() > 0) {
            for (int i = 0; i < fileList.size(); i++) {
                FileItem item = (FileItem) fileList.get(i);
                if (item.isFormField()) {
                    processFormField(item);
                } else {
                    processUploadedFile(item);
                }
            }
        }
    }

    /**
     * @param item:表单数据
     * @throws UnsupportedEncodingException
     * @deprecated：解析表达数据
     * @time:2015-01-09
     */
    public void processFormField(FileItem item) throws UnsupportedEncodingException {
        String fieldName = item.getFieldName();
        String fieldValue = "";
        fieldValue = item.getString("utf-8");
        if (this.sendType.equalsIgnoreCase("JSON")) {
//            JSONObject json = JSONObject.fromObject(fieldValue);
            JSONObject json = JSONObject.parseObject(fieldValue);
            Iterator iter = json.keySet().iterator();
            while (iter.hasNext()) {
                fieldName = (String) iter.next();
                fieldValue = json.getString(fieldName);
                saveFormParam.put(fieldName, fieldValue);
            }
            return;
        }
        saveFormParam.put(fieldName, fieldValue);
    }


    /**
     * @param item:文档数据
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @deprecated：解析文档数据
     * @time:2015-01-09
     */
    public void processUploadedFile(FileItem item) throws IOException {
        fileName = item.getName();
        if (fileName.indexOf("/") >= 0) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        } else if (fileName.indexOf("\\") >= 0) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        }
        fileContentStream = item.getInputStream();

    }

    /**
     * @param fieldName:参数名称
     * @deprecated：解析文档数据
     * @return：参数对于的值
     * @time:2015-01-09
     */

    public String GetMsgByName(String fieldName) {
        return saveFormParam.get(fieldName);
    }

    public String SetMsgByName(String fieldName, String nameValue) {
        return saveFormParam.put(fieldName, nameValue);
    }

    /**
     * 清除所有SetMsgByName所有内容
     *
     * @time:2015-01-09
     */
    public void MsgTextClear() {
        saveFormParam.clear();
    }

    /**
     * 清除所有List所有内容
     *
     * @time:2017-09-01
     */
    public void ListClear() {
        list.clear();
    }

    public int MsgFileSize() {
        return this.FFileSize;
    }


    //删除文件
    public boolean DelFile(String FileName) {
        File mFile = new File(FileName);
        if (mFile.exists()) {
            mFile.delete();
        } else {
            SetMsgByName("DelFileState", "失败");
            return false;
        }
        SetMsgByName("DelFileState", "成功");
        return true;
    }

    //saveFormParam数量
    public int GetFieldCount() {
        return saveFormParam.size();
    }

    //创建文件夹
    public boolean MakeDirectory(String FilePath) {
        File mFile = new File(FilePath);
        mFile.mkdirs();
        return (mFile.isDirectory());
    }


    //数据存放到list
    public String GetFieldName(int mIndex) {
        int tag = list.size();
        if (tag == 0) {
            saveFormParam.remove("OPTION");
            saveFormParam.remove("TEMPLATE");
            Iterator<String> iterator = saveFormParam.keySet().iterator();
            while (iterator.hasNext()) {
                String Key = iterator.next();
                list.add(Key);
            }
        }
        String BookMarkName = list.get(mIndex).toString();
        System.out.println(BookMarkName);
        return BookMarkName;
    }


    public byte[] MsgFileBody() throws IOException {
        mFileBody = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = fileContentStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        mFileBody = output.toByteArray();
        fileContentStream.close();
        return mFileBody;
    }

    public void MsgFileBody(byte[] body) {
        if (body != null) {
            this.FFileSize = body.length;
            this.mFileBody = body;
        } else {
            this.mFileBody = body;
        }
    }


    /**
     * 把字节数组保存为一个文件
     *
     * @param outputFile
     * @return
     */
    public boolean MsgFileSave(String outputFile) {
        try {
            File f = new File(outputFile);
            FileOutputStream fos = null;
            BufferedInputStream bis = null;
            int BUFFER_SIZE = 1024;
            byte[] buf = new byte[BUFFER_SIZE];
            int size = 0;
            bis = new BufferedInputStream(fileContentStream);
            fos = new FileOutputStream(f);
            while ((size = bis.read(buf)) != -1)
                fos.write(buf, 0, size);
            bis.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean MsgFileLoad(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            fileContentStream = new FileInputStream(new File(fileName));
            MsgFileBody();
        } else {
            mFileBody = new byte[0];
        }
        return true;
    }
    
/*    public boolean MsgFileLoad(String fileName) throws IOException{
    	File f = new File(fileName);  
        if (!f.exists()) {  
            throw new FileNotFoundException(fileName);  
        }
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());  
        BufferedInputStream in = null;  
        try {  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while (-1 != (len = in.read(buffer, 0, buf_size))) {  
                bos.write(buffer, 0, len);  
            } 
            mFileBody = bos.toByteArray(); 
            isLoadFile = true;
            return true;
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        } finally {  
            try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            bos.close();  
        } 
    }*/

    //获取setMsgByName的值并把封装成JSON数据
    public String GetHashToJson() {
        JSONObject json = new JSONObject();
        for (Iterator<String> iterator = saveFormParam.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            json.put(key, saveFormParam.get(key));
        }
        return json.toString();
    }

    // char[]转byte[]
    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    /**
     * @param response
     * @throws IOException
     * @deprecated:将文件的二进制数据设置到信息包中
     */
    public void Send(HttpServletResponse response, int codec, String fileId) throws IOException {
        try {
            String getJsonStr = GetHashToJson();
            response.reset();
            response.setHeader("RName", new String(getJsonStr.getBytes("gb2312"), "ISO8859-1"));
            response.setHeader("fileId", fileId);
            if (mFileBody.length != 0) {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/x-msdownload;charset=utf-8");
                if (codec == 0) {
                    response.setContentLength(mFileBody.length);
                    response.getOutputStream().write(mFileBody, 0, mFileBody.length);
                } else if (codec == 1) {
                    //char[] charsFileBody = Base64.encode(mFileBody);
                    //response.setContentLength(charsFileBody.length);
                    //System.out.println("mFileBody.length=" + mFileBody.length + " charsFileBody.length=" + charsFileBody.length);
                    //response.getOutputStream().write(getBytes(charsFileBody), 0, charsFileBody.length);
                }

                response.getOutputStream().flush();
                response.getOutputStream().close();

            } else {
                response.setHeader("MsgError", iMsgServer2015.MsgError);
            }
            response.flushBuffer();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

}
