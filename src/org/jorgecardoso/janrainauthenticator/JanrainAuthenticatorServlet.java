package org.jorgecardoso.janrainauthenticator;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.*;

import org.mortbay.log.Log;
import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class JanrainAuthenticatorServlet extends HttpServlet {
	

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Rpx rpx = new Rpx("a9f4ceac4c1383f71f1797cf06813a67b2839b7e", "https://rpxnow.com");
		
		Element el = rpx.authInfo(req.getParameter("token"));
		
		String identifier = el.getElementsByTagName("identifier").item(0).getTextContent();
		String username = el.getElementsByTagName("preferredUsername").item(0).getTextContent();
		
		String redirectUrl = req.getParameter("redirect");
		int hashIndex = redirectUrl.indexOf("#");
		String hash = "";
		if ( -1 != hashIndex ) {
			hash = redirectUrl.substring(hashIndex);
			System.err.println("Hash: " + hash);
			redirectUrl = redirectUrl.substring(0, hashIndex);
		} else {
			System.err.println("No hash");
		}
		if ( null != redirectUrl) {
			if ( !redirectUrl.contains("?") ) {
				redirectUrl += "?";
			} else {
				redirectUrl += "&";
			}
			redirectUrl += "identifier=" + URLEncoder.encode(identifier, "UTF-8");
			redirectUrl +="&preferredUsername="+ URLEncoder.encode(username, "UTF-8");
			redirectUrl += hash;
			resp.sendRedirect( redirectUrl );
		} else {
			resp.setContentType("text/plain");
		
	
			resp.getWriter().println( "Identifier: " + identifier + "\n preferredUsername: " + username);
		}
	}
}
