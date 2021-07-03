package ru.maximivanov.yourvocabular.network;

import java.io.*;
import java.util.Objects;
import android.os.Handler;
import android.os.Message;
import okhttp3.*;
import com.google.gson.*;
import ru.maximivanov.yourvocabular.R;
import ru.maximivanov.yourvocabular.application.App;
import ru.maximivanov.yourvocabular.models.Element;
import ru.maximivanov.yourvocabular.models.User;

public class Translate implements Runnable {
    private final static String subscriptionKey = App.getStrRes(R.string.subscription_key);
    private final Handler handler;
    private final String inputText;
    private final static String location = App.getStrRes(R.string.location);
    private final HttpUrl url;
    private final OkHttpClient client;

    public Translate(String input, Handler handler) {
        this.handler = handler;
        this.inputText = input;
        url = new HttpUrl.Builder()
                .scheme(App.getStrRes(R.string.scheme))
                .host(App.getStrRes(R.string.host))
                .addPathSegment(App.getStrRes(R.string.added_path_segment))
                .addQueryParameter(App.getStrRes(R.string.api_version_name),
                        App.getStrRes(R.string.api_version))
                .addQueryParameter(App.getStrRes(R.string.from), User.getFromLang())
                .addQueryParameter(App.getStrRes(R.string.to), User.getToLang())
                .build();
        client = new OkHttpClient();
    }

    // This function performs a POST request.
    public String Post() throws IOException {
        MediaType mediaType = MediaType.parse(App.getStrRes(R.string.media_type));
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \"" + inputText + "\"}]");
        Request request = new Request.Builder().url(url).post(body)
                .addHeader(App.getStrRes(R.string.ocp_apim_key), subscriptionKey)
                .addHeader(App.getStrRes(R.string.ocp_apim_region), location)
                .addHeader(App.getStrRes(R.string.content_type), App.getStrRes(R.string.media_type))
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public static String getOutputFromJson(String jsonText) {
//        JsonElement json = JsonParser.parseString(json_text);
        jsonText = jsonText.substring(1, jsonText.length() - 1);
        Gson gson = new GsonBuilder().create();
        OutputTranslations translation = gson.fromJson(jsonText, OutputTranslations.class);
        return translation.translations[0].text;
    }

    @Override
    public void run() {
        try {
            String response = Post();
            response = getOutputFromJson(response);
            Message msg = new Message();
            msg.obj = new Element(inputText, response);
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class OutputTranslations {
    OneTranslation[] translations;
}

class OneTranslation {
    String text;
    String to;
}