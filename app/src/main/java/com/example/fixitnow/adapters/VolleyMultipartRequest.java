package com.example.fixitnow.adapters;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;

    private final Response.Listener<NetworkResponse> mListener;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Map<String, DataPart> byteData;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.headers = new HashMap<>();
        this.params = new HashMap<>();
        this.byteData = new HashMap<>();
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

    public abstract Map<String, DataPart> getByteData();

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // Add text params
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dos, entry.getKey(), entry.getValue());
            }

            // Add file data
            for (Map.Entry<String, DataPart> entry : byteData.entrySet()) {
                buildFilePart(dos, entry.getKey(), entry.getValue());
            }

            // End boundary
            dos.writeBytes("--" + boundary + "--\r\n");
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }

    private void buildTextPart(DataOutputStream dos, String name, String value) throws IOException {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
        dos.writeBytes(value + "\r\n");
    }

    private void buildFilePart(DataOutputStream dos, String name, DataPart dataFile) throws IOException {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + dataFile.getFileName() + "\"\r\n");
        dos.writeBytes("Content-Type: " + dataFile.getType() + "\r\n\r\n");
        dos.write(dataFile.getContent());
        dos.writeBytes("\r\n");
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    // DataPart class to hold file info
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
