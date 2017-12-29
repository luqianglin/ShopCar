package com.et.shop;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import com.et.util.DbUntils;


public class CartServlet extends HttpServlet {

	/**
	 * 键值对
	 * id=次数
	 * 点击了某个商品后 如果session中不存在 该 id的记录 id=1
	 * 				存在该id的记录 id=id+1;
	 *
	 * 
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("id");
//		String id1 = request.getParameter("id1");
		HttpSession session = request.getSession();
		
		//购物车算法
//		if(id1!=null){
//			session.setAttribute(id1, (Integer)session.getAttribute(id1)-1);
//			if((Integer)session.getAttribute(id1)<=0){
//				session.removeAttribute(id1);
//			}
//		}else{
//			if(session.getAttribute(id)==null){
//				session.setAttribute(id, 1);
//			}else{
//				session.setAttribute(id, (Integer)session.getAttribute(id)+1);
//			}
//		}
		
		if(session.getAttribute(id)==null){
			session.setAttribute(id, 1);
		}else{
			// flag=add minus enter
			String flag = request.getParameter("flag");
			if("add".equals(flag)){
				session.setAttribute(id, (Integer)session.getAttribute(id)+1);
			}else if("remove".equals(flag)){
				int i=(Integer)session.getAttribute(id)-1;
				session.setAttribute(id, i);
				if(i<=0){
					session.removeAttribute(id);
				}
			}else if("enter".equals(flag)){
				String num = request.getParameter("num");
				session.setAttribute(id, Integer.parseInt(num));
			}else{
				session.setAttribute(id, (Integer)session.getAttribute(id)+1);
			}
		}
		
		out.println("购物车数据:");
		out.println("<a href='QueryGoodServlet'>继续购物</a>");
		out.println("<style type=\"text/css\"> table{"+
				"width:100%;border: 1px solid black"+
				"}"+
				"th,td{"+
				"	border: 1px solid black"+
				"}</style>");
		
		out.println("<table style=\"width:100%;border:1px solid black\">"+
				"<tr>"+
				"<th>id</th><th>购买数量</th><th>显示图片</th><th>商品名称</th><th>模型</th><th>商品单价</th><th>商品位置</th><th>描述</th><th>操作</th>"+
		
				"</tr>");
			
		
		Enumeration em = session.getAttributeNames();
		while(em.hasMoreElements()){

			String  name = em.nextElement().toString();
			String value = session.getAttribute(name).toString();	
			String sql = "select * from GooDS where id="+name;
				
			try {
				Connection conn = QueryGoodServlet.conn;
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()){
					String imagePath = rs.getString("IMAGEPATH");
					String goodName = rs.getString("NAME");
					String model = rs.getString("MODEL");
					double price = rs.getDouble("PRICE");
					String address = rs.getString("STOCK");
					String descp = rs.getString("DESCP");
					
					out.println("</tr>");
					out.println("<td>"+name+"</td>");
					//out.println("<td>"+value+"</td>");
					out.println("<td><img src=\""+request.getContextPath()+imagePath+"\"/></td>");
					out.println("<td>"+goodName +"</td>");
					out.println("<td>"+model+"</td>");
					out.println("<td>"+price+"</td>");
					out.println("<td>"+address+"</td>");
					//out.println("<td>"+descp+"</td>");
					out.println("<td><input type='button' value='+' onclick=\"window.location='CartServlet?id="+name+"&flag=add';\">" +
							"<input id='"+name+"' style=width:50px; type='text' value='"+value+"' onkeydown=\"if(event.keyCode == 13){window.location='CartServlet?id="+name+"&flag=enter&num='+document.getElementById('"+name+"').value}\">"+

							"<input type='button' value='-' onclick=\"window.location='CartServlet?id="+name+"&flag=remove';\"></td>");
					out.println("</tr>");
					
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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

}
