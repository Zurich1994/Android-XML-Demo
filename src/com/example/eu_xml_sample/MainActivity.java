 package com.example.eu_xml_sample;

import java.util.List;

 
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {
	 ListView listView;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_listview);
			listView = (ListView) findViewById(R.id.listView);
			HttpTask0 task = new HttpTask0(this, 2);
			task.execute("http://192.168.1.103:8084/Mobile01_SimpleHttpServer/xml?type=list");
			
		}
		
		public void updateUI(List<String> data){
			MyListAdapter adapter = new MyListAdapter(data, this);
			listView.setAdapter(adapter);
			
		}
}
