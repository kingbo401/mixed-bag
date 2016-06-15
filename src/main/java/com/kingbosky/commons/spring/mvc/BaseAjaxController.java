package com.kingbosky.commons.spring.mvc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.kingbosky.commons.utils.JsonUtils;

public class BaseAjaxController extends BaseController{
	protected void outputJsonObject(HttpServletResponse response,Object o){
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            IOUtils.write(o.toString(), os, "UTF-8");
        } catch (IOException e) {
            logger.warn("write json error", e);
        } finally {
            IOUtils.closeQuietly(os);
        } 	
    }
	
	public void outJsonp(HttpServletRequest request, HttpServletResponse response, Object object) {
		String callback = request.getParameter("callback");
		String string = JsonUtils.objectToJson(object);
		if (callback != null && callback.length() > 0) {
			response.setContentType("text/javascript;charset=UTF-8");
			string = (new StringBuilder()).append(callback).append("(").append(string).append(")").toString();
		} else {
			response.setContentType("text/plain;charset=UTF-8");
		}
		out(response, string);
	}

	public void outJsonJsonp(HttpServletRequest request, HttpServletResponse response, Object object) {
		String callback = request.getParameter("callback");
		String string = JsonUtils.objectToJson(object);
		if (callback != null && callback.length() > 0) {
			response.setContentType("text/javascript;charset=UTF-8");
			string = (new StringBuilder()).append(callback).append("(").append(string).append(")").toString();
		} else {
			response.setContentType("application/json;charset=UTF-8");
		}
		out(response, string);
	}

    protected void out(HttpServletResponse response, String string) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(string);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}
}
