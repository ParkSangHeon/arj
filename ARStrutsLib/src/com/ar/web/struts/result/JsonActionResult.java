package com.ar.web.struts.result;

import java.io.OutputStream;
import java.io.StringWriter;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.json.simple.JSONObject;

import com.ar.common.collection.QMap;
import com.ar.web.struts.action.ARAction;
import com.opensymphony.xwork2.ActionInvocation;

/****************************************
 * <pre>
 * JSON 결과 반환 ActionResult클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ****************************************/
public class JsonActionResult extends StrutsResultSupport {

	private static final long serialVersionUID = -4835424523085918842L;
	
	private String encoding;
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	} // setEncoding()
	
	protected void doExecute(String location, ActionInvocation invocation) throws Exception {
		Object obj = invocation.getAction();
		if (obj instanceof ARAction) {
			ARAction commonAction = (ARAction)obj;

			QMap map = commonAction.getParams();

//			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			
			// Create JSON
			//String json = map.json();
			String json = JSONObject.toJSONString(map);
			
			if (encoding == null || encoding.trim().equals("")) {
				PrintWriter writer = response.getWriter();
				writer.write(json);
				writer.flush();
			} else {
				OutputStream out = response.getOutputStream();
				byte[] data = json.getBytes(this.encoding);
				out.write(data);
				out.flush();
			} // if
			
		} // if
		
	} // doExecute()

} // class JsonActionResult
