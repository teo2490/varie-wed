package teo.work;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public Bitmap mImageBitmap;
	public ImageView mImageView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button shareButton = (Button) findViewById(R.id.button1);
        Button callButton = (Button) findViewById(R.id.button2);
        Button camButton = (Button) findViewById(R.id.button3);
        Button mapsButton = (Button) findViewById(R.id.button4);
        mImageView = (ImageView) findViewById(R.id.imageView1);
        final EditText et = (EditText) findViewById(R.id.editText1);
        
        shareButton.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		//create the send intent  
        		Intent shareIntent =   
        		 new Intent(android.content.Intent.ACTION_SEND);  
        		  
        		//set the type  
        		shareIntent.setType("text/plain");  
        		  
        		//add a subject  
        		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,   
        		 "EVVIVA GLI SPOSI!");  
        		  
        		//build the body of the message to be shared  
        		String shareMessage = "Fai un regalo a Anna e Stefano con la lista 1!";  
        		  
        		//add the message  
        		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,   
        		 shareMessage);  
        		  
        		//start the chooser for sharing  
        		startActivity(Intent.createChooser(shareIntent,   
        		 "Dove vuoi condividere?"));  
        	}
        	});
        
        callButton.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		//Il numero sarà estratto dal DB e inserito con una variabile nella stringa
        		String myNumber= "tel:40916";
        		Intent myIntent = new Intent(Intent.ACTION_CALL, Uri.parse(myNumber));
        		startActivity(myIntent);
        		
        	}
        	});
        
        camButton.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		dispatchTakePictureIntent(2490);
        	}
        	});
        
        mapsButton.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		//Per ottenere la stringa si concatenano tutti i dati sull'indirizzo presenti nel DB con dei + in mezzo
        		//e si usa il metodo replaceAll("a","b") per togliere spazi e mettere +
        		String t = et.getText().toString();
        		t = t.replaceAll(" ", "+");
        		String uri = "geo:0,0?q="+t;
        		//controllo se Google Maps e' installato
        		if(isGoogleMapsInstalled())
        	    {
        			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        			startActivity(intent);
        	    }else{
        	    	Toast.makeText(getApplicationContext(),"Installa Google Maps, per favore!",Toast.LENGTH_LONG).show();
        	    }
        	}
        	});
    }
    
    //Camera
    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }
    
    //Camera
    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
    }
    
    //Controlla che Google Maps sia installato.
    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        } 
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
    	//Cosa fare quando torna dalla fotocamera (PERDE FOTO SE PASSO A LANDSCAPE!)
		super.onActivityResult(requestCode, resultCode, data);
		handleSmallCameraPhoto(data);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
