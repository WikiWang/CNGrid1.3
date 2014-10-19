package cn.edu.buaa.cngrid;

import java.util.ArrayList;
import java.util.List;
import cn.edu.buaa.getjson.*;
import cn.edu.buaa.getdata.*;
/**
 * 功能描述：该类用于实现flex与普通java类中的方法通信
 * @author wk
 * 
 */

public class Service {
	
	List<HPC> HPC_list = new ArrayList<HPC>();
	List<Jobs> jobs_list = new ArrayList<Jobs>();
	String time;
	GetMapData gmd = new GetMapData();
	GetJson gj = new GetJson();
	
	public List<HPC> set_hpc(){				
		HPC_list = gmd.getProfiles();
		return HPC_list;
	}
	public List<Jobs> set_jobs(){
		jobs_list = gmd.getJobs();
		return jobs_list;		
	}
	public String set_time(){
		time = gmd.getTime();
		return time;
	}

	public void update(){
		gj.update();
	}

}