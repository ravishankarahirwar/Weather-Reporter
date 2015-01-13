package weatherreporter.com.weatherreporter;


import weatherreporter.dataclasses.Data;
import weatherreporter.dataclasses.MyLog;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class MyTabListener implements ActionBar.TabListener {

	private Fragment mFragment;
	private FirstActivity mActivity;
	private final String mTag;
	private final Class mClass;
	Data data;
	Tab lastTab;
	FragmentTransaction lastFt;

	
	boolean isSelect =false;
	String greg = "MyTabListener";


	public MyTabListener(FirstActivity activity, Data data, String tag, Class<?> clz) {
		MyLog.d("gerg", "constructor");
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		this.data=data;

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

		lastTab=tab;
		lastFt=ft;
		if (mFragment == null) {	
		MyLog.d("gerg", "onTabSelected "+mTag+ "\ntab:"+ tab+"\nft:"+ft);

			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			ft.add(android.R.id.content, mFragment, mTag);

		} else {
			ft.attach(mFragment);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (mFragment != null) {
		MyLog.d("gerg", "onTabUnselected "+mTag+ "\ntab:"+ tab+"\nft:"+ft);
			ft.detach(mFragment);
		}
	}


	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		MyLog.d("gerg", "onTabReselected "+mTag+ "\ntab:"+ tab+"\nft:"+ft);
		onTabUnselected(tab,ft);
		onTabSelected(tab, ft);
	}
	
	

}
