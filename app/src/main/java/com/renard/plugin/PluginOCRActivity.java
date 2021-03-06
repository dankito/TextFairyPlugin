package com.renard.plugin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;

import com.googlecode.leptonica.android.Pix;
import com.renard.ocr.OCRActivity;
import com.renard.util.PreferencesUtils;
import com.renard.util.Screen;

import java.io.File;
import java.io.IOException;

/**
 * Created by ganymed on 18/08/15.
 */
public class PluginOCRActivity extends OCRActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void ocrErrorOccurred(Context context, String errorMessage) {
    sendOcrErrorOccurred(context, errorMessage);
    super.ocrErrorOccurred(context, errorMessage);
  }

  protected void sendOcrErrorOccurred(Context context, String errorMessage) {
    Intent errorMessageIntent = new Intent(context, PluginStartActivity.class);

    errorMessageIntent.putExtra(Constants.ERROR_MESSAGE_OCR_RESULT_EXTRA_NAME, errorMessage);

    setResult(RESULT_CANCELED, errorMessageIntent);
  }

  @Override
  protected void readingDocumentDone(Pix pix, String hocrString, String utf8String, int accuracy) {
    sendOcrResultToCaller(hocrString, utf8String, accuracy);

    cleanupDialog(pix);
    finish();
  }

  protected void sendOcrResultToCaller(String hocrString, String utf8String, int accuracy) {
    Intent resultIntent = new Intent(this, PluginStartActivity.class);

    resultIntent.putExtra(Constants.HOCR_OCR_RESULT_EXTRA_NAME, hocrString);
    resultIntent.putExtra(Constants.UTF8_OCR_RESULT_EXTRA_NAME, utf8String);
    resultIntent.putExtra(Constants.ACCURACY_OCR_RESULT_EXTRA_NAME, accuracy);

    setResult(RESULT_OK, resultIntent);
  }

  protected void cleanupDialog(Pix pix) {
    recycleResultPix(pix);
    Screen.unlockOrientation(this);
  }

  @Override
  protected void proceedToNextActivity() {
    // nothing to do here, we simply return to PluginStartActivity
  }

  @Override
  protected void askUserAboutDocumentLayout(Pix pixOrg, boolean accessibility) {
    startOcrLayoutAnalysis(pixOrg, PreferencesUtils.getPluginLastSelectedOCRLanguage(this), PreferencesUtils.getPluginLastSelectedLayoutKind(this));
  }


  @Override
  protected void saveDocument(Pix pix, String hocrString, String utf8String, int accuracy) {
    // avoid saving documents / images
  }

  // these to methods may can be removed
  @Override
  protected Uri saveDocumentToDB(File imageFile, String hocr, String plainText) throws RemoteException {
    return null;
  }

  @Override
  protected File saveImage(Pix p) throws IOException {
    return null;
  }
}
