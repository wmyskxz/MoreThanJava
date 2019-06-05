项目开始时间：2018年4月8日14:37:47
项目完成时间：2018年4月9日10:03:30

## 技术准备
> 这个项目是自己用于巩固 J2EE 相关知识的练手项目，非常简单，但是相关的功能却非常实用，所以在这里分享一下

为了完成这个项目，需要掌握如下技术：
- Java
基础知识
- 前端：
HTML, CSS, JAVASCRIPT, JQUERY
- J2EE：
Tomcat, Servlet, JSP, Filter
- 数据库：
MySQL

## 开发流程
项目虽然很简单，很小，但是为了开发的有条不紊，还是按照商业项目的开发来完成。

#### ① 需求分析
首先要确定要做哪些功能

- 使用**数据库**来**保存数据**
- 能**增删改查**学生的信息*（学号，名称，年龄，性别，出生日期）*

#### ② 表结构设计
根据需求，那么只需要一个 student 表就能够完成功能了。

- **创建数据库：student**
将数据库编码格式设置为 UTF-8 ，便于存取中文数据
```MySQL
DROP DATABASE IF EXISTS student;
CREATE DATABASE student DEFAULT CHARACTER SET utf8;
```

- **创建学生表：student**
不用**学生学号(studentID)**作为主键的原因是：不方便操作，例如在更新数据的时候，同时也要更改学号，那这样的操作怎么办呢？
所以我们加了一个 **id** 用来唯一表示当前数据。
```MySQL
CREATE TABLE student(
  id int(11) NOT NULL AUTO_INCREMENT,
  studentID int(11) NOT NULL UNIQUE,
  name varchar(255) NOT NULL,
  age int(11) NOT NULL,
  sex varchar(255) NOT NULL,
  birthday date DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

#### ③ 原型设计
就是设计界面，在商业项目中，这是很重要的一步，我们可以**解除界面原型，低成本、高效率**的与客户达成**需求的一致性**。

这个项目一共就分为两个页面：
- 主页面：
![首页](https://upload-images.jianshu.io/upload_images/7896890-0169962ea353fcbd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 学生编辑页面：
![编辑页面](https://upload-images.jianshu.io/upload_images/7896890-2eac73f04dcf3ea4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### ④ 实体类的设计
实体类仅仅是对数据库中表的一一映射，同时可能还需要兼顾对业务能力的支持。
- 在 Packge[bean]下创建 Student 类：
```java
package bean;

import java.util.Date;

public class Student {

	private int id;				// 在数据库中的ID
	private int studentID;      // 学号，跟ID区分开为了方便数据库操作
	private String name;		// 姓名
	private int age;			// 年龄
	private String sex;			// 性别
	private Date birthday;		// 出生日期

    // setter 和 getter (为节约篇幅没列出来)
}
```

#### ⑤ DAO 类的设计
DAO，即 Date Access Object，数据库访问对象，就是对数据库相关操作的封装，让其他地方看不到 JDBC 的代码。

首先我们先创建一个数据库操作的工具类：
- 在 Packge[util]下创建 DBUtil 类：
```java
/**
 * 数据库工具类，这个类的作用是初始化驱动，并且提供一个getConnection用于获取连接。
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	static String ip = "127.0.0.1";
	static int port = 3306;
	static String database = "student";
	static String encoding = "UTF-8";
	static String loginName = "root";
	static String password = "root";

	static {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s", ip, port, database, encoding);
		return DriverManager.getConnection(url, loginName, password);
	}

	public static void main(String[] args) throws SQLException {
		System.out.println(getConnection());

	}

}
```
- 写工具类的好处：
**便于统一维护，降低维护成本**

然后是 DAO 类，除了进行典型的 ORM 支持功能之外，也需要提供各种业务方法。
- 在 Packge[dao]下创建 StudentDAO 类：
```java
package dao;

import bean.Student;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

	public int getTotal() {

		int total = 0;

		String sql = "SELECT COUNT(*) FROM student";
		try (Connection c = DBUtil.getConnection(); Statement st = c.createStatement()) {

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return total;
	}

	public void add(Student student) {

		String sql = "INSERT INTO student VALUES(NULL,?,?,?,?,?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, student.getStudentID());
			ps.setString(2, student.getName());
			ps.setInt(3, student.getAge());
			ps.setString(4, student.getSex());
			ps.setDate(5, new java.sql.Date(student.getBirthday().getTime()));

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {

		String sql = "DELETE FROM student WHERE ID = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);

			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Student student) {

		String sql = "update student set student_id = ?, name = ?, age = ?, sex = ?, birthday = ? where id = ? ";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, student.getStudentID());
			ps.setString(2, student.getName());
			ps.setInt(3, student.getAge());
			ps.setString(4, student.getSex());
			ps.setDate(5, new java.sql.Date(student.getBirthday().getTime()));
			ps.setInt(6, student.getId());

			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Student get(int id) {

		Student student = new Student();

		String sql = "SELECT * FROM student WHERE ID = " + id;
		try (Connection c = DBUtil.getConnection(); Statement st = c.createStatement()) {

			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {

				int student_id = rs.getInt("student_id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String sex = rs.getString("sex");
				Date birthday = rs.getDate("birthday");
				student.setStudentID(student_id);
				student.setName(name);
				student.setAge(age);
				student.setSex(sex);
				student.setBirthday(birthday);
				student.setId(id);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return student;
	}

	public List<Student> list() {
		return list(0, Short.MAX_VALUE);
	}

	public List<Student> list(int start, int count) {

		List<Student> students = new ArrayList<>();

		String sql = "SELECT * FROM student ORDER BY student_id desc limit ?,?";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Student student = new Student();
				int id = rs.getInt("id");
				int studentID = rs.getInt("student_id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String sex = rs.getString("sex");
				Date birthday = rs.getDate("birthday");
				student.setId(id);
				student.setStudentID(studentID);
				student.setName(name);
				student.setAge(age);
				student.setSex(sex);
				student.setBirthday(birthday);

				students.add(student);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return students;
	}

}
```
- 该类中，既提供了**增删改查**这些基本的 **CRUD** 操作
1.增加：`public void add(Student student)`
2.删除：`public void delete(int id)`
3.修改：`public void update(Student student)`
4.查询所有：`public List<Student> list()`
- 又提供了一些非 **CRUD** 方法
1.获取总数：`public int getTotal()`
2.根据 id 获取：`public Student get(int id)`

#### ⑥ 业务类介绍
作为 J2EE Web 应用，一般都会按照如图所示的设计流程进行：
Servlet -> Service（业务类） -> DAO -> database 

当浏览器提交请求到 Tomcat Web 服务器的时候，对应的 Servlet 的doGet/doPost 方法会被调用，接着在 Servlet 中调用 Service类，然后在 Service 类中调用DAO类，最后在 DAO 中访问数据库获取相应的数据。
![](https://upload-images.jianshu.io/upload_images/7896890-7e224d06d164441a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

单本项目没有使用 Service 这一层，原因是在对 DAO 类进行开发中，已经提供了很好的支持业务的方法，没有必要再包括上一层 Service 业务类。

> 参考链接：[这里](http://how2j.cn/k/tmall-j2ee/tmall-j2ee-995/995.html)

#### ⑦ 功能开发
需要按照模块之间的依赖关系，顺序开发。
- 首先为项目添加[必要的 jar 包](https://pan.baidu.com/s/19EXWcsi1DUMdUrCXgPeqVg)：
jstl.jar
mysql-connector-java-5.0.8-bin.jar
servlet-api.jar
standard.jar
这也是 Web 开发中最基本的 4 个包

#### ——【1.编写 Filter】——
由于项目中设计表单 POST 方式的提交，所以我们先来编写好相关编码的过滤器，好支持中文的存取

- 在 Packge[filter] 下编写 EncodingFilter 类：
```java
package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

        // 设置编码格式为 UTF-8
		request.setCharacterEncoding("UTF-8");

		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
	}

}
```

#### ——【2. 编写 Servlet 】——
按照传统的方式，我们项目的业务为**增删改查**，所以对应四个路径，也就是需要编写四个 Servlet 才可以

- **AddServlet：**
```java
package servlet;

import bean.Student;
import dao.StudentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/addStudent")
public class AddServlet extends HttpServlet {

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Student student = new Student();

        // 直接从表单中获取数据
		int studentID = Integer.parseInt(req.getParameter("studentID"));
		String name = req.getParameter("name");
		int age = Integer.parseInt(req.getParameter("age"));
		String sex = req.getParameter("radio");
		Date birthday = null;

		// String 类型按照 yyyy-MM-dd 的格式转换为 java.util.Date 类
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			birthday = simpleDateFormat.parse(req.getParameter("birthday"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		student.setStudentID(studentID);
		student.setName(name);
		student.setAge(age);
		student.setSex(sex);
		student.setBirthday(birthday);

		new StudentDAO().add(student);

		resp.sendRedirect("/listStudent");  // 这里可以理解为刷新，重新请求
	}
}
```

- **DeleteServlet：**
```
package servlet;

import dao.StudentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteStudent")
public class DeleteServlet extends HttpServlet {

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int id = Integer.parseInt(req.getParameter("id"));
		new StudentDAO().delete(id);

		resp.sendRedirect("/listStudent");
	}
}
```

- **EditServlet：**
```java
package servlet;

import bean.Student;
import dao.StudentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editStudent")
public class EditServlet extends HttpServlet {

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int id = Integer.parseInt(req.getParameter("id"));
		Student student = new StudentDAO().get(id);

		req.setAttribute("student", student);

		req.getRequestDispatcher("/editStudent.jsp").forward(req, resp);
	}
}
```

- **ListServlet：**
```java
package servlet;

import bean.Student;
import dao.StudentDAO;
import util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/listStudent")
public class ListServlet extends HttpServlet {

	private StudentDAO studentDAO = new StudentDAO();

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 获取分页参数
		int start = 0;
		int count = 10;

		try {
			start = Integer.parseInt(req.getParameter("page.start"));
			count = Integer.parseInt(req.getParameter("page.count"));
		} catch (Exception e) {
		}

		Page page = new Page(start, count);

		List<Student> students = studentDAO.list(page.getStart(), page.getCount());
		int total = studentDAO.getTotal();
		page.setTotal(total);

		req.setAttribute("students", students);
		req.setAttribute("page", page);

		req.getRequestDispatcher("/listStudent.jsp").forward(req, resp);
	}
}
```

- **UpdateServlet：**
```java
package servlet;

import bean.Student;
import dao.StudentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/updateStudent")
public class UpdateServlet extends HttpServlet {

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Student student = new Student();

		int id = Integer.parseInt(req.getParameter("id"));
		int studentID = Integer.parseInt(req.getParameter("studentID"));
		String name = req.getParameter("name");
		int age = Integer.parseInt(req.getParameter("age"));
		String sex = req.getParameter("radio");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date birthday = null;
		try {
			birthday = simpleDateFormat.parse(req.getParameter("birthday"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		student.setId(id);
		student.setStudentID(studentID);
		student.setName(name);
		student.setAge(age);
		student.setSex(sex);
		student.setBirthday(birthday);

		new StudentDAO().update(student);

		resp.sendRedirect("/listStudent");
	}
}
```

#### ——【3. JSP 的编写】——
我们把默认的 index.jsp 修改成如下代码：
```jsp
<%
    request.getRequestDispatcher("/listStudent").forward(request, response);
%>
```
- 引入 JQ 和 Bootstrap
为了简化操作，引入了 JQuery 和 Bootstrap
- 编写 listStudent.jsp
其实主要还是利用 Bootstrap 编写好整个页面，我写的时候也是对照[这里](http://www.runoob.com/bootstrap/bootstrap-panels.html)写的

```
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>

    <%-- 引入JQ和Bootstrap --%>
    <script src="js/jquery/2.0.0/jquery.min.js"></script>
    <link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link href="css/style.css" rel="stylesheet">

    <title>学生管理页面 - 首页</title>

    <script>
        $(function () {
            $("ul.pagination li.disabled a").click(function () {
                return false;
            });
        });
    </script>
</head>

<body>

<div class="listDIV">
    <table class="table table-striped table-bordered table-hover table-condensed">

        <caption>学生列表 - 共${page.total}人</caption>
        <thead>
        <tr class="success">
            <th>学号</th>
            <th>姓名</th>
            <th>年龄</th>
            <th>性别</th>
            <th>出生日期</th>

            <th>编辑</th>
            <th>删除</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${students}" var="s" varStatus="status">
            <tr>
                <td>${s.studentID}</td>
                <td>${s.name}</td>
                <td>${s.age}</td>
                <td>${s.sex}</td>
                <td>${s.birthday}</td>

                <td><a href="/editStudent?id=${s.id}"><span class="glyphicon glyphicon-edit"></span> </a></td>
                <td><a href="/deleteStudent?id=${s.id}"><span class="glyphicon glyphicon-trash"></span> </a></td>
            </tr>
        </c:forEach>

        </tbody>
    </table>
</div>

<nav class="pageDIV">
    <ul class="pagination">
        <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
            <a href="?page.start=0">
                <span>«</span>
            </a>
        </li>

        <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
            <a href="?page.start=${page.start-page.count}">
                <span>‹</span>
            </a>
        </li>

        <c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">

            <c:if test="${status.count*page.count-page.start<=30 && status.count*page.count-page.start>=-10}">
                <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
                    <a
                            href="?page.start=${status.index*page.count}"
                            <c:if test="${status.index*page.count==page.start}">class="current"</c:if>
                    >${status.count}</a>
                </li>
            </c:if>
        </c:forEach>

        <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
            <a href="?page.start=${page.start+page.count}">
                <span>›</span>
            </a>
        </li>
        <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
            <a href="?page.start=${page.last}">
                <span>»</span>
            </a>
        </li>
    </ul>
</nav>

<div class="addDIV">

    <div class="panel panel-success">
        <div class="panel-heading">
            <h3 class="panel-title">增加学生</h3>
        </div>
        <div class="panel-body">

            <form method="post" action="/addStudent" role="form">
                <table class="addTable">
                    <tr>
                        <td>学号：</td>
                        <td><input type="text" name="studentID" id="studentID" placeholder="请在这里输入学号"></td>
                    </tr>
                    <tr>
                        <td>姓名：</td>
                        <td><input type="text" name="name" id="name" placeholder="请在这里输入名字"></td>
                    </tr>
                    <tr>
                        <td>年龄：</td>
                        <td><input type="text" name="age" id="age" placeholder="请在这里输入年龄"></td>
                    </tr>
                    <tr>
                        <td>性别：</td>
                        <td><input type="radio" class="radio radio-inline" name="radio" value="男"> 男
                            <input type="radio" class="radio radio-inline" name="radio" value="女"> 女
                        </td>
                    </tr>
                    <tr>
                        <td>出生日期：</td>
                        <td><input type="date" name="birthday" id="birthday" placeholder="请在这里输入出生日期"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <button type="submit" class="btn btn-success">提 交</button>
                        </td>

                    </tr>

                </table>
            </form>
        </div>
    </div>

</div>

</body>
</html>
```

- eidtStudent.jsp
编辑表单对照着首页的增加表单稍微改一改参数就好了
```
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

    <%-- 引入JQ和Bootstrap --%>
    <script src="js/jquery/2.0.0/jquery.min.js"></script>
    <link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link href="css/style.css" rel="stylesheet">

    <title>学生管理页面 - 编辑页面</title>
</head>

<body>

<div class="editDIV">

    <div class="panel panel-success">
        <div class="panel-heading">
            <h3 class="panel-title">编辑学生</h3>
        </div>
        <div class="panel-body">

            <form method="post" action="/updateStudent" role="form">
                <table class="editTable">
                    <tr>
                        <td>学号：</td>
                        <td><input type="text" name="studentID" id="studentID" value="${student.studentID}"
                                   placeholder="请在这里输入学号"></td>
                    </tr>
                    <tr>
                        <td>姓名：</td>
                        <td><input type="text" name="name" id="name" value="${student.name}" placeholder="请在这里输入名字">
                        </td>
                    </tr>
                    <tr>
                        <td>年龄：</td>
                        <td><input type="text" name="age" id="age" value="${student.age}" placeholder="请在这里输入年龄"></td>
                    </tr>
                    <tr>
                        <td>性别：</td>
                        <td><input type="radio" class="radio radio-inline" name="radio" value="男"> 男
                            <input type="radio" class="radio radio-inline" name="radio" value="女"> 女
                        </td>
                    </tr>
                    <tr>
                        <td>出生日期：</td>
                        <td><input type="date" name="birthday" id="birthday" value="${student.birthday}"
                                   placeholder="请在这里输入出生日期"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <input type="hidden" name="id" value="${student.id}">
                            <button type="submit" class="btn btn-success">提 交</button>
                        </td>

                    </tr>

                </table>
            </form>
        </div>
    </div>

</div>

</body>
</html>
```

- style.css 文件：
```
body {
    padding-top: 60px;
}

div.listDIV {
    width: 600px;
    margin: 0 auto;
}

div.editDIV {
    width: 400px;
    margin: 0 auto;
}

nav.pageDIV {
    text-align: center;
}

div.addDIV {
    width: 300px;
    margin: 0 auto;
}

table.addTable {
    width: 100%;
    padding: 5px;
}

table.addTable td {
    padding: 5px;
}

table.editTable {
    width: 100%;
    padding: 5px;
}

table.editTable td {
    padding: 5px;
}
```

#### ——【4. 项目细节】——
- 项目的整理结构：
![](https://upload-images.jianshu.io/upload_images/7896890-e95ffeefc9bfbeca.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 分页功能
- 首页在 Packge[util] 下创建一个 Page 工具类：
```java
package util;

public class Page {

	int start;		// 开始数据
	int count;		// 每一页的数量
	int total;		// 总共的数据量

	public Page(int start, int count) {
		super();
		this.start = start;
		this.count = count;
	}

	public boolean isHasPreviouse(){
		if(start==0)
			return false;
		return true;

	}
	public boolean isHasNext(){
		if(start==getLast())
			return false;
		return true;
	}

	public int getTotalPage(){
		int totalPage;
		// 假设总数是50，是能够被5整除的，那么就有10页
		if (0 == total % count)
			totalPage = total /count;
			// 假设总数是51，不能够被5整除的，那么就有11页
		else
			totalPage = total / count + 1;

		if(0==totalPage)
			totalPage = 1;
		return totalPage;

	}

	public int getLast(){
		int last;
		// 假设总数是50，是能够被5整除的，那么最后一页的开始就是40
		if (0 == total % count)
			last = total - count;
			// 假设总数是51，不能够被5整除的，那么最后一页的开始就是50
		else
			last = total - total % count;

		last = last<0?0:last;
		return last;
	}

    // 各种 setter 和 getter
}
```
- totalPage 是计算得来的数，用来表示页码一共的数量

在首页显示的 StudentList 用 page 的参数来获取：
```
List<Student> students = studentDAO.list(page.getStart(), page.getCount());
```
并且在 DAO 类中用 **LIMIT** 关键字：
```
String sql = "SELECT * FROM student ORDER BY student_id desc limit ?,?";
```
- 第一个参数为 start，第二个参数为 count
这样就能根据分页的信息来获取到响应的数据

- **编写分页栏：**

1.写好头和尾
```
<nav class="pageDIV">
    <ul class="pagination">
    .....
    </ul>
</nav>
```
2.写好`«` ` ‹`这两个功能按钮
使用 `<c:if>`标签来增加边界判断，如果没有前面的页码了则设置为disable状态
```
        <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
            <a href="?page.start=0">
                <span>«</span>
            </a>
        </li>

        <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
            <a href="?page.start=${page.start-page.count}">
                <span>‹</span>
            </a>
        </li>
```
再通过 JavaScrip 代码来完成禁用功能：
```
<script>
    $(function () {
        $("ul.pagination li.disabled a").click(function () {
            return false;
        });
    });
</script>
```
3.完成中间页码的编写
从 `0` 循环到 `page.totalPage - 1` ，`varStatus` 相当于是循环变量
- status.count 是从1开始遍历
- status.index 是从0开始遍历
- **要求：**显示当前页码的前两个和后两个就可，例如当前页码为3的时候，就显示 1 2 3(当前页) 4 5 的页码
- **理解测试条件：**
-10 <= 当前页*每一页显示的数目 - 当前页开始的数据编号 <= 30
![](https://upload-images.jianshu.io/upload_images/7896890-1f82d91e47a31c7f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 只要理解了这个判断条件，其他的就都好理解了
```
<c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">

    <c:if test="${status.count*page.count-page.start<=30 && status.count*page.count-page.start>=-10}">
        <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
            <a
                    href="?page.start=${status.index*page.count}"
                    <c:if test="${status.index*page.count==page.start}">class="current"</c:if>
            >${status.count}</a>
        </li>
    </c:if>
</c:forEach>
```
4.在 Servlet 中获取参数
```
// 获取分页参数
int start = 0;
int count = 10;

try {
	start = Integer.parseInt(req.getParameter("page.start"));
	count = Integer.parseInt(req.getParameter("page.count"));
} catch (Exception e) {
}

....

// 共享 page 数据
req.setAttribute("page", page);
```
#### Date 转换的问题
```java
    /**
     * Date类型转为指定格式的String类型
     * 
     * @param source
     * @param pattern
     * @return
     */
    public static String DateToString(Date source, String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(source);
    }
```

```java
    /**
     * 
     * 字符串转换为对应日期
     * 
     * @param source
     * @param pattern
     * @return
     */
    public static Date stringToDate(String source, String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(source);
        } catch (Exception e) {
        }
        return date;
    }
```

## 项目总结
这一个项目实在有些太简单了，可能最需要理解的一个功能就属于**【分页功能】**了吧

不过还是借助这个项目，进一步巩固了 J2EE 开发的相关知识，也对开发的流程愈发熟悉，整个项目编写时间不超过 8 个小时，对于我自己来说，不算快，但还算比较顺畅的

#### 需要改进的地方：

1. 登录验证
本项目没有增加登录验证，可以增加一个登录页面并结合 session 来完成验证

2. 代码重构
本项目仅仅完成的是一个学生表的增删改查，却有以下的五个 Servlet ：

![](https://upload-images.jianshu.io/upload_images/7896890-83b087dbf84970dd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果项目大起来，那可想而知，Servlet 有多臃肿，维护成本有多高
- **改进方法：**用一个 StudentServlet 代替
- **具体做法：**使用 **Filter + Servlet** 完成
> ① 首先编写一个**过滤所有地址的 Filter**，并**解析地址栏的地址**，**提取出其中的方法**传递给 **StudentServlet** *（这个时候需要统一的地址，如：`student_list`、`student_edit`、`student_delete`、`student_update`）*
```
request.setAttribute("method", method);
```
> ② 在 Servlet 中获取 method 方法，并调用
```
// 获取到对应的方法
String method = (String) request.getAttribute("method");
// 对 method 作判断，调用对应的方法
```

3. 没有对输入的数据的正确性进行验证
这显然会导致许多问题，可以通过 js 代码来完成验证

---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693