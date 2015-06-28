package com.example.ubaid.jhelper;

/**
 * Created by ubaid on 27/06/15.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Path conventions :
// item[0]                       get array element / JSON Object
// item[0].object                get JSON Object in the element / object
// item[0].object.arr            get array nested in object
// item[0].object.size_width     get/set a property of JSON Object
// object1.object2.property      get nested objects and properties

/* ********************************************************************************************
* CLASS -- Helper class to get nester JSON objects by string
* *********************************************************************************************
*/
public class JHelper {
    private JSONObject rootJsonObj;
    private JSONArray rootJsonArray;
    private String rootName;    // name of root node
    private String varName;     // Variable name
    private boolean bIsArray = false;
    private final String LOG_TAG = "JHelper";

    public JSONArray getArray(String strPath) {
        return (JSONArray) getJson(strPath+"[]", false);
    }

    public JSONObject getObject(String strPath) {
        return (JSONObject) getJson(strPath, false);
    }

    public boolean getBoolean(String strPath) {
        return ((JSONObject) getJson(strPath, true)).optBoolean(varName);
    }

    public int getInteger(String strPath) {
        return ((JSONObject) getJson(strPath, true)).optInt(varName);
    }

    public String getString(String strPath) {
        return ((JSONObject) getJson(strPath, true)).optString(varName);
    }

    public double getDouble(String strPath) {
        return ((JSONObject) getJson(strPath, true)).optDouble(varName);
    }

    public JHelper setBoolean(String strPath, boolean value) {
        JSONObject jObj = null;
        JHelper jTemp = new JHelper();
         try {
             jObj = (JSONObject)getJson(strPath, true);
             jObj.put(varName, value);
        } catch (JSONException e) { Log.d(LOG_TAG, "JHelper::setBoolean() error  for: " + varName); }
          jTemp.setRootJson(jObj);
        return jTemp;
    }

    public JHelper setInteger(String strPath, int value) {
        JSONObject jObj = null;
        JHelper jTemp = new JHelper();
        try {
            jObj = (JSONObject)getJson(strPath, true);
            jObj.put(varName, value);
        } catch (JSONException e) { Log.d(LOG_TAG, "JHelper::setInteger() error  for: " + varName); }
        jTemp.setRootJson(jObj);
        return jTemp;
    }

    public JHelper setString(String strPath, String value) {
        JSONObject jObj = null;
        JHelper jTemp = new JHelper();
        try {
            jObj = (JSONObject)getJson(strPath, true);
            jObj.put(varName, value);
        } catch (JSONException e) { Log.d(LOG_TAG, "JHelper::setString() error  for: " + varName); }
        jTemp.setRootJson(jObj);
        return jTemp;
    }

    public JHelper setDouble(String strPath, double value) {
        JSONObject jObj = null;
        JHelper jTemp = new JHelper();
        try {
            jObj = (JSONObject)getJson(strPath, true);
            jObj.put(varName, value);
        } catch (JSONException e) { Log.d(LOG_TAG, "JHelper::setDouble() error  for: " + varName); }
        jTemp.setRootJson(jObj);
        return jTemp;
    }

    public Object getRoot(){
        return bIsArray ? rootJsonObj : rootJsonArray;
    }

    public boolean isRootArray(){
        return bIsArray;
    }

    /**
     * ***********************************************************************************************
     * METHOD : returns JHelper object for the path, note to get array use '[]' at the array name end
     * ************************************************************************************************
     */

    public JHelper get(String strPath){
        JHelper jhTemp = new JHelper();
        Object obj = getJson(strPath, false);
        if(obj instanceof  JSONObject){
            jhTemp.setRootJson((JSONObject)obj);
        } else {
            jhTemp.setRootJson((JSONArray)obj);
        }
        return jhTemp;
    }
    /**
     * ***********************************************************************************************
     * METHOD : returns JSON object/array or Variable based on the string provided
     * ************************************************************************************************
     */
    private Object getJson(String arg, boolean bVariable) {
        List<Token> tokens = new ArrayList<Token>();
        String token[] = arg.split("\\.");
        int tokenLength = 0;
        // Create tokens from string
        for (int i = 0; i < token.length; i++) {
            int start = token[i].indexOf("[");
            int end = token[i].indexOf("]");
            int type = end - start;

            switch (type) {
                case 0:  // JSON object
                    tokens.add(new Token(token[i], 0, 0));
                    break;
                case 1: // JSON array object
                    token[i] = token[i].replace("[]", "");
                    tokens.add(new Token(token[i], 1, 0));
                    break;
                default: // JSON array index
                    String index = token[i].substring(start + 1, end);
                    String temp = token[i].substring(0, start);
                    tokens.add(new Token(temp, 2, Integer.parseInt(index)));
                    break;
            }
        }

        if (bVariable) { // if String is for JSON variable, don't consider last token
            tokenLength = token.length - 1;
            varName = tokens.get(tokenLength).name;
        } else {
            tokenLength = token.length;
        }

        Object jsonResult = bIsArray ? rootJsonArray : rootJsonObj;

        try {
            for (int i = 0; i < tokenLength; i++) {
                if (i == 0) {
                    switch (tokens.get(i).type) {
                        case 0:
                            if (bIsArray) {
                                jsonResult = ((JSONArray) (jsonResult));
                            } else {
                                jsonResult = ((JSONObject) (jsonResult)).getJSONObject(tokens.get(i).name);
                            }
                        case 1:
                            if (bIsArray) {
                                jsonResult = ((JSONArray) (jsonResult));
                            } else {
                                jsonResult = ((JSONObject) (jsonResult)).getJSONArray(tokens.get(i).name);
                            }
                        default:
                            if (bIsArray) {
                                jsonResult = ((JSONArray) (jsonResult)).getJSONObject(tokens.get(i).index);
                            } else {
                                jsonResult = ((JSONObject) (jsonResult)).getJSONArray(tokens.get(i).name).get(tokens.get(i).index);
                            }
                    }
                } else {
                    switch (tokens.get(i).type) {
                        case 0:
                            jsonResult = ((JSONObject) (jsonResult)).getJSONObject(tokens.get(i).name);
                        case 1:
                            jsonResult = ((JSONObject) (jsonResult)).getJSONArray(tokens.get(i).name);
                        default:
                            jsonResult = ((JSONObject) (jsonResult)).getJSONArray(tokens.get(i).name).get(tokens.get(i).index);
                    }
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "JHelper::getJson() error getting JSON " );
            return null;
        }
        return jsonResult;
    }

    /**
     * ***********************************************************************************************
     * METHOD : to return array index string
     * ************************************************************************************************
     */
    public static String I(int index) {
        return "[" + Integer.toString(index) + "]";
    }

    /**
     * ***********************************************************************************************
     * METHOD : set JSON root object and root name
     * ************************************************************************************************
     */
    public void setRootJson(JSONObject json) {
        rootJsonObj = json;
        bIsArray = false;
        Iterator<?> keys = rootJsonObj.keys();
        if (keys.hasNext()) {
            String rootNode = (String) keys.next();
            rootName = rootNode;
            Log.d(LOG_TAG, "RootNode: " + rootNode);
        }
    }

    public void setRootJson(JSONArray json) {
        rootJsonArray = json;
        bIsArray = true;
        Iterator<?> keys = rootJsonObj.keys();
        if (keys.hasNext()) {
            String rootNode = (String) keys.next();
            rootName = rootNode;
            Log.d(LOG_TAG, "RootNode: " + rootNode);
        }
    }

    public String getRootName() {
        return rootName;
    }

    /**
     * ***********************************************************************************************
     * CLASS : to store JSON tokens that will be used to find json object
     * ************************************************************************************************
     */
    public class Token {
        public String name;  // Name of the Json object or Variable
        public int type;    // Type Json object or array
        public int index;   // index for array

        Token(String tag, int type, int index) {
            this.name = tag;
            this.type = type;
            this.index = index;
        }
    }

    /**
     * **********************************************************************************
     * METHOD -- Reads a file in assests folder and returns it as string used for JSON loading
     * ***********************************************************************************
     */
    public boolean loadJSONFile(Context context, String fileName) {

        boolean bResult = true;
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            Log.d(LOG_TAG, "JHelper::loadJSONFile() opening file: " + fileName);
            bResult = false;
        }

        try {
            JSONObject jObj = new JSONObject(text.toString());
            setRootJson(jObj);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "JHelper::loadJSONFile() Error creating JSONObject");
            try {
                JSONArray jArray = new JSONArray(text.toString());
                setRootJson(jArray);
            } catch (JSONException e1) {
                Log.d(LOG_TAG, "JHelper::loadJSONFile() Error creating JSONArray");
                bResult = false;
            }
        }

        return bResult;
    }

}