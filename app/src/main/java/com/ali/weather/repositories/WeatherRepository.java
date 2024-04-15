package com.ali.weather.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ali.weather.database.RoomDatabase;
import com.ali.weather.model.ForecastItem;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.utilities.Constants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherRepository {

    private RoomDatabase roomDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();

    public WeatherRepository(Context context) {
        roomDatabase = RoomDatabase.getInstance(context);
    }

    public LiveData<WeatherItem> getWeatherInfo(String id){
        return roomDatabase.weatherDao().getWeatherInfo(id);
    }

    public LiveData<List<ForecastItem>> getForecastInfo(String id){
        return roomDatabase.forecastDao().getForecastItemInfo(id);
    }

    public LiveData<List<WeatherItem>> getAllWeatherInfo(){
        return roomDatabase.weatherDao().getAllWeatherInfo();
    }

    public ForecastItem getItemInfo(String id){
        return roomDatabase.forecastDao().getItemInfo(id);
    }

    public void updateDatabase(){
        for (String id: Constants.IDS) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    parseWeather("current",id);
                }
            });
        }

        for (String id: Constants.IDS) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    parseWeather("forecast",id);
                }
            });
        }
    }

    public void parseWeather(String type, String id) {
        try {
            URL url = new URL(type.equalsIgnoreCase("current") ? Constants.CURRENT_DAY+id : Constants.FORECAST+id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            Element root = document.getDocumentElement();
            NodeList items = root.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                String title = item.getElementsByTagName("title").item(0).getTextContent();
                String description = item.getElementsByTagName("description").item(0).getTextContent();
                String points = item.getElementsByTagName("georss:point").item(0).getTextContent();
                HashMap<String, String> titleParseData = parseString(title);
                HashMap<String, String> descriptionParseData = parseString(description);
                if (type.equals("current")){
                    WeatherItem weatherItem = new WeatherItem();
                    weatherItem.id = id;
                    for (HashMap.Entry<String, String> entry : titleParseData.entrySet()) {
                        if (entry.getKey().contains("00")){
                            weatherItem.weather = entry.getValue();
                        }
                    }

                    weatherItem.temperature = descriptionParseData.get("Temperature");
                    weatherItem.wind = descriptionParseData.get("Wind Speed");
                    weatherItem.humidity = descriptionParseData.get("Humidity");
                    weatherItem.pressure = descriptionParseData.get("Pressure");
                    weatherItem.pressure_type = description.contains("Steady") ? "Steady" : description.contains("Rising") ? "Rising" : "Falling";
                    weatherItem.windDirection = descriptionParseData.get("Wind Direction");
                    roomDatabase.weatherDao().insertInfo(weatherItem);
                }else{
                    ForecastItem weatherItem = new ForecastItem();
                    weatherItem.id = id+i;
                    weatherItem.cityId = id;
                    weatherItem.weather = titleParseData.get("00 BST");
                    weatherItem.maximumTemperature = descriptionParseData.get("Maximum Temperature");
                    weatherItem.minimumTemperature = descriptionParseData.get("Minimum Temperature");
                    weatherItem.sunSet = descriptionParseData.get("Sunset");
                    weatherItem.uv = descriptionParseData.get("UV Risk");
                    weatherItem.pollution = descriptionParseData.get("Pollution");
                    weatherItem.sunRise = descriptionParseData.get("Sunrise");
                    weatherItem.visibility = descriptionParseData.get("Visibility");
                    weatherItem.wind = descriptionParseData.get("Wind Speed");
                    weatherItem.humidity = descriptionParseData.get("Humidity");
                    weatherItem.pressure = descriptionParseData.get("Pressure");
                    weatherItem.windDirection = descriptionParseData.get("Wind Direction");
                    String[] location = points.split(" ");
                    weatherItem.latitude = location[0];
                    weatherItem.longitude = location[1];

                    for (HashMap.Entry<String, String> entry : titleParseData.entrySet()) {
                        switch (entry.getKey()){
                            case "Monday":
                            case "Tuesday":
                            case "Wednesday":
                            case "Thursday":
                            case "Friday":
                            case "Saturday":
                            case "Sunday":
                            case "Today":
                            case "Tonight":
                                weatherItem.day = entry.getKey();
                                weatherItem.weather = entry.getValue();
                                break;
                        }
                    }
                    roomDatabase.forecastDao().insertInfo(weatherItem);
                }
            }

            inputStream.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> parseString(String input){
        Pattern pattern = Pattern.compile("(\\w+(?: \\w+)*): ([^,]+)");
        HashMap<String, String> keyValueMap = new HashMap<>();
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            keyValueMap.put(key, value);
        }
        return keyValueMap;
    }
}
