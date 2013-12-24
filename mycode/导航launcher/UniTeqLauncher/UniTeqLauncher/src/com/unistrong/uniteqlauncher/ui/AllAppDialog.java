package com.unistrong.uniteqlauncher.ui;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.unistrong.uniteqlauncher.MainActivity;
import com.unistrong.uniteqlauncher.R;
import com.unistrong.uniteqlauncher.adapter.GridViewAdapter;
import com.unistrong.uniteqlauncher.db.DBService;
import com.unistrong.uniteqlauncher.util.AppInfo;
import com.unistrong.uniteqlauncher.util.Constants;

/**
 * 加载所有APP的dialog
 * @author Baron.hu
 * */
public class AllAppDialog extends AlertDialog{
	
	public static final String LOG_TAG = "AllAppDialog";
	
	private Context mContext;
	private GridView gridMenu;
	private View menuView;
	private List<ResolveInfo> apps;
	private AlertDialog alertDialog;
	private ViewGroup settings;
	private DBService mDBService;
	
	public static boolean hasAdd = false;
	
	public AllAppDialog(Context context, List<ResolveInfo> apps, ViewGroup settings) {
		super(context);
		this.apps = apps;
		this.settings = settings;
		this.mContext = context;
		mDBService = new DBService(mContext);
		displayDialog();
	}
	
	/**
	 * long press and show all applications in a dialog
	 * 
	 * @param
	 * @return void
	 * */
	public void displayDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.prompt).setMessage(R.string.prompt_text);
		
		menuView = View.inflate(mContext, R.layout.menu_gridview, null);
		gridMenu = (GridView) menuView.findViewById(R.id.menuview);
		gridMenu.setAdapter(new GridViewAdapter(mContext, apps));
		gridMenu.setOnItemClickListener(listener);

		alertDialog = builder.create();
		alertDialog.setView(menuView);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
		Log.d(LOG_TAG, "displayDialog()......");
	}
	
	public OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// we just need one icon,hereby we remove
			// the previous sub view
			settings.removeAllViews(); 
			hasAdd = true; // user has choose an application
			ResolveInfo mResolveInfo = apps.get(position);

			Drawable appIcon = mResolveInfo.activityInfo
					.loadIcon(mContext.getPackageManager());
			String packageName = mResolveInfo.activityInfo.packageName;
			String className = mResolveInfo.activityInfo.name;
			String appName = mResolveInfo.activityInfo.loadLabel(
					mContext.getPackageManager()).toString();
			
			/* 把值赋给暂存变量,以便下次开启应用时不必重新从数据库加载 */
			AppInfo.pkgName = packageName;
			AppInfo.clsName = className;
			AppInfo.appIcon = appIcon;
			AppInfo.appName = appName;
			Log.d(LOG_TAG, new AppInfo().toString());

			/* 重新布局子view */
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.app_view, null);
			ImageView iv = (ImageView) v.findViewById(R.id.appicon);
			TextView tv = (TextView) v.findViewById(R.id.appname);
			iv.setImageDrawable(appIcon);
			tv.setText(appName);
			settings.addView(v);

			SQLiteDatabase db = mDBService.getWritableDatabase(); // get DB
			try {
				db.execSQL(Constants.DEL_SQL); // clear all data before inserting a new one
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				((BitmapDrawable) iv.getDrawable()).getBitmap().compress(
						CompressFormat.PNG, 100, baos);
				Object[] args = new Object[] { packageName, className, appName,
						baos.toByteArray() };
				db.execSQL(Constants.INSERT_SQL, args);
				baos.close();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			alertDialog.dismiss();
		}
	};

}
