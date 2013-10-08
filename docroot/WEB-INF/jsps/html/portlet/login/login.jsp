<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="com.liferay.portal.kernel.captcha.CaptchaTextException" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.theme.ThemeDisplay" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<portlet:defineObjects />

<liferay-util:buffer var="html">
	<liferay-util:include page="/html/portlet/login/login.portal.jsp" />
</liferay-util:buffer>

<%
	ThemeDisplay themeDisplay =
		(ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

	if(themeDisplay.isSignedIn()) {
		out.print(html);
		return;
	}

	int errorPos = html.indexOf("<input name=\"_58_formDate\"");
	int captchaPos = html.indexOf("<label class=\"checkbox\" for=\"_58_rememberMeCheckbox\">");
	System.out.println(errorPos);
	System.out.println(captchaPos);
%>

<%= html.substring(0, errorPos) %>

<liferay-ui:error exception="<%= CaptchaTextException.class %>" message="text-verification-failed" />

<%= html.substring(errorPos, captchaPos) %>

<portlet:resourceURL var="captchaURL">
	<portlet:param name="struts_action" value="/login/captcha" />
</portlet:resourceURL>

<liferay-ui:captcha url="<%= captchaURL %>" />

<%= html.substring(captchaPos) %>