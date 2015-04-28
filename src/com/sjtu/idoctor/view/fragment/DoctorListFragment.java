package com.sjtu.idoctor.view.fragment;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sjtu.idoctor.R;
import com.sjtu.idoctor.model.DoctorCacheBean;
import com.sjtu.idoctor.utils.DBUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class DoctorListFragment extends Fragment {

	private ListView lv;
	private ArrayAdapter<String> adapter;
	private Button chooseBtn;
	private Button cancelBtn;
	private Bitmap bitmap;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private DBUtils dbu;
	private List<DoctorCacheBean> docEntity;
	private List<String> doclist = new ArrayList<String>();
	DoctorCacheBean doc = null;
	private String docName = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_doctorlist, container, false);
		lv = (ListView) v.findViewById(R.id.doc_listview);
		chooseBtn = (Button) v.findViewById(R.id.choose_button);
		cancelBtn = (Button) v.findViewById(R.id.cancel_button);
		
		preferences = getActivity().getSharedPreferences("Doctor", Activity.MODE_PRIVATE);
		
		dbu = new DBUtils(preferences);
		
		new GetDoctorListThread().start();
		
		v.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				docName = parent.getItemAtPosition(position).toString();
			}
			
		});
		chooseBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doc = findDoctorByName(docName);
				editor = preferences.edit();
				if(doc != null && docName!=""){
					editor.putString("doctorName", docName);
					editor.putInt("doctorId", doc.getStaffId());
					editor.commit();
					try {
						URL url=new URL(doc.getPhotoSrc());
						InputStream is= url.openStream();
						bitmap = BitmapFactory.decodeStream(is);
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImageView ph = (ImageView) getActivity().findViewById(R.id.doctor_photo);
					ph.setImageBitmap(bitmap);
					TextView t = (TextView) getActivity().findViewById(R.id.doctor_title);
					t.setText("当前医生："+docName);
				}
				
				getActivity().onBackPressed();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().onBackPressed();
			}
		});
		return v; 
	}
	
	private class GetDoctorListThread extends Thread{
		@Override
		public void run(){
			Message msg = new Message();
			docEntity = dbu.getDoctors();
			msg.what=1;
			handler.sendMessage(msg);
		}
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what==1){
				for(int i=0; i<docEntity.size(); i++){
					doclist.add(docEntity.get(i).getName());
				}
				adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, doclist);
				lv.setAdapter(adapter);
				lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				lv.setSelected(true);
				lv.setSelection(0);
				lv.setItemChecked(0, true);
				docName  = doclist.get(0);
			}
		}
	};
	
	public DoctorCacheBean findDoctorByName(String docname){
		DoctorCacheBean d = null;
		for(int i=0; i<docEntity.size(); i++){
			if(docEntity.get(i).getName().equals(docname))
				d = docEntity.get(i);
		}
		return d;
	}
	
}
