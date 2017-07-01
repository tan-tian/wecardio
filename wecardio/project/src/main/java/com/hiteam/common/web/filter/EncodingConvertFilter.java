package com.hiteam.common.web.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * 编码过滤器
 */
public class EncodingConvertFilter extends OncePerRequestFilter {
	
	private String fromEncoding = "ISO-8859-1";	// 原编码格式
	private String toEncoding = "UTF-8";		// 目标编码格式

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		if (request.getMethod().equalsIgnoreCase("GET")) {
			Iterator<?> it = request.getParameterMap().values().iterator();
			while (it.hasNext()) {
				String[] strs = (String[]) it.next();
				for (int i = 0, l = strs.length; i < l; i++) {
					try {
						strs[i] = new String(strs[i].getBytes(this.fromEncoding), this.toEncoding);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	/*
	 * =-------------------------------------------------------------=
	 * get & set methods
	 * =-------------------------------------------------------------=
	 */
	public String getFromEncoding() {
		return fromEncoding;
	}

	public void setFromEncoding(String fromEncoding) {
		this.fromEncoding = fromEncoding;
	}

	public String getToEncoding() {
		return toEncoding;
	}

	public void setToEncoding(String toEncoding) {
		this.toEncoding = toEncoding;
	}

}
