package com.lichao.dictionary;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DictionaryActivity extends Activity {

	private EditText     etInput           = null;
	private Button       btnSearch         = null;
	private TextView     tvResult          = null;
	private final String DATABASE_FILENAME = "dictionary.txt";
	private final String DATABASE_PATH     = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/download";

	SQLiteDatabase database;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		etInput   = (EditText) findViewById(R.id.etInput);
		btnSearch = (Button)   findViewById(R.id.btnSearch);
		tvResult  = (TextView) findViewById(R.id.tvResult);
		
		try {
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
		} catch (Exception e) {
			tvResult.setText("Failed to open database!" + "\n\n" + e.toString());
		}
		
		btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(etInput.getText().toString().trim().length() == 0) {
					tvResult.setText("Please input a word to continue...");
					return;
				}
				
				String sql = "select explain from eng where word=?";
				
				try {
					Cursor cursor = database.rawQuery(sql, new String[]{ etInput.getText().toString().trim() });
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						byte bytes[] = cursor.getBlob(0);
						String sn = new String(bytes,"utf-8");
						tvResult.setText(etInput.getText().toString().trim() + "\n\n" + sn.trim());
						cursor.close();
					} else {
						tvResult.setText("This word is not found!");
					}
				} catch(Exception e) {
					tvResult.setText("Failed to Search this word!" + "\n\n" + e.toString());
				} finally {
					
				}
			}
		});
	}
}