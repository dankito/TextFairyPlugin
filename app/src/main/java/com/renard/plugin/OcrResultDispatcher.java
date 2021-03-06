package com.renard.plugin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ganymed on 18/08/15.
 */
public class OcrResultDispatcher {

  protected static Intent createSendResultIntent(String callingApplicationPackageName) {
    Intent sendIntent = new Intent(Constants.SEND_OCR_RESULT_INTENT_ACTION);

    if(callingApplicationPackageName != null) {
      sendIntent.setPackage(callingApplicationPackageName);
    }

    return sendIntent;
  }


  public static void sendOcrResult(Context context, String callingApplicationPackageName, String hocrString, String utf8String, int accuracy, OcrSource source, String ocrSourceUri) {
    Intent sendIntent = createSendResultIntent(callingApplicationPackageName);

    sendIntent.putExtra(Constants.RECOGNITION_SUCCESSFUL_OCR_RESULT_EXTRA_NAME, true);
    sendIntent.putExtra(Constants.HOCR_OCR_RESULT_EXTRA_NAME, hocrString);
    sendIntent.putExtra(Constants.UTF8_OCR_RESULT_EXTRA_NAME, utf8String);
    try { sendIntent.putExtra(Constants.ACCURACY_OCR_RESULT_EXTRA_NAME, accuracy / 100f);
    } catch(Exception ex) { Log.e("OcrResultDispatcher", "Could not convert integer Accuracy " + accuracy + " to a float value", ex); }

    sendIntent.putExtra(Constants.OCR_SOURCE_EXTRA_NAME, getSourceStringFromOcrSource(source));
    sendIntent.putExtra(Constants.OCR_SOURCE_URI_EXTRA_NAME, ocrSourceUri);

    context.sendBroadcast(sendIntent);
  }

  protected static String getSourceStringFromOcrSource(OcrSource source) {
    switch(source) {
      case CaptureImage:
        return Constants.OCR_SOURCE_CAPTURE_IMAGE;
      case SelectAnExistingImageOnDevice:
        return Constants.OCR_SOURCE_SELECT_AN_EXISTING_IMAGE_FROM_DEVICE;
      case RecognizeFromUri:
        return Constants.OCR_SOURCE_RECOGNIZE_FROM_URI;
      default:
        return "";
    }
  }

  public static void sendOcrError(Context context, String callingApplicationPackageName, String errorMessage) {
    Intent sendIntent = createSendResultIntent(callingApplicationPackageName);

    sendIntent.putExtra(Constants.RECOGNITION_SUCCESSFUL_OCR_RESULT_EXTRA_NAME, false);
    sendIntent.putExtra(Constants.ERROR_MESSAGE_OCR_RESULT_EXTRA_NAME, errorMessage);

    context.sendBroadcast(sendIntent);
  }

  public static void sendOcrProcessDone(Context context, String callingApplicationPackageName) {
    Intent sendIntent = createSendResultIntent(callingApplicationPackageName);

    sendIntent.putExtra(Constants.IS_DONE_OCR_RESULT_EXTRA_NAME, true);

    context.sendBroadcast(sendIntent);
  }

}
