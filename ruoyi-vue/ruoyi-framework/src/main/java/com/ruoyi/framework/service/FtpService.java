package com.ruoyi.framework.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import com.ruoyi.framework.config.FtpProperties;

@Service
@Slf4j
public class FtpService {
    private final FtpProperties ftpProperties;

    public FtpService(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    /**
     * 创建并配置FTP客户端
     * 
     * @return 配置好的FTP客户端
     * @throws IOException 连接异常时抛出
     */
    private FTPClient createFtpClient() throws IOException {
        FTPClient ftpClient = new FTPClient();
        // 设置连接超时时间
        ftpClient.setConnectTimeout(10000);
        // 设置数据传输超时
        ftpClient.setDataTimeout(60000);
        // 控制连接超时
        ftpClient.setDefaultTimeout(10000);
        
        log.info("正在连接FTP服务器: {}:{}", ftpProperties.getServer(), ftpProperties.getPort());
        ftpClient.connect(ftpProperties.getServer(), ftpProperties.getPort());
        
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            log.error("FTP服务器拒绝连接, 返回码: {}", reply);
            throw new IOException("FTP服务器拒绝连接, 返回码: " + reply);
        }
        
        if (!ftpClient.login(ftpProperties.getUser(), ftpProperties.getPassword())) {
            disconnect(ftpClient);
            throw new IOException("FTP登录失败，用户名或密码错误");
        }
        
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setBufferSize(1024 * 1024);
        
        log.info("FTP连接建立成功");
        return ftpClient;
    }

    /**
     * 安全关闭FTP连接
     * 
     * @param ftpClient FTP客户端
     */
    private void disconnect(FTPClient ftpClient) {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
                log.info("FTP连接已关闭");
            } catch (IOException e) {
                log.warn("关闭FTP连接时出错", e);
            }
        }
    }

    /**
     * 列出指定目录下的文件
     * 
     * @param remoteDir 远程目录
     * @return 文件名列表
     * @throws Exception 操作失败时抛出
     */
    public List<String> listFiles(String remoteDir) throws Exception {
        List<String> fileList = new ArrayList<>();
        FTPClient ftpClient = null;

        try {
            ftpClient = createFtpClient();
            FTPFile[] files = ftpClient.listFiles(remoteDir);

            for (FTPFile file : files) {
                fileList.add(file.getName());
            }
            return fileList;
        } catch (Exception e) {
            log.error("通过FTP获取文件信息失败: {}", e.getMessage(), e);
            throw e;
        } finally {
            disconnect(ftpClient);
        }
    }

    /**
     * 上传文件到FTP服务器
     * 
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @param remoteDir 远程目录
     * @return 上传后的文件路径
     * @throws Exception 操作失败时抛出
     */
    public String uploadFile(InputStream inputStream, String fileName, String remoteDir) throws Exception {
        FTPClient ftpClient = null;
        String newFileName = null;
        
        try {
            ftpClient = createFtpClient();
            
            // 确保目录存在
            createDirectoryIfNotExists(ftpClient, remoteDir);
            
            // 生成新的文件名（使用UUID）
            String fileExtension = "";
            if (fileName.contains(".")) {
                fileExtension = fileName.substring(fileName.lastIndexOf("."));
            }
            newFileName = UUID.randomUUID().toString() + fileExtension;
            
            // 上传文件
            boolean success = ftpClient.storeFile(remoteDir + "/" + newFileName, inputStream);
            if (!success) {
                throw new IOException("文件上传失败");
            }
            
            log.info("文件上传成功: {}", newFileName);
            return newFileName;
            
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw e;
        } finally {
            disconnect(ftpClient);
        }
    }
    
    /**
     * 创建目录（如果不存在）
     * 
     * @param ftpClient FTP客户端
     * @param dirPath 目录路径
     * @throws IOException 操作失败时抛出
     */
    private void createDirectoryIfNotExists(FTPClient ftpClient, String dirPath) throws IOException {
        String[] dirs = dirPath.split("/");
        String currentPath = "";
        
        for (String dir : dirs) {
            if (dir.isEmpty()) {
                continue;
            }
            
            currentPath += "/" + dir;
            
            // 检查目录是否存在
            FTPFile[] files = ftpClient.listFiles(currentPath);
            boolean dirExists = false;
            
            for (FTPFile file : files) {
                if (file.isDirectory() && file.getName().equals(dir)) {
                    dirExists = true;
                    break;
                }
            }
            
            // 如果目录不存在，创建它
            if (!dirExists) {
                boolean success = ftpClient.makeDirectory(currentPath);
                if (!success) {
                    throw new IOException("创建目录失败: " + currentPath);
                }
                log.info("创建目录成功: {}", currentPath);
            }
        }
    }
}