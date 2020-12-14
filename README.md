# QR-Barcode-Scanner
QR &amp; Barcode Scanner library based on zxing &amp; full stable QR scanner code in android

# Feature
* Use Flash-Light
* Use Camera Facing 
* Share QR/Bar Code content
* Search QR/Bar Code content into web browser
* Copy to clipboard QR/Bar Code content
* Easy to use

# How to use
* Need to add in root gradle file as following :
```gradle 
allprojects {
     repositories {
	maven { url 'https://jitpack.io' }
	}
   }
```
* In your app module gradle file just need to add the dependency
```gradle 
dependencies 
   {
     implementation 'com.github.AnviamSolutions:QR-Barcode-Scanner:1.0.1'
   }
```
* Add camera permission to AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.CAMERA" />
    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />
    <uses-permission android:name="android.permission.FLASHLIGHT"/>
```

* Add frame layout into your activity xml
```java
<FrameLayout
        android:id="@+id/content_frame"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </FrameLayout>
```
* In Your activity, intialize Scanner
```java
  ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new AnviamScannerView(this);
        contentFrame.addView(mScannerView);
```
* In Your activity, Declare handle result
```java

@Override
    public void onResume() {
        super.onResume();
        if (mScannerView!=null)
            mScannerView.setResultHandler(rawResult -> {
        if (rawResult!=null && !TextUtils.isEmpty(rawResult.getText())){
            //Todo method to get can content
        }        
       });
        if (mScannerView!=null){
            mScannerView.setCameraFacing(ToggleButtonCamera.isChecked());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScannerView!=null)
            mScannerView.stopCamera();
    }
 ```
# Flip Camera (Back/Front)
```java
  mScannerView.setCameraFacing(b);
 ```
# Flash Light(On/Off)
```java
 mScannerView.setFlashLight(b);
 ```
# Found a bug?
Submit a [github issue](https://github.com/AnviamSolutions/QR-Barcode-Scanner/issues/new)
# Screenshots
<img src="/Screenshot_20201214-201651[1].png"/>
