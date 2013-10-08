/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package cz.topolik.login;

import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletURLFactoryUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tomas Polesovsky
 */
public class CaptchaChecker {

	public boolean check(
		ActionRequest request, ActionResponse response)
		throws Exception {

		try {

			CaptchaUtil.check(request);

		} catch (CaptchaTextException e) {
			SessionErrors.add(request, CaptchaTextException.class, e);

			HttpServletRequest httpServletRequest =
				PortalUtil.getHttpServletRequest(request);

			HttpServletResponse httpServletResponse =
				PortalUtil.getHttpServletResponse(response);

			postProcessAuthFailure(httpServletRequest, httpServletResponse);

			return false;
		}

		return true;
	}

	public boolean check(
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		try {

			CaptchaUtil.check(request);

		} catch (CaptchaTextException e) {
			SessionErrors.add(request, CaptchaTextException.class, e);

			postProcessAuthFailure(request, response);

			return false;
		}

		return true;
	}

	protected void postProcessAuthFailure(
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		Layout layout = (Layout)request.getAttribute(WebKeys.LAYOUT);

		PortletURL portletURL = PortletURLFactoryUtil.create(
			request, PortletKeys.LOGIN, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("saveLastPath", Boolean.FALSE.toString());

		portletURL.setWindowState(WindowState.MAXIMIZED);

		response.sendRedirect(portletURL.toString());
	}
}
