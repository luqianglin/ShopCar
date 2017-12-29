package com.et.shop;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.et.util.DbUntils;
import com.sun.faces.taglib.html_basic.InputSecretTag;

public class QueryGoodServlet extends HttpServlet {
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		String goodName = request.getParameter("goodName");
		if(goodName==null){
			goodName="";
		}
		PrintWriter out = response.getWriter();
		out.println("<style type=\"text/css\">form input{"+"border: 2px solid red"+"}"+"input.txt{"+"width:25%"+"}"+ "input.sou{"+"color: white; width: 5%;background-color: red"+"}"+
			" table{"+
			"width:100%;border: 1px solid black"+
			"}"+
			"th,td{"+
			"	border: 1px solid black"+
			"}</style>");
		
		out.println("<form action=\"QueryGoodServlet\" method=get>"+
				"<input type=\"text\" class=\"txt\" name=\"goodName\" value=\""+goodName+"\"/><input type=\"submit\" class=\"sou\" value=\"搜索\"/>"+  
				"</form>");
		
		out.println("<table> "+
			"<tr>"+
			"<th>显示图片</th><th>商品名称</th><th>模型</th><th>商品单价</th><th>商品位置</th><th>描述</th><th>操作</th>"+
			"</tr>");
		
		//查询逻辑
		String sql ="select * from GooDS where name like '%"+goodName+"%'";
		try {
			//预编译sql语句
			PreparedStatement pst = conn.prepareStatement(sql);
			//
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				String model = rs.getString("MODEL");
				String imagePath = rs.getString("IMAGEPATH");
				double price = rs.getDouble("PRICE");
				String stock = rs.getString("STOCK");
				String descp = rs.getString("DESCP");
				out.println("<tr>"+
	    		"<td><img src=\""+request.getContextPath()+imagePath+"\"/></td><td>"+name+"</td><td>"+model+"</td><td>"+price+"</td><td>"+stock+"</td><td>"+descp+"</td><td><input type='button' value='+' onclick=\"window.location='CartServlet?id="+id+"';\"></td>"+
				"</tr>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.println("</table>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		this.doGet(request, response);
	}
	
	public static Connection conn = null;
	@Override
	public void destroy() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void init() throws ServletException {
		try {
			conn = DbUntils.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
