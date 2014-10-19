package cn.edu.buaa.getdata;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.buaa.cngrid.*;

/**
 * 
 * @author wk
 * 将本地缓存的json格式文件转化为java类对象
 */
public class GetMapData
{
	private static final String fileName = "../webapps/CNGrid1.3/data.json";      //本地数据缓存
	private static final String timeFileName = "../webapps/CNGrid1.3/time.txt";   //后台刷新时间
	
	private List<HPC> profiles = new ArrayList<HPC>();
	private List<Jobs> jobs = new ArrayList<Jobs>();
	private String time;
	
	private static JSONArray profile;
	private static JSONArray job;

	
	public GetMapData(){
		explain();
	}
	
	private void explain(){
		List<HPC> tempProfiles = new ArrayList<HPC>();
		List<Jobs> tempJobs = new ArrayList<Jobs>();
		String content = "";
		File file = new File(fileName);
		File timeFile = new File(timeFileName);		
		try {
			BufferedReader readTime = new BufferedReader(new InputStreamReader(new FileInputStream(timeFile),"UTF-8"));
			time = readTime.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}//一次读入一行，直到读入null为文件结束  
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String line=null;
	        JSONObject js = null;
	        try {
				while((line=bf.readLine())!=null){
					content = content + line;
				}
				js = JSONObject.fromObject(content);
				profile = (JSONArray)js.get("profiles");
				job = (JSONArray)js.get("jobs");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for(int i=0;i<profile.size();i++){
			tempProfiles.add((HPC)JSONObject.toBean((JSONObject)profile.get(i), HPC.class));
		}
		for(int i=0;i<job.size();i++){
			tempJobs.add((Jobs)JSONObject.toBean((JSONObject)job.get(i), Jobs.class));
			tempJobs.get(i).setUser(tempJobs.get(i).getUser().substring(0, 3)+"*");
		}
		profiles = tempProfiles;
		jobs = tempJobs;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}

