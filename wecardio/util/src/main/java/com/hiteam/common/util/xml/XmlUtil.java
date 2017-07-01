package com.hiteam.common.util.xml;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.SAXReader;

/**
 * xml宸ュ叿绫�
 * 
 */
public class XmlUtil {

	/**
	 * 鑺傜偣鏂囨湰鐨勫睘鎬у悕绉�
	 */
	public static final String VALUE_KEY_NAME = "$text";

	/**
	 * 杞崲xml瀛楃涓蹭负map<br>
	 * 灞炴�у拰瀛愯妭鐐瑰潎瀛樻斁浜庡悓涓猰ap閲岋紝瀵逛簬鍚屽悕灞炴�ф垨瀛愯妭鐐癸紝鍚堝苟涓簂ist瀵硅薄
	 * 
	 * @param xml
	 *            xml瀛楃涓�
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	public static Map<String, Object> xmlToMap(String xml)
			throws UnsupportedEncodingException, DocumentException {
		return xmlToMap(xml, false);
	}

	/**
	 * 杞崲xml瀛楃涓蹭负map<br>
	 * 灞炴�у拰瀛愯妭鐐瑰潎瀛樻斁浜庡悓涓猰ap閲岋紝瀵逛簬鍚屽悕灞炴�ф垨瀛愯妭鐐癸紝鍚堝苟涓簂ist瀵硅薄
	 * 
	 * @param xml
	 *            xml瀛楃涓�
	 * @param stringWhenNoAttrAndChild
	 *            閬囧埌鏃犲睘鎬у拰瀛愯妭鐐圭殑鑺傜偣锛屾槸鍚︾洿鎺ヨ繑鍥炴枃鏈��
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	public static Map<String, Object> xmlToMap(String xml,
			boolean stringWhenNoAttrAndChild)
			throws UnsupportedEncodingException, DocumentException {
		xml = preprocess(xml);
		String encoding = "UTF-8";
		xml = getXmlHeader(encoding) + xml;

		// 鑾峰緱瑙ｆ瀽鍣�
		SAXReader saxreader = new SAXReader();
		// 鑾峰緱document鏂囨。瀵硅薄
		Document doc = saxreader.read(new ByteArrayInputStream(xml
				.getBytes(encoding)));
		// 鑾峰緱鏍硅妭鐐�
		Element rootElement = doc.getRootElement();

		Map<String, Object> result = new LinkedHashMap<String, Object>();

		Object rootValue = elementToObject(rootElement,
				stringWhenNoAttrAndChild);
		result.put(rootElement.getName(), rootValue);

		return result;
	}

	/**
	 * 杞崲map涓簒ml瀛楃涓�<br>
	 * 1銆乵ap涓殑鍏冪礌鍊煎鏋滀负map绫诲瀷锛屽垯杞崲涓哄瓙鑺傜偣<br>
	 * 2銆乵ap涓殑鍏冪礌鍊煎鏋滀笉鏄痬ap鍜宭ist绫诲瀷锛屽垯杞崲涓哄睘鎬�<br>
	 * 3銆乵ap涓殑鍏冪礌鍊煎鏋滄槸list绫诲瀷锛屽垯鍙栧嚭list涓殑鍏冪礌锛屾寜鐓�1銆�2姝ュ鐞�
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToXml(Map<String, Object> map) {
		return mapToXml(map, false);
	}

	/**
	 * 杞崲map涓簒ml瀛楃涓�<br>
	 * 1銆乵ap涓殑鍏冪礌鍊煎鏋滀负map绫诲瀷锛屽垯杞崲涓哄瓙鑺傜偣<br>
	 * 2銆乵ap涓殑鍏冪礌鍊煎鏋滀笉鏄痬ap鍜宭ist绫诲瀷锛屽垯鏍规嵁ignoreAttribute鐨勫�艰浆鎹负瀛愯妭鐐规垨灞炴��<br>
	 * 3銆乵ap涓殑鍏冪礌鍊煎鏋滄槸list绫诲瀷锛屽垯鍙栧嚭list涓殑鍏冪礌锛屾寜鐓�1銆�2姝ュ鐞�
	 * 
	 * @param map
	 * @param ignoreAttribute
	 *            鏄惁灏唌ap瀵硅薄鍩烘湰绫诲瀷灞炴�ц浆涓哄瓙鑺傜偣锛岃�屼笉鏄厓绱犲睘鎬�
	 * @return
	 */
	public static String mapToXml(Map<String, Object> map,
			boolean ignoreAttribute) {
		return mapToXml(map, false, null);
	}

	/**
	 * 杞崲map涓簒ml瀛楃涓�<br>
	 * 1銆乵ap涓殑鍏冪礌鍊煎鏋滀负map绫诲瀷锛屽垯杞崲涓哄瓙鑺傜偣<br>
	 * 2銆乵ap涓殑鍏冪礌鍊煎鏋滀笉鏄痬ap鍜宭ist绫诲瀷锛屽垯杞崲涓哄睘鎬�<br>
	 * 3銆乵ap涓殑鍏冪礌鍊煎鏋滄槸list绫诲瀷锛屽垯鍙栧嚭list涓殑鍏冪礌锛屾寜鐓�1銆�2姝ュ鐞�
	 * 
	 * @param map
	 * @param header
	 *            鏄惁鍖呭惈xml澶撮儴
	 * @param encoding
	 *            澶撮儴缂栫爜
	 * 
	 * @return
	 */
	public static String mapToXml(Map<String, Object> map, boolean header,
			String encoding) {
		return mapToXml(map, false, header, encoding);
	}

	/**
	 * 杞崲map涓簒ml瀛楃涓�<br>
	 * 1銆乵ap涓殑鍏冪礌鍊煎鏋滀负map绫诲瀷锛屽垯杞崲涓哄瓙鑺傜偣<br>
	 * 2銆乵ap涓殑鍏冪礌鍊煎鏋滀笉鏄痬ap鍜宭ist绫诲瀷锛屽垯鏍规嵁ignoreAttribute鐨勫�艰浆鎹负瀛愯妭鐐规垨灞炴��<br>
	 * 3銆乵ap涓殑鍏冪礌鍊煎鏋滄槸list绫诲瀷锛屽垯鍙栧嚭list涓殑鍏冪礌锛屾寜鐓�1銆�2姝ュ鐞�
	 * 
	 * @param map
	 * @param ignoreAttribute
	 *            鏄惁灏唌ap瀵硅薄鍩烘湰绫诲瀷灞炴�ц浆涓哄瓙鑺傜偣锛岃�屼笉鏄厓绱犲睘鎬�
	 * @param header
	 *            鏄惁鍖呭惈xml澶撮儴
	 * @param encoding
	 *            澶撮儴缂栫爜
	 * 
	 * @return
	 */
	public static String mapToXml(Map<String, Object> map,
			boolean ignoreAttribute, boolean header, String encoding) {
		if (map == null || map.isEmpty()) {
			return "";
		}

		if (map.size() != 1) {
			throw new IllegalArgumentException("map鐨勫ぇ灏忎笉绛変簬1锛屾棤娉曡浆鎹负鏍硅妭鐐癸紒");
		}

		String rootName = null;
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			rootName = key;
		}

		Document doc = new DOMDocument();
		// doc.setXMLEncoding(encoding);
		Object rootData = map.get(rootName);
		Element root = null;
		if (rootData instanceof Map) {
			root = mapToElement(rootName, (Map<String, Object>) rootData,
					ignoreAttribute);
		} else {
			root = new DOMElement(rootName);
			root.addCDATA(rootData.toString());
		}

		doc.add(root);

		String xml = doc.asXML();

		xml = preprocess(xml);
		if (header) {
			xml = getXmlHeader(encoding) + xml;
		}

		return xml;
	}

	/**
	 * 鑾峰緱xml澹版槑澶�
	 * 
	 * @param encoding
	 *            缂栫爜
	 * @return xml澹版槑澶�
	 */
	public static String getXmlHeader(String encoding) {
		return "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
	}

	/**
	 * 杞崲map瀵硅薄涓哄厓绱�
	 * 
	 * @param elementName
	 *            鍏冪礌鍚嶇О
	 * @param map
	 *            map瀵硅薄
	 * @param ignoreAttribute
	 *            鏄惁灏唌ap瀵硅薄鍩烘湰绫诲瀷灞炴�ц浆涓哄瓙鑺傜偣锛岃�屼笉鏄厓绱犲睘鎬�
	 * 
	 * @return
	 */
	private static Element mapToElement(String elementName,
			Map<String, Object> map, boolean ignoreAttribute) {
		Element element = new DOMElement(elementName);

		for (String key : map.keySet()) {
			Object value = map.get(key);

			if (value != null) {
				if (VALUE_KEY_NAME.equals(key)) {
					element.addCDATA(value.toString());
				} else {
					if (value instanceof Map) {
						element.add(mapToElement(key,
								(Map<String, Object>) value, ignoreAttribute));
					} else if (value instanceof List) {
						for (Object o : (List<Object>) value) {
							if (o instanceof Map) {
								element.add(mapToElement(key,
										(Map<String, Object>) o,
										ignoreAttribute));
							} else {
								if (ignoreAttribute) {
									Element e = new DOMElement(key);
									e.addCDATA(o.toString());
									element.add(e);
								} else {
									element.addAttribute(key, o.toString());
								}
							}
						}
					} else {
						if (ignoreAttribute) {
							Element e = new DOMElement(key);
							e.addCDATA(value.toString());
							element.add(e);
						} else {
							element.addAttribute(key, value.toString());
						}
					}
				}
			}
		}

		return element;
	}

	/**
	 * 杞崲element瀵硅薄涓簃ap鎴栧瓧绗︿覆
	 * 
	 * @param element
	 * @param stringWhenNoAttrAndChild
	 *            閬囧埌鏃犲睘鎬у拰瀛愯妭鐐圭殑鑺傜偣锛屾槸鍚︾洿鎺ヨ繑鍥炴枃鏈��
	 * @return
	 */
	private static Object elementToObject(Element element,
			boolean stringWhenNoAttrAndChild) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();

		// 澶勭悊灞炴�у��
		List<Attribute> attrList = element.attributes();
		if (attrList != null) {
			for (Attribute attribute : attrList) {
				result.put(attribute.getName(), attribute.getValue());
			}
		}

		List<Element> children = element.elements();
		if (children != null && children.size() > 0) {// 瀛樺湪瀛愯妭鐐�
			for (Element child : children) {
				Object elementValue = elementToObject(child,
						stringWhenNoAttrAndChild);
				String name = child.getName();

				if (result.containsKey(name)) {
					Object value = result.get(name);
					if (value instanceof List) {
						((List<Object>) value).add(elementValue);
					} else {
						List<Object> list = new ArrayList<Object>();
						list.add(value);
						list.add(elementValue);
						result.put(name, list);
					}
				} else {
					result.put(name, elementValue);
				}
			}
		} else {
			String text = element.getText();
			if (stringWhenNoAttrAndChild && result.size() == 0) {// 娌℃湁灞炴�у��
				return text;
			}

			result.put(VALUE_KEY_NAME, text);
		}

		return result;
	}

	/**
	 * 棰勫鐞唜ml瀛楃涓�
	 * 
	 * @param xml
	 * @return
	 */
	private static String preprocess(String xml) {
		xml = xml.trim();

		if (xml.startsWith("<?")) {
			xml = xml.substring(xml.indexOf("?>") + 2);
		}

		return xml;
	}

	public static void main(String[] args) throws UnsupportedEncodingException,
			DocumentException {
		String xml = "<?xml version=\"1.0\" encoding=\"GBk\"?><cc><bb>123</bb></cc>";
		System.out.println(xml);
		Map<String, Object> map = xmlToMap(xml, true);
		System.out.println(map);
		System.out.println(mapToXml(map, true, true, "GBK"));
	}

}
