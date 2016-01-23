package com.renard.plugin;

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
  protected void readingDocumentDone(Pix pix, String hocrString, String utf8String, int accuracy) {
    OcrResultDispatcher.ocrRecognitionSuccessful(this, hocrString, utf8String, accuracy);

    cleanupDialog(pix);
    finish();
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

  // avoid saving documents / images

  @Override
  protected void saveDocument(Pix pix, String hocrString, String utf8String, int accuracy) {

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
