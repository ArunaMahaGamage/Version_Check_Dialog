# Android App Version Check Width Play Store

* Add Jsoup Libery to Gradle

      // Version Check
      implementation 'org.jsoup:jsoup:1.11.3'
      
      
* Create VersionChecker Class

        public class VersionChecker extends AsyncTask<String, String, String> {

        String newVersion;

        @Override
        protected String doInBackground(String... params) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.aruna.test" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }
      }
    
    
 * Add This Code to Splash Activity or Your MAin Activity or Some Thing
 
            private void checkVersion() {
              VersionChecker versionChecker = new VersionChecker();
              try {
                  PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
                  String gradleVersion = pInfo.versionName;

                  String playStoreVersion = versionChecker.execute().get();
                  Log.e("Version_Code", playStoreVersion);

                  if (!(gradleVersion.equalsIgnoreCase(playStoreVersion))) {
                      showVersionCheckDialog();
                  }

              } catch (ExecutionException e) {
                  e.printStackTrace();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              } catch (PackageManager.NameNotFoundException e) {
                  e.printStackTrace();
              }
          }

          public void showVersionCheckDialog(){

              final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
              // Add the buttons
              builder.setTitle("Update Check");
              builder.setMessage("A New version of Application is Available for your best experience");
              builder.setPositiveButton("Update Now", new android.content.DialogInterface.OnClickListener() {
                  public void onClick(android.content.DialogInterface dialog, int id) {
                      // User clicked OK button

                      final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                      try {
                          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                      } catch (android.content.ActivityNotFoundException anfe) {
                          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                      }

                  }
              });
              builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(android.content.DialogInterface dialog, int which) {
                      dialog.dismiss();
                  }
              });
              // Set other dialog properties

              // Create the AlertDialog
              android.app.AlertDialog dialog = builder.create();
              dialog.show();
          }
