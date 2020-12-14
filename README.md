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
```java 
allprojects {
     repositories {
	maven { url 'https://jitpack.io' }
	}
   }
```
* In your app module gradle file just need to add the dependency
```java 
dependencies 
   {
     implementation 'com.github.AnviamSolutions:QR-Barcode-Scanner:1.0.1'
   }
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
 mScannerView.setResultHandler(rawResult -> {
        if (rawResult!=null && !TextUtils.isEmpty(rawResult.getText())){
            //Todo method to get can content
        }        
       });
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
