![](https://upload-images.jianshu.io/upload_images/7896890-326744dd6c79212a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 文件上传

#### 文件上传前的准备
1. 在表单中**必须有一个上传的控件**
```<input type="file" name="testImg"/>```
2. 因为 GET 方式有请求大小的限制，所以**表单的提交方式必须是 POST**
```<form action="/upload" method="post">```
3. 表单默认的编码方式为 `application/x-www-form-urlencoded` ，应该修改为 ```multipart/form-data``` ，**以二进制的形式进行数据的传输**
```<form action="/upload" method="post" enctype="multipart/form-data">```

- **注意：**此时 Servlet 中就不能再使用 `request对象.getParameter(String name)` 来获取请求参数
![](https://upload-images.jianshu.io/upload_images/7896890-9ffa6de1139ff470.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 上传组件
要**实现文件的上传**，需要基于以下两种组件的一种：
- **Apache FileUpload 组件**【操作比较复杂】
- **SmartUpload 组件**【操作比较简单，但存在一些问题】

---

## 基于 FileUpload 组件

使用基于Apache FileUpload上传组件实现文件的上传，步骤：

1. 将[必要的 jar 包](https://pan.baidu.com/s/1HqCRcoRyVw3JbXApjCcglw)导入到项目中
commons-fileupload-1.2.2.jar
commons-io-1.4.jar
2. 简单写好 upload.jsp 上传页面：

```html
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*" %>
<html>
<head>
    <title>文件的上传和下载</title>
</head>
<body>

文件上传：
<form action="/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="testImg"/> <br>
    <input type="submit"/>
</form>

</body>
</html>
```
![](https://upload-images.jianshu.io/upload_images/7896890-4103d55665ab457a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

3. 编写 UploadServlet 处理相关请求：

```java
package servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 验证请求是否满足要求（post 请求 / enctype 是否以multipart打头
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        // 如果不满足要求就立即结束对该请求的处理
        if (!isMultipart) {
            return;
        }

        try {
            // FileItem 是表单中的每一个元素的封装
            // 创建一个 FileItem 的工厂类
            FileItemFactory factory = new DiskFileItemFactory();
            // 创建一个文件上传处理器（装饰设计模式）
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 解析请求
            List<FileItem> items = upload.parseRequest(req);
            for (FileItem fileItem :
                    items) {
                // 判断空间是否是普通控件
                if (fileItem.isFormField()) {
                    // 普通控件
                } else {
                    // 上传控件
                    // 将上传的文件保存到服务器
                    fileItem.write(new File("C:/", "testImg.jpg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```


#### 文件上传的细节
上面已经能实现简单的文件上传功能了，但文件上传有很多需要注意的地方。

- **缓存大小和临时目录**
在上传文件的时候,如果文件过大,而且是将文件放在缓存中,就可能造成内存溢出
默认的缓存大小为 `10 kb`，临时目录的默认值为 `tomcat/temp`
- **解决方案：**将操作缓存大小的文件放在服务器的磁盘(临时目录)中,在文件的大小超过设定的缓存大小的时候就会使用到临时目录
``` java
// 设置缓存大小 500kb
factory.setSizeThreshold(1024*500);
// 设置临时目录
factory.setRepository(new File("C:/"));
```
- **通常不需要设置**

另一个问题是：如果用户 A 上传了一个文件名叫xxx，用户 B 也上传了一个文件名叫 xxx，那么此时用户 B 的文件会把用户 A 的文件给覆盖掉。
- **文件名称的处理**
相同文件名称的文件，后面的文件会把前面的文件覆盖掉
- **解决方案：使用UUID来生成一个随机的而且不重复字符串**作为文件的名称，获取真实文件的后缀名需要用到 `FilenameUtils` 这个工具类
- **语法：**

```java
String fileName = UUID.randomUUID().toString()+"."+FilenameUtils.getExtension(fileItem.getName());
```

#### 文件类型的约束

上传文件的类型应该需要做一些约束的，比如在上传头像的时候就只允许上传图片，其他类型的文件都不应该允许上传，并且如果类型不正确的时候，应该给用户一个错误的提示。

- **jsp 文件增加显示错误提示信息：**

我们给我们的 jsp 开头增加一个 `<span>` 来显示错误信息

```html
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*" %>
<html>
<head>
    <title>文件的上传和下载</title>
</head>
<body>

文件上传：<span>${errorMsg}</span>
<form action="/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="testImg"/> <br>
    <input type="submit"/>
</form>

</body>
</html>
```

- **判断上传文件类型：**
限制文件类型那么就需要对文件类型进行判断
- 语法：```String contentType = fileItem.getContentType();```
- Servlet 源码：
```java
package servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 验证请求是否满足要求（post 请求 / enctype 是否以multipart打头
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        // 如果不满足要求就立即结束对该请求的处理
        if (!isMultipart) {
            return;
        }

        try {
            // FileItem 是表单中的每一个元素的封装
            // 创建一个 FileItem 的工厂类
            FileItemFactory factory = new DiskFileItemFactory();
            // 创建一个文件上传处理器（装饰设计模式）
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 解析请求
            List<FileItem> items = upload.parseRequest(req);
            for (FileItem fileItem :
                    items) {
                // 判断空间是否是普通控件
                if (fileItem.isFormField()) {
                    // 普通控件
                } else {
                    // 上传控件
                    String contentType = fileItem.getContentType();
                    if (!contentType.startsWith("image/")) {
                        // 实现简单的错误提示
                        req.setAttribute("errorMsg", "亲，您上传的文件格式不正确，请重新上传！");
                        req.getRequestDispatcher("upload.jsp").forward(req, resp);
                        return;  // 如果不是图片类型则不再对请求进行处理
                    }
                    // 随机命名文件名
                    String fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileItem.getName());
                    // 将上传的文件保存到服务器
                    fileItem.write(new File("C:/", fileName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 解决中文乱码问题
- 解决中文文件名乱码（事实上我在本地测试并没有出现乱码），得到解析器以后，就**直接设置解析器的编码为UTF-8就行了**
```
 fileUpload.setHeaderEncoding("UTF-8");
```
- 解决表单数据乱码，在获取表单值的时候，按照UTF-8编码来获取
```
String value = fileItem.getString("UTF-8");
```


#### 文件大小约束

- 单个文件数据的大小约束
即整个表单之中只有一个上传控件
- 语法：`upload.setFileSizeMax(1024 * 500); // 单个文件不能超过500 kb`
- 一次请求数据的大小约束
限制整个请求之中的数据大小
- 语法：`upload.setSizeMax(1024 * 750); // 整个请求文件大小不能超过 750 kb`


#### 使用 Map 封装请求信息

上面提到，我们已经无法用 request对象正常获取到参数，那么我们究竟应该怎么办呢？

> - 使用 Map 封装一下就好了，等到要用的时候再从 Map 中取出
![](https://upload-images.jianshu.io/upload_images/7896890-4e09be236b365217.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


---

## SmartUpload

上面已经基于 FileUpload 实现了文件的上传，事实上也没有想象中那么复杂，让我们来看看 SmartUpload

要使用SmartUpload组件，就需要导入smartupload.jar开发包

#### 快速入门

``` java
//实例化组件
SmartUpload smartUpload = new SmartUpload();

//初始化上传操作
smartUpload.initialize(this.getServletConfig(), request, response);

try {

    //上传准备
    smartUpload.upload();
    
    //对于普通数据，单纯到request对象是无法获取得到提交参数的。也是需要依赖smartUpload
    String password = smartUpload.getRequest().getParameter("password");
    System.out.println(password);
    
    //上传到uploadFile文件夹中
    smartUpload.save("uploadFile");

} catch (SmartUploadException e) {
    e.printStackTrace();    
}
```

- 摘自：[这里](https://mp.weixin.qq.com/s?__biz=MzI4Njg5MDA5NA==&mid=2247483727&idx=1&sn=87db212ac7e2590c57749a9857028f14&chksm=ebd7404edca0c9583fb3074d6827952bbfb5335c13e236719cde573e52bfe89e85e133755ab3#rd)
- 上面链接提到 SmartUpload 组件在解决中文乱码中有一些小问题，我没有测试过，但我觉得自己写一个 FileUpload 工具类已经足够使用了
---

## 文件下载
1. 写一个简单的 jsp 页面：

![](https://upload-images.jianshu.io/upload_images/7896890-6ace49a2c5c19e7e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2. 在 WEB-INF 下创建 download 文件夹，里面放入一个 test.zip
3. 编写 Servlet 处理相关请求

```java
package servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 设置响应数据的 MIME 类型
        resp.setContentType("application/x-msdownload");
        // 获取文件名称
        String fileName = req.getParameter("fileName");
        // 判断浏览器是否是 IE
        String userAgent = req.getHeader("User-Agent");
        if (userAgent.contains("MSIE")) {
            // IE
            // 设置文件的名称
            resp.setHeader("Content-Disposition", "attachment; fileName="
                    + URLEncoder.encode(fileName, "UTF-8"));
        } else {
            // 非IE
            resp.setHeader("Content-Disposition", "attachment; fileName="
                    + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
        }

        // 获取文件所在的路径
        String path = req.getServletContext().getRealPath("/WEB-INF/download");

        // 获取指定的文件对象
        File f = new File(path, fileName);
        ServletOutputStream out = resp.getOutputStream();
        // 将文件复制到输出流中，响应给浏览器
        Files.copy(Paths.get(f.getAbsolutePath()), out);
    }
}
```

---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693