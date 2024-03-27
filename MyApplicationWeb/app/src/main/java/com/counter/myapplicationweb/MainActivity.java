package com.counter.myapplicationweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String currentUrl = "https://rofls.akrapov1c.ru/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.WebView);
        webView.getSettings().setJavaScriptEnabled(true);
        loadWebViewUrl(currentUrl);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                currentUrl = url;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                String htmlData = "<html>" +
                        "<head>" +
                        "<style>" +
                        "  body { background-color: #f2f2f2; text-align: center; margin-top: 50px; color: #333; font-family: 'Arial';}" +
                        "  h1 { color: #555; }" +
                        "  p { margin: 20px 0; font-size: 18px; }" +
                        "  .button { background-color: #4CAF50; color: white; padding: 14px 20px; margin: 8px 0; border: none; cursor: pointer; width: 200px;}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<h1>Упс! Сервис не найден.</h1>" +
                        "<p>Кажется, мы не можем достучаться до сервиса. Попробуйте проверить соединение с интернетом, перезапустить приложение или проверить настройки сети.</p>" +
                        "<button class='button' onclick='restartApp()'>Перезапустить приложение</button>" + // Кнопка перезапуска
                        "<button class='button' onclick='openNetworkSettings()'>Проверить настройки сети</button>" + // Существующая кнопка
                        "<script type='text/javascript'>" +
                        "  function openNetworkSettings() {" +
                        "    Android.openNetworkSettings();" +
                        "  }" +
                        "  function restartApp() {" +
                        "    Android.restartApp();" +
                        "  }" +
                        "</script>" +
                        "</body>" +
                        "</html>";

                view.loadDataWithBaseURL(null, htmlData, "text/HTML", "UTF-8", null);
                view.getSettings().setJavaScriptEnabled(true);
                view.addJavascriptInterface(new Object() {
                    @JavascriptInterface
                    public void openNetworkSettings() {
                        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }

                    @JavascriptInterface
                    public void restartApp() {
                        // Реализация перезапуска приложения
                        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                }, "Android");
            }
        });
    }

    private void loadWebViewUrl(String url) {
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            webView.clearCache(true);
            loadWebViewUrl(currentUrl);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
