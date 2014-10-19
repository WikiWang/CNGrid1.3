package cn.edu.buaa.getjson;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;


import cn.edu.buaa.cngrid.HPC;
import cn.edu.buaa.cngrid.Jobs;

public class GetJson implements Runnable{

	
	public static String URL_login;
	public static String URL_display;
	public static String URL_logout;
	public static String URL_update; 
	
	private static final String fileName = "../webapps/CNGrid1.3/data.json";
	private static final String timeFileName = "../webapps/CNGrid1.3/time.txt";
	private static final String timeInterval = "../webapps/CNGrid1.3/timeInterval.txt";
	private static final String url_interface = "../webapps/CNGrid1.3/url_interface.properties";
	
	private List<HPC> profiles = new ArrayList<HPC>();
	private List<Jobs> jobs = new ArrayList<Jobs>();
	private static int N = 600000;

	/**
	 * 
	 * ��̨�߳�������ʱ��rest�ӿڻ�ȡ���ݴ浽���ػ���
	 * 
	 */

	public void run(){
		File Interval = new File(timeInterval);	
		Properties properties = new Properties();
		Boolean flag = true;  //����ļ��Ƿ��
		while(flag){
			try {
				BufferedReader readInterval = new BufferedReader(new InputStreamReader(new FileInputStream(Interval),"UTF-8"));
				N = Integer.parseInt(readInterval.readLine());
				System.out.println(N);
				InputStream inputStream = new FileInputStream(url_interface);  
	            properties.load(inputStream);  
	            inputStream.close(); //�ر���  
	            URL_login = properties.getProperty("URL_login");  
	            URL_display = properties.getProperty("URL_display");  
	            URL_logout = properties.getProperty("URL_logout");  
	            URL_update = properties.getProperty("URL_update"); 
				flag=false;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("�Զ�ˢ��ʱ�������ļ�(timeInterval.txt)��ʧ�ܣ�");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		while(true){
			GetMapData(URL_display);
			try {
				Thread.sleep(N);
				System.out.println(N);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void GetMapData(String url){
		List<Cookie> cookies = new ArrayList<Cookie>();		
		cookies=login(URL_login);//��ȡcookies
		writeToFile(cookies, url);//����ȡ��json��ʽ�ļ��浽����
		}
	
	private void writeToFile(List<Cookie> cookies, String url){
		JSONObject json = null;	
		json = get(url,cookies);	//��ȡjson��ʽ����	
		File file = new File(fileName);
		File timeFile = new File(timeFileName);
		boolean flag = true;
		while(flag){
			try {
				BufferedWriter write = new BufferedWriter(new FileWriter(file));
				BufferedWriter writeTime = new BufferedWriter(new FileWriter(timeFile));
				write.write(json.toString());
				write.close();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//�������ڸ�ʽ
				writeTime.write(df.format(new Date()));
				writeTime.close();
				flag = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("�����ļ�data.json��ʧ��");
				e.printStackTrace();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static List<Cookie> login(String url){
		CookieStore cookieStore = new BasicCookieStore();
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		HttpClient httpClient = HttpsClient.newHttpsClient();
		HttpGet httpGet = new HttpGet(url);
		boolean flag = true;
		while(flag){
			try {
				HttpResponse httpResponse = httpClient.execute(httpGet,context);
				HttpEntity e = httpResponse.getEntity();
				flag = false;
			} catch (Exception e1) {
				System.out.println("��������ʧ��!");
//				e1.printStackTrace();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
		List<Cookie> cookies = cookieStore.getCookies();
		return cookies;
	}
	
	public static JSONObject get(String url,List<Cookie> cookies ){
		HttpClient httpClient = HttpsClient.newHttpsClient();
		HttpClientContext context = HttpClientContext.create();
		HttpGet httpGet = new HttpGet(url);
		JSONObject json = null;
		httpGet.addHeader("Cookie", cookies.get(0).getName()+"="+cookies.get(0).getValue());
		boolean flag = true;
		while(flag){
			try {
				HttpResponse httpResponse = httpClient.execute(httpGet,context);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
	                HttpEntity entity = httpResponse.getEntity(); 
	                String res="";
	                BufferedReader bf ;
	                try {
	                	bf = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
	                	String line=null;
	                	JSONObject js = null;
	                	while((line=bf.readLine())!=null){
	                		res = res + line;
	                	}
						js = JSONObject.fromObject(res);
						if((js.get("status_code").toString()).equals("0")){
							json = (JSONObject)js.get("mapdata");
						}
						else{
							System.out.println(js.get("status_str"));
						}
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}  
	            } 
				flag = false;
			} catch (Exception e) {
				System.out.println("�����ж�");
				e.printStackTrace();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return json;
	}
	
	/**
	 * logout��δ�õ�
	 * @param url
	 */
	public static void logout(String url){
		HttpClient httpClient = HttpsClient.newHttpsClient();
		HttpClientContext context = HttpClientContext.create();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet,context);
			HttpEntity e = httpResponse.getEntity();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public List<HPC> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<HPC> profiles) {
		this.profiles = profiles;
	}

	public List<Jobs> getJobs() {
		return jobs;
	}
	public void setJobs(List<Jobs> jobs) {
		this.jobs = jobs;
	}


	public void update() {
		GetMapData(URL_update);
	}

}
