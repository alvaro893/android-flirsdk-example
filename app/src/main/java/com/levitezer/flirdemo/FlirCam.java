package com.levitezer.flirdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.flir.flironesdk.Device;
import com.flir.flironesdk.Frame;
import com.flir.flironesdk.FrameProcessor;
import com.flir.flironesdk.RenderedImage;

import java.util.EnumSet;

/**
 * Created by alvaro on 22.5.2017.
 */

public class FlirCam implements Device.Delegate, FrameProcessor.Delegate, Device.StreamDelegate {
    private static final String TAG = FlirCam.class.getSimpleName();
    private final Activity context;
    private final ImageView imageView;
    private Device device;
    private FrameProcessor frameProcessor;
    private Device.TuningState currentTuningState;

    public FlirCam(Activity context, ImageView imageView) {
        this. imageView = imageView;
        this.context = context;
        EnumSet<RenderedImage.ImageType> sett = EnumSet.of(RenderedImage.ImageType.ThermalLinearFlux14BitImage);
        this.frameProcessor = new FrameProcessor(context, this, sett);
    }

    @Override
    public void onTuningStateChanged(Device.TuningState tuningState) {
        currentTuningState = tuningState;
    }

    @Override
    public void onAutomaticTuningChanged(boolean b) {

    }

    @Override
    public void onDeviceConnected(Device device) {
        Log.d(TAG, "onDeviceConnected");
        this.device = device;
//        this.device.setPowerUpdateDelegate(this);
        this.device.startFrameStream(this);
    }

    @Override
    public void onDeviceDisconnected(Device device) {
        this.device = device;
    }

    @Override
    public void onFrameProcessed(RenderedImage renderedImage) {
        Log.d(TAG, "onFrameProcessed");
        final Bitmap bitmap = renderedImage.getBitmap();
        updateThermalImageView(bitmap);
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public void onFrameReceived(Frame frame) {
        Log.d(TAG, "onFrameReceived");
        if (currentTuningState != Device.TuningState.InProgress){
            frameProcessor.processFrame(frame);
        }
    }

    private void updateThermalImageView(final Bitmap frame){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(frame);
            }
        });
    }


}
