package com.wiwj.screen.update.mvc;

import com.wiwj.screen.update.util.CommandUtil;
import com.wiwj.screen.update.util.Log;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class UpdateController {
    public static String baseDir = null;

    @PostMapping("update")
    public void upload(@RequestParam("file") MultipartFile file,
                       HttpServletResponse response) {
        boolean flag = false;
        InputStream in = null;
        OutputStream out = null;
        if (stopServer()) {
            try {
                File serverFile = new File(baseDir + "screen");
                if (serverFile.exists()) {
                    File bakDir = new File(baseDir + "server_bak");
                    if (!bakDir.exists()) {
                        bakDir.mkdir();
                    }
                    File bakFile = new File(baseDir + "server_bak/screen." + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                    bakFile.createNewFile();
                    in = new FileInputStream(serverFile);
                    out = new FileOutputStream(bakFile);
                    FileCopyUtils.copy(in, out);
                    serverFile.delete();
                    Log.log("文件："+baseDir + "screen备份成功");
                }
                flag = doUpload(file);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            String ret = "<script>alert('update screen Success!\\nplease start screen server!');</script>";
            String ret2 = "<script>alert('update screen Fail!');</script>";
            response.getWriter().print(flag ? ret : ret2);
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("server/stop")
    public void serverStop(HttpServletResponse response) {
        try {
            String ret = "<script>alert('stop server Success!');</script>";
            String ret2 = "<script>alert('stop server Fail!');</script>";
            response.getWriter().print(stopServer() ? ret : ret2);
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("server/start")
    public void serverStart(HttpServletResponse response) {
        try {
            String ret = "<script>alert('start server Success!');</script>";
            String ret2 = "<script>alert('start server Fail!');</script>";
            response.getWriter().print(startServer() ? ret : ret2);
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("run")
    public Map runCommand(String commandStr) {
        Map ret = new HashMap();
        ret.put("message", CommandUtil.run(commandStr));
        return ret;
    }

    private boolean doUpload(MultipartFile file) {
        try {
            file.transferTo(new File(baseDir + "screen"));
            Log.log("新文件："+baseDir + "screen上传成功");
            return true;
        } catch (Exception e) {
            Log.log("新文件："+baseDir + "screen上传失败");
            e.printStackTrace();
            return false;
        }
    }

    private boolean stopServer() {
        Log.log("停止screen服务");
        CommandUtil.execute("screen-end.exe");
        return CommandUtil.execute("screen-stop-server.exe");
    }

    private boolean startServer() {
        Log.log("启动screen服务");
        return CommandUtil.execute("screen.exe");
    }
}
