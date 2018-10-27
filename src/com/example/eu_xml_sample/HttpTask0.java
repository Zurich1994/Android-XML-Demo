package com.example.eu_xml_sample;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
 

 
public class HttpTask0 extends AsyncTask<String, Void, byte[]> {
   private MainActivity activity;
   private int type ;
	
   public HttpTask0(MainActivity activity, int type) {
		super();
		this.activity = activity;
		this.type = type;
	}



	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		Toast.makeText(activity, "我要发请求了", Toast.LENGTH_LONG).show();
	}

	

	@Override
	protected byte[] doInBackground(String... params) {
		// TODO Auto-generated method stub
		//byte[] result = httpClientGet(params[0]);
		
		//由于同学们暂时无法连接服务器，此处模拟网络请求返回一个xml的 byte[]
		byte[] result = null ;
		try {
			InputStream in = activity.getAssets().open("list.xml");//加载数据源
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf =new byte[1024];
			while(true){
					int len = in.read(buf);
					if(len == -1) break;
					out.write(buf, 0, len);
			}
			 result = out.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//模拟结束
		 
		return result;
	}
	
	@Override
	protected void onPostExecute(byte[] result) {
		if(result==null) return;
		super.onPostExecute(result);
		 
			List<String> list =  parseListXML(result);
			activity.updateUI(list);
			
		 
    }
	
	 
	
	private void parseSingleXML(byte[] result){
		
		try {
			 
			File f = new File(activity.getFilesDir(),"single.xml");
			FileOutputStream   file = new FileOutputStream(f);
			file.write(result);
			file.close();
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			Element root = doc.getDocumentElement();
			NodeList nodeList = root.getChildNodes();
			for(int i = 0 ; i <nodeList.getLength();i++){
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE){
					Element e = (Element) node;
					if(e.getTagName().equals("info")){
						String studentNo =e.getAttribute("studentNo");
						String name =e.getAttribute("name");
						 
					 
						Log.e("studentNo",studentNo);
						Log.e("name",name);
					}else if(e.getTagName().equals("photo")){
						Log.e(e.getTagName(),e.getTextContent());
					 
					}else if(e.getTagName().equals("level")){
						Log.e(e.getTagName(),e.getTextContent());
					}
				 }
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private List<String> parseListXML(byte[] result){
		List<String> list = new ArrayList<String>();
		try {
			File f = new File(activity.getFilesDir(),"list.xml");
			FileOutputStream   file = new FileOutputStream(f);
			file.write(result);
			file.close();
			//1. 使用DocumentBuilderFactory创建DOM解析器
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			//3. 获取XML文档解析入口
			Document doc = builder.parse(f);
			//4.获取根节点
			Element root = doc.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("article");
			for(int i = 0 ; i <nodeList.getLength();i++){
				Node node = nodeList.item(i);
				 
				if(node.getNodeType() == Node.ELEMENT_NODE){
					Element e = (Element) node;
					String id =e.getAttribute("id");
					String logo =e.getAttribute("logo");
				    list.add(logo);
					Log.e("id",id);
					Log.e("logo",logo);
					NodeList childList = e.getChildNodes();//获取子节点Node
					for(int j = 0 ; j < childList.getLength();j++){
						Node child = childList.item(j);
						if(child.getNodeType() == Node.ELEMENT_NODE){
							Element childElement = (Element) child;
							//Log.e("tag",childElement.getTagName());
							Log.e(childElement.getTagName(),childElement.getTextContent());
						}
					}
				}
				//list.add(a);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private byte[] httpClientGet(String url) {

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(URI.create(url));
			HttpResponse resp = client.execute(request);
			HttpEntity resEntity = resp.getEntity();
			byte[] result = EntityUtils.toByteArray(resEntity);
			return result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
