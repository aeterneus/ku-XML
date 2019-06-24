package com.kuldiegor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.json.JSONObject;
import org.json.XML;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Map;

public class KuXML {
    private static KuXML sKuXML;
    private static Gson sGson;

    private KuXML(){
        sGson = new Gson();
    }

    public static KuXML getInstance(){
        if (sKuXML==null){
            sKuXML = new KuXML();
        }
        return sKuXML;
    }

    public <T> T fromXML(Reader xml, boolean keepStrings,Class<T> classOfT){
        return fromXML(xml,keepStrings,(Type)classOfT);
    }

    public <T> T fromXML(Reader xml,Class<T> classOfT){
        return fromXML(xml,false,(Type)classOfT);
    }

    public <T> T fromXML(String xml, boolean keepStrings,Class<T> classOfT){
        return fromXML(new StringReader(xml),keepStrings,(Type)classOfT);
    }

    public <T> T fromXML(String xml,Class<T> classOfT){
        return fromXML(xml,false,classOfT);
    }

    public <T> T fromXML(Reader xml, boolean keepStrings, Type classOfT){
        org.json.JSONObject jsonObject = org.json.XML.toJSONObject(xml,keepStrings);
        com.google.gson.JsonObject gsonObject = getGsonObject(jsonObject);
        return sGson.fromJson(gsonObject,classOfT);
    }

    public String toXML(Object src){
        com.google.gson.JsonElement gsonElement = sGson.toJsonTree(src);
        if (gsonElement.isJsonObject()) {
            return getXMLFromGsonObject(null,gsonElement.getAsJsonObject());
        } else if (gsonElement.isJsonArray()){
            return getXMLFromGsonArray(null,gsonElement.getAsJsonArray());
        } else {
            return null;
        }
    }

    public com.google.gson.JsonObject getGsonObject(org.json.JSONObject jsonObject){
        com.google.gson.JsonObject gsonObject = new com.google.gson.JsonObject();
        for (String key:jsonObject.keySet()){
            Object o = jsonObject.get(key);
            addToGsonObject(gsonObject,key,o);
        }
        return gsonObject;
    }

    private String getXMLFromGsonObject(String name,com.google.gson.JsonObject gsonObject){
        StringBuilder tagBuilder = new StringBuilder();
        StringBuilder attributeBuilder = new StringBuilder();
        for (Map.Entry<String ,JsonElement> gsonEntry:gsonObject.entrySet()){
            JsonElement gsonElement = gsonEntry.getValue();
            if (gsonElement.isJsonPrimitive()){
                if (gsonEntry.getKey().equals("content")){
                    tagBuilder.append(gsonElement.getAsString());
                } else {
                    attributeBuilder.append(" ")
                            .append(gsonEntry.getKey())
                            .append("=")
                            .append("\"").append(gsonElement.getAsString()).append("\"");
                }
            } else if (gsonElement.isJsonNull()){
                attributeBuilder.append(" ")
                        .append(gsonEntry.getKey())
                        .append("=")
                        .append("\"null\"");
            } else if (gsonElement.isJsonObject()){
                tagBuilder.append(getXMLFromGsonObject(gsonEntry.getKey(),gsonElement.getAsJsonObject()));
            } else if (gsonElement.isJsonArray()){
                tagBuilder.append(getXMLFromGsonArray(gsonEntry.getKey(),gsonElement.getAsJsonArray()));
            }
        }
        StringBuilder finalBuilder = new StringBuilder();
        if (name!=null) {
            finalBuilder.append("<").append(name).append(attributeBuilder).append(">")
                    .append(tagBuilder)
                    .append("</").append(name).append(">");
        } else {
            finalBuilder.append(tagBuilder);
        }
        return finalBuilder.toString();
    }

    private String getXMLFromGsonArray(String name,com.google.gson.JsonArray gsonArray){
        StringBuilder tagBuilder = new StringBuilder();
        for (JsonElement gsonElement:gsonArray){
            if (gsonElement.isJsonPrimitive()){
                tagBuilder.append("<").append(name).append(">")
                        .append(gsonElement.getAsString())
                        .append("</").append(name).append(">");
            } else if (gsonElement.isJsonNull()){
                tagBuilder.append("<").append(name).append(">")
                        .append("null")
                        .append("</").append(name).append(">");
            } else if (gsonElement.isJsonObject()){
                tagBuilder.append(getXMLFromGsonObject(name,gsonElement.getAsJsonObject()));
            } else if (gsonElement.isJsonArray()){
                tagBuilder.append(getXMLFromGsonArray(name,gsonElement.getAsJsonArray()));
            }
        }
        return tagBuilder.toString();
    }

    private void addToGsonObject(com.google.gson.JsonObject gsonObject,String key,Object o){
        if (o!=null){
            if (o instanceof org.json.JSONObject){
                gsonObject.add(key,getGsonObject((JSONObject) o));
            } else if (o instanceof org.json.JSONArray) {
                org.json.JSONArray jsonArray = (org.json.JSONArray) o;

                com.google.gson.JsonArray gsonArray = new com.google.gson.JsonArray();
                for (Object oA:jsonArray){
                    addToGsonArray(gsonArray,oA);
                }
                gsonObject.add(key, gsonArray);
            } else if (o instanceof String) {
                gsonObject.addProperty(key, (String) o);
            } else if (o instanceof Number) {
                gsonObject.addProperty(key, (Number) o);
            } else if (o instanceof Boolean) {
                gsonObject.addProperty(key, (Boolean) o);
            } else if (o instanceof Character) {
                gsonObject.addProperty(key, (Character) o);
            }

        } else {
            gsonObject.add(key,null);
        }
    }

    private void addToGsonArray(com.google.gson.JsonArray gsonArray,Object o){
        if (o!=null){
            if (o instanceof org.json.JSONObject){
                gsonArray.add(getGsonObject((JSONObject) o));
            } else if (o instanceof org.json.JSONArray) {
                org.json.JSONArray jsonArray = (org.json.JSONArray) o;

                com.google.gson.JsonArray gsonArray2 = new com.google.gson.JsonArray();
                for (Object oA:jsonArray){
                    addToGsonArray(gsonArray2,oA);
                }
            } else if (o instanceof String) {
                gsonArray.add((String) o);
            } else if (o instanceof Number) {
                gsonArray.add( (Number) o);
            } else if (o instanceof Boolean) {
                gsonArray.add((Boolean) o);
            } else if (o instanceof Character) {
                gsonArray.add((Character) o);
            }

        }
    }
}
