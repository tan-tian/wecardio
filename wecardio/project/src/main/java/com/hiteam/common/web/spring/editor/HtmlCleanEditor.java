package com.hiteam.common.web.spring.editor;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;

import java.beans.PropertyEditorSupport;

/**
 * HTML清理

 *
 */
public class HtmlCleanEditor extends PropertyEditorSupport {

	private boolean trim;			// 是否去除首尾空白
	private boolean emptyAsNull;	// 空值是否转为null
	//再默认的基础上，去掉了rel属性的控制，避免微信的链接无法跳转的问题
	private Whitelist whitelist = new Whitelist()
			.addTags(
					"a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em",
					"i", "li", "ol", "p", "pre", "q", "small", "strike", "strong", "sub",
					"sup", "u", "ul")
			.addAttributes("a", "href")
			.addAttributes("blockquote", "cite")
			.addAttributes("q", "cite")
			.addProtocols("a", "href", "ftp", "http", "https", "mailto")
			.addProtocols("blockquote", "cite", "http", "https")
			.addProtocols("cite", "cite", "http", "https")
			.addTags("img")
			.addAttributes("img", "align", "alt", "height", "src", "title", "width")
			.addProtocols("img", "src", "http", "https")
			.addAttributes("a", "target");	// 白名单

	public HtmlCleanEditor(boolean trim, boolean emptyAsNull) {
		this.trim = trim;
		this.emptyAsNull = emptyAsNull;
	}

	public HtmlCleanEditor(boolean trim, boolean emptyAsNull, Whitelist whitelist) {
		this.trim = trim;
		this.emptyAsNull = emptyAsNull;
		this.whitelist = whitelist;
	}

	@Override
	public String getAsText() {
		Object obj = getValue();
		return obj != null ? obj.toString() : "";
	}

	@Override
	public void setAsText(String text) {
		if (text != null) {
			String str = this.trim ? text.trim() : text;
			// 过滤html字符，保留换行
			str = Jsoup.clean(str, "", this.whitelist, new OutputSettings().prettyPrint(false));
			// 取消转义
			str = StringEscapeUtils.unescapeHtml(str);
			if ((this.emptyAsNull) && ("".equals(str))) {
				str = null;
			}
			setValue(str);
		} else {
			setValue(null);
		}
	}
}
