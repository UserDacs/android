package com.example.fixitnow.adapters;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Map<String, DataPart> byteData;
    private final Response.Listener<NetworkResponse> mListener;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.headers = new HashMap<>();
        this.params = new HashMap<>();
        this.byteData = new HashMap<>();
        this.mListener = listener;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    public void setParams(Map<String, String> params) {
        this.params.putAll(params);
    }

    public void setByteData(Map<String, DataPart> byteData) {
        this.byteData.putAll(byteData);
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


    protected Map<String, DataPart> getByteData() {
        return byteData;
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
