package com.example.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	boolean isConnected = false;
	private Socket socket;
	BufferedReader in;
	private static final int SERVERPORT = 4444;
	private static final String SERVER_IP = "192.168.0.1";
	int state = 0;
	// 0 means listview
	// 1 means settings
	// 2 means GPS setting

	// Sample ephemeris data
	String eData = "111,2,224,26560300,0.000000188127,-0.000000100583,"
			+ "321.656,87.6875,0.00000436418,0.00000270829,0.00000000504521,"
			+ "0.0139305,0.000000000411089,0.950462,-2.62555,0.000145859,"
			+ "-2.56865,-0.00000000843857,1.75048,0.999903,5153.67,240704,"
			+ "2,797,32,0,0.000000000931323,224,240704,-0.000158074,"
			+ "-0.00000000000250111,0";

	// Listview variables
	private Set<View> fuzeSet = new HashSet<View>();
	private ArrayList<String> roundList = new ArrayList<String>();
	private ArrayList<String> stringList = new ArrayList<String>();
	private Set<Integer> selectedPos = new HashSet<Integer>();
	ListView listview;
	StableArrayAdapter adapter;
	Button setSelected;
	TextView connecting;

	// Setting variables 
	Button impact, delay, nearground, prox, setGPS, setNoGPS, back;
	int newSetting = 4;

	// GPS variables
	EditText twoletter, twelvedigit, elevation;
	TextView twoletterinst, twelvedigitinst, elevationinst;
	Button submitGPS;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Thread(new ClientThread()).start();
		new Thread(new RecvThread()).start();

		// List view setup
		listview = (ListView) findViewById(R.id.listview);
		adapter = new StableArrayAdapter(this,R.layout.mylist, stringList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView item = (TextView) listview.getChildAt(position);
				if (!fuzeSet.contains(item)) {
					item.setBackgroundColor(0xFFA08E5C);
					fuzeSet.add(item);
					selectedPos.add(Integer.parseInt(item.getText()
							.subSequence(4, 5).toString()));
				} else {
					item.setBackgroundColor(0x00000000);
					fuzeSet.remove(item);
					selectedPos.remove(Integer.parseInt(item.getText()
							.subSequence(4, 5).toString()));
				}
			}
		});
		listview.setVisibility(View.GONE);
		back = (Button) findViewById(R.id.back);
		back.setBackgroundResource(android.R.drawable.btn_default);
		back.setVisibility(View.GONE);
		back.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				state = 0;
				setGPS.setVisibility(View.GONE);
				setNoGPS.setVisibility(View.GONE);
				impact.setVisibility(View.GONE);
				delay.setVisibility(View.GONE);
				nearground.setVisibility(View.GONE);
				prox.setVisibility(View.GONE);
				twoletterinst.setVisibility(View.GONE);
				twoletter.setVisibility(View.GONE);
				twelvedigitinst.setVisibility(View.GONE);
				twelvedigit.setVisibility(View.GONE);
				elevationinst.setVisibility(View.GONE);
				elevation.setVisibility(View.GONE);
				submitGPS.setVisibility(View.GONE);
				back.setVisibility(View.GONE);
				setSelected.setVisibility(View.VISIBLE);
				listview.setVisibility(View.VISIBLE);
			}
		});
		setSelected = (Button) findViewById(R.id.setselected);
		setSelected.setBackgroundResource(android.R.drawable.btn_default);
		setSelected.setVisibility(View.GONE);
		setSelected.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!fuzeSet.isEmpty()){
					state = 1;
					setSelected.setVisibility(View.GONE);
					listview.setVisibility(View.GONE);
					setGPS.setVisibility(View.VISIBLE);
					setNoGPS.setVisibility(View.VISIBLE);
					impact.setVisibility(View.VISIBLE);
					delay.setVisibility(View.VISIBLE);
					nearground.setVisibility(View.VISIBLE);
					prox.setVisibility(View.VISIBLE);
					back.setVisibility(View.VISIBLE);
					impact.setBackgroundResource(android.R.drawable.btn_default);
					delay.setBackgroundResource(android.R.drawable.btn_default);
					nearground.setBackgroundResource(android.R.drawable.btn_default);
					prox.setBackgroundResource(android.R.drawable.btn_default);
				}
			}
		});

		// Settings setup
		setGPS = (Button) findViewById(R.id.setgps);
		setGPS.setBackgroundResource(android.R.drawable.btn_default);
		setGPS.setVisibility(View.GONE);
		setNoGPS = (Button) findViewById(R.id.setnogps);
		setNoGPS.setBackgroundResource(android.R.drawable.btn_default);
		setNoGPS.setVisibility(View.GONE);
		impact = (Button) findViewById(R.id.myImpact);
		impact.setBackgroundResource(android.R.drawable.btn_default);
		impact.setVisibility(View.GONE);
		delay = (Button) findViewById(R.id.myDelay);
		delay.setBackgroundResource(android.R.drawable.btn_default);
		delay.setVisibility(View.GONE);
		nearground = (Button) findViewById(R.id.myNearGround);
		nearground.setBackgroundResource(android.R.drawable.btn_default);
		nearground.setVisibility(View.GONE);
		prox = (Button) findViewById(R.id.myProx);
		prox.setBackgroundResource(android.R.drawable.btn_default);
		prox.setVisibility(View.GONE);
		setGPS.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				state = 2;
				setGPS.setVisibility(View.GONE);
				setNoGPS.setVisibility(View.GONE);
				impact.setVisibility(View.GONE);
				delay.setVisibility(View.GONE);
				nearground.setVisibility(View.GONE);
				prox.setVisibility(View.GONE);
				back.setVisibility(View.VISIBLE);
				twoletterinst.setVisibility(View.VISIBLE);
				twoletter.setVisibility(View.VISIBLE);
				twelvedigitinst.setVisibility(View.VISIBLE);
				twelvedigit.setVisibility(View.VISIBLE);
				elevationinst.setVisibility(View.VISIBLE);
				elevation.setVisibility(View.VISIBLE);
				submitGPS.setVisibility(View.VISIBLE);
			}
		});
		setNoGPS.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (newSetting == 4){
					Context context = getApplicationContext();
					CharSequence text = "Select a setting!";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					return;
				}
				StringBuilder sb = new StringBuilder();
				sb.append("<list>");
				for (int round : selectedPos) {
					sb.append(round + "," + newSetting
							+ ",AA000000000000,00000;");
				}
				sb.append("</list>");
				sendString(sb.toString());
				newSetting = 4;
				state = 0;
				setGPS.setVisibility(View.GONE);
				setNoGPS.setVisibility(View.GONE);
				impact.setVisibility(View.GONE);
				delay.setVisibility(View.GONE);
				nearground.setVisibility(View.GONE);
				prox.setVisibility(View.GONE);
				back.setVisibility(View.GONE);
				setSelected.setVisibility(View.VISIBLE);
				listview.setVisibility(View.VISIBLE);
			}
		});
		impact.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				newSetting = 0;
				impact.setBackgroundColor(0xFFA08E5C);
				delay.setBackgroundResource(android.R.drawable.btn_default);
				nearground.setBackgroundResource(android.R.drawable.btn_default);
				prox.setBackgroundResource(android.R.drawable.btn_default);
			}
		});

		delay.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				newSetting = 1;
				impact.setBackgroundResource(android.R.drawable.btn_default);
				delay.setBackgroundColor(0xFFA08E5C);
				nearground.setBackgroundResource(android.R.drawable.btn_default);
				prox.setBackgroundResource(android.R.drawable.btn_default);
			}
		});

		nearground.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				newSetting = 2;
				impact.setBackgroundResource(android.R.drawable.btn_default);
				delay.setBackgroundResource(android.R.drawable.btn_default);
				nearground.setBackgroundColor(0xFFA08E5C);
				prox.setBackgroundResource(android.R.drawable.btn_default);
			}
		});

		prox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				newSetting = 3;
				impact.setBackgroundResource(android.R.drawable.btn_default);
				delay.setBackgroundResource(android.R.drawable.btn_default);
				nearground
						.setBackgroundResource(android.R.drawable.btn_default);
				prox.setBackgroundColor(0xFFA08E5C);
			}
		});

		// GPS setup
		twoletterinst = (TextView) findViewById(R.id.areaDesig);
		twoletterinst.setVisibility(View.GONE);
		twoletter = (EditText) findViewById(R.id.editTwoLetter);
		twoletter.setVisibility(View.GONE);
		twelvedigitinst = (TextView) findViewById(R.id.digitgrid);
		twelvedigitinst.setVisibility(View.GONE);
		twelvedigit = (EditText) findViewById(R.id.editTwelveDigit);
		twelvedigit.setVisibility(View.GONE);
		elevationinst = (TextView) findViewById(R.id.elevationtext);
		elevationinst.setVisibility(View.GONE);
		elevation = (EditText) findViewById(R.id.elevation);
		elevation.setVisibility(View.GONE);
		submitGPS = (Button) findViewById(R.id.submitgps);
		submitGPS.setBackgroundResource(android.R.drawable.btn_default);
		submitGPS.setVisibility(View.GONE);
		submitGPS.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String elevationStr = elevation.getText().toString();
				if (twelvedigit.getText().toString().length() != 12){
					Context context = getApplicationContext();
					CharSequence text = "Coordinates must be 12 digits!";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					return;
				}
				if (twoletter.getText().toString().length() != 2){
					Context context = getApplicationContext();
					CharSequence text = "Map designator must be 2 letters!";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					return;
				}
				while (elevationStr.length() < 5) {
					elevationStr = "0" + elevationStr;
				}
				StringBuilder sb = new StringBuilder();
				sb.append("<list>");
				for (int round : selectedPos) {
					sb.append(round + "," + newSetting + ","
							+ twoletter.getText().toString()
							+ twelvedigit.getText().toString() + ","
							+ elevationStr + ";");
				}
				sb.append("</list>");
				sendString(sb.toString());
				state = 0;
				twoletterinst.setVisibility(View.GONE);
				twoletter.setVisibility(View.GONE);
				twelvedigitinst.setVisibility(View.GONE);
				twelvedigit.setVisibility(View.GONE);
				elevationinst.setVisibility(View.GONE);
				elevation.setVisibility(View.GONE);
				submitGPS.setVisibility(View.GONE);
				back.setVisibility(View.GONE);
				setSelected.setVisibility(View.VISIBLE);
				listview.setVisibility(View.VISIBLE);
			}
		});
	}

	public void onStart() {
		super.onStart();
		new Thread(new sendEphemerisThread()).start();
	}

	public void sendString(String message) {
		//Tests if the message being sent is ephemeris data or matches the setting protocol
		//if (message != eData){
		//	if (!message.matches("\\<list\\>(/d,/d,[A-Z]{2}(/d){12},(/d){5};)*\\<\\list\\>")){
		//		return;
		//	}
		//}
		// if it passes those two tests, continue to send
		try {
			String str = message;	
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
			out.print(str);
			out.flush();			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ClientThread implements Runnable {

		@Override
		public void run() {
			connecting = (TextView) findViewById(R.id.connecting);
			while (true) {
				try {
					InetSocketAddress serverAddr = new InetSocketAddress(
							SERVER_IP, SERVERPORT);
					socket = new Socket();
					socket.bind(null);
					socket.connect(serverAddr, 10000);
					break;
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			isConnected = true;

			new Thread() {
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							connecting.setText("Connected");
							setSelected.setVisibility(View.VISIBLE);
							listview.setVisibility(View.VISIBLE);
						}
					});
				}
			}.start();
		}

	}

	class sendEphemerisThread implements Runnable {
		//Thread that continuously runs until the tablet connects
		// at which point the ephemeris data is sent to the magazine
		// for distribution to the rounds
		@Override
		public void run() {
			while (true) {
				if (isConnected) {
					try {
						String str = eData;
						PrintWriter out1 = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										socket.getOutputStream())), true);
						out1.print(str);
						out1.flush();
						return;
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	class RecvThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				if (isConnected) {
					try {
						in = new BufferedReader(new InputStreamReader(
								socket.getInputStream()));
						String fromServer = in.readLine();
						if (fromServer != null) {
							String begin = fromServer.substring(0, 6);
							if (begin.equals("<list>")) {
								// Clear roundlist and add rounds received to
								// new roundlist
								roundList.clear();
								stringList.clear();
								String newFromServer1 = fromServer.substring(6);
								String[] list1 = newFromServer1.split(";");
								for (String item : list1) {
									if (!item.equals("</list>")) {
										//if (item.matches("/d,/d,[A-Z]{2}(/d){12},(/d){5}")){
											roundList.add(item);
											addListView(item);
										//}
									}
								}
								// Update listview on the UI thread.
								// Android requires that only UI threads can
								// touch views
								new Thread() {
									public void run() {
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												updateListView();
											}
										});
									}
								}.start();
							}
						}
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (OutOfMemoryError e) {
						// uh oh
					}
				}
			}
		}

		private void addListView(String item) {
			String id = item.substring(0,1);
			String set = item.substring(2,3);
			String gps = item.substring(4, 18);
			String elev = item.substring(19);
			String setting = "";
			if (set.equals("0")){
				setting = "Impact";
			} else if (set.equals("1")){
				setting = "Delay";
			} else if (set.equals("2")){
				setting = "Near Ground";
			} else if (set.equals("3")){
				setting = "Proximity";
			}
			stringList.add("ID: " + id + "   Setting: " + setting + "\n" + "Grid Coord: " + gps + "   Elevation: " + elev);
		}

	}

	public void updateListView() {
		listview.setVisibility(View.VISIBLE);
		StableArrayAdapter adapter = new StableArrayAdapter(this,
				R.layout.mylist, stringList);
		listview.setAdapter(adapter);
		fuzeSet.clear();
		selectedPos.clear();
		/*if (listview.getCount() > 0){
			Log.w("com.example.client", "size=" + String.valueOf(listview.getCount()));
			Log.w("com.example.client", "first visible=" + String.valueOf(listview.getFirstVisiblePosition()));
			for (int i = 0; i < listview.getCount(); i++) {
				Log.w("com.example.client", "accessing child at=" + String.valueOf(i));
				TextView item = (TextView) listview.getChildAt(i);
				item.setBackgroundColor(0x00000000);
				String setting = item.getText().subSequence(17,18).toString();
				if (setting.equals("I")){
					item.setCompoundDrawablesWithIntrinsicBounds(R.drawable.impact, 0, 0, 0);
				} else if (setting.equals("D")){
					item.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delay, 0, 0, 0);
				} else if (setting.equals("N")){
					item.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nearground, 0, 0, 0);
				} else if (setting.equals("P")){
					item.setCompoundDrawablesWithIntrinsicBounds(R.drawable.proximity, 0, 0, 0);
				}
			}
		}*/
		if (state != 0) {
			listview.setVisibility(View.GONE);
		}
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {
		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
        }
	}
	

}
