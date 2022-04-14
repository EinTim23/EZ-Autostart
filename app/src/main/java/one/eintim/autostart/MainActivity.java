package one.eintim.autostart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private void closeApp(){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        System.exit(1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        SharedPreferences prfs = getSharedPreferences("main", Context.MODE_PRIVATE);
        String name = prfs.getString("packagename", "");
        if(name.equals("")){
            final EditText taskEditText = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Package Name")
                    .setMessage("Enter the name of the package that you want to autostart.")
                    .setView(taskEditText)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = getSharedPreferences("main", Context.MODE_PRIVATE).edit();
                            editor.putString("packagename", taskEditText.getText().toString());
                            editor.commit();
                            closeApp();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            closeApp();
                        }
                    })
                    .create();
            dialog.show();
        }else{
            try{
                startActivity(getPackageManager().getLaunchIntentForPackage(name));
                System.exit(1);
            }finally {
                Toast.makeText(this, "Error launching app", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = getSharedPreferences("main", Context.MODE_PRIVATE).edit();
                editor.putString("packagename", "");
                editor.commit();
                closeApp();
            }
        }
    }
}