package com.codeseasy.loginui.media;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DecodeTcp extends Thread{
    private InputStream is = null;
    private FileInputStream fs = null;

    private SurfaceView mSurfaceView;
    private Context mContext;

    private Button mReadButton;
    private MediaCodec mCodec;

    Thread readFileThread;
    boolean isInit = false;

    // Video Constants
    private final static String MIME_TYPE = "video/avc"; // H.264 Advanced Video
    private final static int VIDEO_WIDTH = 1080;
    private final static int VIDEO_HEIGHT = 720;
    private final static int TIME_INTERNAL = 30;
    private final static int HEAD_OFFSET = 512;


    InputStream mInputStream;

    public DecodeTcp(Context context, SurfaceView sfw, InputStream inputStream) {
        mSurfaceView = sfw;
        mContext = context;
        //输入流
        mInputStream = inputStream;
        initDecoder();

    }




    //初始化
    public void initDecoder() {
        try {
            mCodec = MediaCodec.createDecoderByType(MIME_TYPE);
            MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE,
                    VIDEO_WIDTH, VIDEO_HEIGHT);
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar);
            //mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
            mCodec.configure(mediaFormat, mSurfaceView.getHolder().getSurface(),
                    null, 0);
            mCodec.start();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    long mCount = 0;

    public boolean onFrame(byte[] buf, int offset, int length) {
        Log.d("DecodeDebug", "onFrame start");
        Log.d("DecodeDebug","onFrame Thread:" + Thread.currentThread().getId());
        // Get input buffer index
        ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
        int inputBufferIndex = mCodec.dequeueInputBuffer(100);

        Log.d("DecodeDebug","onFrame index:" + inputBufferIndex);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(buf, offset, length);
            mCodec.queueInputBuffer(inputBufferIndex, 0, length, mCount
                    * TIME_INTERNAL, 0);
            mCount++;
        } else {
            return false;
        }

        // Get output buffer index
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, 100);
        while (outputBufferIndex >= 0) {
            mCodec.releaseOutputBuffer(outputBufferIndex, true);
            outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, 0);
        }
        Log.e("Media", "onFrame end");
        return true;
    }

    /**
     * Find H264 frame head
     *
     * @param buffer
     * @param len
     * @return the offset of frame head, return -1 if can not find one
     */
    static int findHead(byte[] buffer, int from,int len) {
        int i;

        //
        for (i = from; i < len; i++) {
            if (checkHead(buffer, i))
                return i;
        }
        return -1;
    }

    /**
     * Check if is H264 frame head
     *
     * @param buffer
     * @param offset
     * @return whether the src buffer is frame head
     */
    static boolean checkHead(byte[] buffer, int offset) {
        // 00 00 00 01
        if (buffer[offset] == 0 && buffer[offset + 1] == 0
                && buffer[offset + 2] == 0 && buffer[3] == 1)
            return true;
        // 00 00 01
        if (buffer[offset] == 0 && buffer[offset + 1] == 0
                && buffer[offset + 2] == 1)
            return true;
        return false;
    }

    @Override
    public void run() {
        long h264Read = 0;
        int frameOffset = 0;
        byte[] buffer = new byte[100000];
        byte[] framebuffer = new byte[200000];
        while (true) {
            try {

                //方法返回从输入流中读取不受阻塞，输入流方法的下一次调用的剩余字节数


                // 从流中读到Buffer
                int count = mInputStream.read(buffer);
                Log.d("DecodeDebug", "count: " + count);
                //累计一下读到的总字节长度
                h264Read += count;
                Log.d("DecodeDebug", "count:" + count + " h264Read:" + h264Read);

                // Fill frameBuffer 将buffer截断并填入framebuffer，使得buffer中保存了完整的数据帧
                if (frameOffset + count < 200000) {
                    System.arraycopy(buffer, 0, framebuffer, frameOffset, count);
                    frameOffset += count;
                } else {
                    //当前的framebuffer放不下，将之前的framebuffer清除
                    frameOffset = 0;
                    System.arraycopy(buffer, 0, framebuffer,
                            frameOffset, count);
                    frameOffset += count;
                }

                while(frameOffset > 0) {
                    // Find H264 head 寻找第一个帧头 offset是帧头的位置
                    int frameBegin = findHead(framebuffer, 0, frameOffset);
                    Log.d("DecodeDebug", " Head:" + frameBegin);
                    // 找到帧头之后，将帧头移动到framebuffer的 0 位置
                    if (frameBegin == -1) {
                        //整个缓冲区内都没有找到帧头，直接将缓存中的内容抛弃
                        frameOffset = 0;
                        break;
                    } else if (frameBegin > 0) {
                        //将缓冲区整体向前移动，使得帧头在缓冲区的开始位置
                        byte[] temp = framebuffer;
                        framebuffer = new byte[200000];
                        System.arraycopy(temp, frameBegin, framebuffer, 0, frameOffset - frameBegin);
                        frameOffset -= frameBegin;
                    }
                    // 如果帧头在 0位置，不需要处理

                    //开始找帧尾的位置  一帧一般不会低于 300-400字节 从300字节开始搜索
                    int frameEnd = findHead(framebuffer, 300, frameOffset);

                    if (frameEnd == -1) {
                        //如果没有帧尾，说明这一帧还没有完全被接收到，继续接收数据
                        break;
                    }
                    onFrame(framebuffer, 0, frameEnd);
                    //不管能不能正确解析，直接将这一帧丢掉
                    byte[] temp = framebuffer;
                    framebuffer = new byte[200000];
                    System.arraycopy(temp, frameEnd, framebuffer, 0, frameOffset - frameEnd);
                    frameOffset -= frameEnd;
                }
                Log.d("loop", "end loop");

            } catch (Exception e) {
                Log.e("DECODETCPERROR",e.toString());
                e.printStackTrace();
            }
        }
    }

}
