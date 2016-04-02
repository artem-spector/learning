package com.artem.learningclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.artem.client.ServerConnection;

public class LearningApp extends ActionBarActivity {

    private ServerConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_app);
        conn = ServerConnection.getConnection(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_learning_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendDrawing(View view) {
        conn.sendDrawing(getDrawingSurfaceView().getData());
    }

    public void clearDrawing(View view) {
        getDrawingSurfaceView().clear();
    }

    private DrawingSurfaceView getDrawingSurfaceView() {
        return (DrawingSurfaceView) findViewById(R.id.drawingView);
    }
}
