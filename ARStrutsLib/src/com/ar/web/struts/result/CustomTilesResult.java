package com.ar.web.struts.result;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;

import com.opensymphony.xwork2.ActionInvocation;

/*****************************************************
 * <pre>
 * Action결과를 Tiles를 이용해 출력하기 위한 Result 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *****************************************************/
public class CustomTilesResult extends StrutsResultSupport {

	private static final long serialVersionUID = -4948028026457337547L;
	
	private String bodyContent;
	public void setBodyContent(String bodyContent){
		this.bodyContent = bodyContent;
	} // setBodyContent

	@Override
	protected void doExecute(String location, ActionInvocation invocation) throws Exception {
		setLocation(location);
        
        ServletContext servletContext = ServletActionContext.getServletContext();
        TilesContainer container = TilesAccess.getContainer(servletContext);

        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        AttributeContext attributeContext = container.getAttributeContext(request, response);
        attributeContext.putAttribute("content", new Attribute(bodyContent));

        container.render(location, request, response);
	} // doExecute()

} // class CustomTilesResult
