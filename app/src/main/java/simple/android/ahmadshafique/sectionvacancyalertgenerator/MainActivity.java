package simple.android.ahmadshafique.sectionvacancyalertgenerator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

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



        myWebView.addJavascriptInterface(new MyWebViewClient(), "HTMLOUT");

        /* WebViewClient must be set BEFORE calling loadUrl! */
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
        /* This call inject JavaScript into the page which just finished loading. */
                myWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
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

            Pattern p = Pattern.compile(html);
            Matcher m = p.matcher("</tr>\n" +
                    "        <tr>\n" +
                    "            <td>00542</td>\n" +
                    "            <td>Open</td>\n" +
                    "            <td>40</td>\n" +
                    "            <td>40</td>\n" +
                    "            <td>WEB TECHNOLOGIES [B]</td>");
            while(m.find()){
                System.out.println("FOUND THE MATCHING STRING");

                for(int i = m.start() ; i<m.end() ; i++){
                    System.out.print(i);
                }

                System.out.println("PRINTED THE MATCHING STRING");
            }
        }

    }
}