package simple.android.ahmadshafique.sectionvacancyalertgenerator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {


    Button startButton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebView myWebView = (WebView) findViewById(R.id.myWebView);

        startButton = (Button) findViewById(R.id.startButton);
        //WebView myWebView = new WebView(this);
        //myWebView.loadUrl("http://portal.aiub.edu");
        //myWebView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        // Get a handler that can be used to post to the main thread
        final Handler mainHandler = new Handler(this.getMainLooper());



        myWebView.addJavascriptInterface(new MyWebViewClient(), "HTMLOUT");




        /* WebViewClient must be set BEFORE calling loadUrl! */
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                                /* This call inject JavaScript into the page which just finished loading. */
                        myWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<table>'+document.getElementsByTagName('html')[0].innerHTML+'</table>');");

                    } // This is your code
                };
                mainHandler.post(myRunnable);
            }
        });


        /* load a web page */
        myWebView.loadUrl("http://portal.aiub.edu");

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println(myWebView.getUrl());
            }
        });

    }

    // Use When the user clicks a link from a web page in your WebView
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("portal.aiub.edu")) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }


        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {


            // process the html as needed by the app

            //String lowerCase = html.toLowerCase();

            //String input = html.toString();
            //writeToFile(input,this);

            //Pattern p = Pattern.compile(lowerCase);
            Pattern p = Pattern.compile(html);

            /*
            String matchNeeded = "WEB" ;
            String matchNeededLowerCase = matchNeeded.toLowerCase();
            */

            Matcher m = p.matcher("<tr>\n" +
                    "            <td>00542</td>\n" +
                    "            <td>Open</td>\n" +
                    "            <td>40</td>\n" +
                    "            <td>40</td>\n" +
                    "            <td>WEB TECHNOLOGIES [B]</td>");

                    /*
            Matcher m = p.matcher("<tr>\n" +
                    "             <td>00542</td>\n" +
                    "             <td>Open</td>\n" +
                    "             <td>40</td>\n" +
                    "             <td>40</td>\n" +
                    "             <td>WEB TECHNOLOGIES [B]</td>");
                    */

            while(m.find()){
                System.out.println("FOUND THE MATCHING STRING");

                for(int i = m.start() ; i<m.end() ; i++){
                    System.out.print(i);
                }

                System.out.println("PRINTED THE MATCHING STRING");
            }
        }

    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("myfile.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("myfile.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}