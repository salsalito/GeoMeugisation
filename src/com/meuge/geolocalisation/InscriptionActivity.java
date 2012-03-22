package com.meuge.geolocalisation;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class InscriptionActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription);
        Intent thisIntent = getIntent();
        String login = thisIntent.getExtras().getString("login");
        TextView monText = (TextView)findViewById(R.id.txt_username);
        monText.setText(login);
    }
	
	private void passageVue() {
		// Get the remote contact
		Intent intent = new Intent(InscriptionActivity.this,
    			LoginActivity.class);
		startActivity(intent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        	case R.id.iback: Toast.makeText(this, "Login", Toast.LENGTH_LONG).show();
        						passageVue();break;
        }
    	return true;
    }
    
    public boolean onPrepareOptionsMenu(Menu menu){
     
    	boolean result = super.onPrepareOptionsMenu(menu);
    	MenuItem tt = menu.findItem(R.id.iback);
    	tt.setVisible(true);
    	return result;
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
