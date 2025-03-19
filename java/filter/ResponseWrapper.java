package filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream capture;  //用於捕獲響應的輸出流 //錄影帶
    private ServletOutputStream output;  
    private PrintWriter writer;   

    public ResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        capture = new ByteArrayOutputStream();
        output = new ServletOutputStream() {
        	
            @Override
            public void write(int b) throws IOException {
                capture.write(b); //將數據寫入ByteArrayOutputStream
            }

            @Override
            public boolean isReady() {
                return true; //總是準備好輸入數據
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                // 无需实现非阻塞I/O的写监听
            }
        };
        writer = new PrintWriter(capture);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return output;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public String getCaptureAsString() throws IOException {
        writer.flush(); // 确保所有数据都写入到capture中
        return capture.toString("UTF-8");
    }

    public byte[] getCaptureAsBytes() {
        writer.flush(); // 确保所有数据都写入到capture中
        return capture.toByteArray();
    }
}
