package XmlUtil;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by heavenick on 2015/6/11.
 */
public class XmlUtil {


    /**
     *
     * �� xml ת����map �����Ե������ļ���
     * @param str eg:<?xml version="1.0" encoding="utf-8"?><result><result_status>000</result_status><result_memo></result_memo><result_data><data>1436324056553</data></result_data></result>
     * @return
     */
    public static Map<String,Object> xml2Map(String str) {
        try {
            Document doc = DocumentHelper.parseText(str) ;

            Element root = doc.getRootElement();
            List<Element> list =  root.elements();

            if(list != null && list.size() > 0){
                return doc2MapRecursion(list);
            }else{
                Map<String,Object> result = new HashMap<String, Object>();
                result.put(root.getName() , root.getTextTrim());
                return result ;
            }

        } catch (DocumentException e) {

        }
        return null ;
    }

    private static Map<String,Object> doc2MapRecursion(List<Element> eles){
        Map<String,Object> result = new HashMap<String, Object>() ;
        for (Element element : eles) {
            Object obj = null ;
            List<Element> child = element.elements() ;

            obj = (child != null && child.size() > 0 ) ? doc2MapRecursion(child)  :  element.getTextTrim();

            Object resultObj = result.get(element.getName()) ;

            if(resultObj != null){
                List<Object> resultList = resultObj instanceof  List ? (List)resultObj:new ArrayList<Object>() ;
                resultList.add(obj) ;
                result.put(element.getName(), resultList) ;
            }else{
                result.put(element.getName() , obj) ;
            }

        }
        return result ;
    }

    /**
     * ��xml ת���� bean ����
     * @param str
     * @param types
     * @return
     */
    public static Object xml2Bean(String str ,Class<?>... types){
        XStream xStreamt = getXstream();

        // ָ���ڵ��Ӧ��class
        xStreamt.processAnnotations(types);

        // str�ǽ��յ���xml�ַ�����ע�⣬xml����Ľڵ��bean������һ��Ҫ�ܶ�Ӧ�ϣ���������쳣
        return  xStreamt.fromXML(str);
    }


    /**
     * ������ת���� xml  ��
     * @param data
     * @param types
     * @return
     */
    public static  String bean2Xml(Object data, Class<?>... types){
        XStream xStreamt  =   getXstream();

        // ָ���ڵ��Ӧ��class
        xStreamt.processAnnotations(types);

        return  xStreamt.toXML(data);
    }

    
    private static XStream getXstream() {
        XStream xStreamt  =  new XStream( new DomDriver("utf-8",new NoNameCoder()));

        xStreamt.registerConverter(new DateConverter("yyyyMMddHHmmss","yyyyMMdd", null, Locale.ENGLISH, TimeZone.getTimeZone("GMT+8"),false));

        xStreamt.ignoreUnknownElements();
        return xStreamt;
    }

}

/**
 * 
 * xml ת����bean ����ʱ��ʹ�� XStream �� ��Ҫʹ��XStream ��ע�� ��ʶ������Ϣ�� 
 * 
 */
@XStreamAlias("result")
public class Result {

    @XStreamAlias("result_status")
	private String result_status ;
	
	@XStreamAlias("result_memo")
	private String result_memo;
	
    
	private  String result_data ;


	public String getResult_status() {
		return result_status;
	}

	public void setResult_status(String result_status) {
		this.result_status = result_status;
	}

	public String getResult_memo() {
		return result_memo;
	}

	public void setResult_memo(String result_memo) {
		this.result_memo = result_memo;
	}

	public String getResult_data() {
		return result_data;
	}

	public void setResult_data(String result_data) {
		this.result_data = result_data;
	}
}
