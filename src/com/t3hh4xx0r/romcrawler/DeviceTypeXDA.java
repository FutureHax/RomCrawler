package com.t3hh4xx0r.romcrawler;

public enum DeviceTypeXDA {
	   TORO("http://forum.xda-developers.com/forumdisplay.php?f=1455"), 
	   VIGOR("http://forum.xda-developers.com/forumdisplay.php?f=1392"),
	   ACE("http://forum.xda-developers.com/forumdisplay.php?f=765"),
	   ERIS("http://forum.xda-developers.com/forumdisplay.php?f=554"),
	   INC("http://forum.xda-developers.com/forumdisplay.php?f=638"),
	   VIVOW("http://forum.xda-developers.com/forumdisplay.php?f=1141"),
	   SHOOTER("http://forum.xda-developers.com/forumdisplay.php?f=1098"),
	   SUPERSONIC("http://forum.xda-developers.com/forumdisplay.php?f=653"),
	   SPEEDY("http://forum.xda-developers.com/forumdisplay.php?f=938"),
	   VISION("http://forum.xda-developers.com/forumdisplay.php?f=758"),
	   DOUBLESHOT("http://forum.xda-developers.com/forumdisplay.php?f=1209"),
	   PASSION("http://forum.xda-developers.com/forumdisplay.php?f=559"),
	   PYRAMID("http://forum.xda-developers.com/forumdisplay.php?f=1112"),
	   CDMA_TARGA("http://forum.xda-developers.com/forumdisplay.php?f=1006"),
	   OLYMPUS("http://forum.xda-developers.com/forumdisplay.php?f=997"),
	   CDMA_SPYDER("http://forum.xda-developers.com/forumdisplay.php?f=1363"),
	   MECHA("http://forum.xda-developers.com/forumdisplay.php?f=943"),
	   MAGURO("http://forum.xda-developers.com/forumdisplay.php?f=1339"),
	   SHOLES("http://forum.xda-developers.com/forumdisplay.php?f=671"),
	   CDMA_DROID2("http://forum.xda-developers.com/forumdisplay.php?f=741"),
	   CDMA_DROID2WE("http://forum.xda-developers.com/forumdisplay.php?f=741"),
	   CRESPO("http://forum.xda-developers.com/forumdisplay.php?f=883"),
	   CRESPO4G("http://forum.xda-developers.com/forumdisplay.php?f=1171"),
       SPHD710("http://forum.xda-developers.com/forumdisplay.php?f=1284"),
	   CDMA_SHADOW("http://forum.xda-developers.com/forumdisplay.php?f=691"),
           SPHD700("http://forum.xda-developers.com/forumdisplay.php?f=716"),
           BLAZE("http://forum.xda-developers.com/forumdisplay.php?f=1309"),
           STINGRAY("http://forum.xda-developers.com/forumdisplay.php?f=948"),
           TENDERLOIN("http://forum.xda-developers.com/forumdisplay.php?f=1247"),
           ENCORE("http://forum.xda-developers.com/forumdisplay.php?f=864"),
           P970("http://forum.xda-developers.com/forumdisplay.php?f=1076"),
           P999("http://forum.xda-developers.com/forumdisplay.php?f=1117"),
           GTP7510("http://forum.xda-developers.com/forumdisplay.php?f=1132"),
           CDMA_SOLANA("http://forum.xda-developers.com/forumdisplay.php?f=1196"),
           SUNFIRE("http://forum.xda-developers.com/forumdisplay.php?f=1223"),
           VENUS2("http://forum.xda-developers.com/forumdisplay.php?f=834"),
           DAYTONA("http://forum.xda-developers.com/forumdisplay.php?f=834"),
           GALAXYS2("http://forum.xda-developers.com/forumdisplay.php?f=1204"),
           SGHT89("http://forum.xda-developers.com/forumdisplay.php?f=1383"),
           SCHI510("http://forum.xda-developers.com/forumdisplay.php?f=1146"),
           SGHT839("http://forum.xda-developers.com/forumdisplay.php?f=1128"),
           SGHT989("http://forum.xda-developers.com/forumdisplay.php?f=1332"),
           SGHI777("http://forum.xda-developers.com/forumdisplay.php?f=1304"),
           SGHT959V("http://forum.xda-developers.com/forumdisplay.php?f=1068"),
           INFUSE4G("http://forum.xda-developers.com/forumdisplay.php?f=1161");



	    private final String forumUrl;

	    private DeviceTypeXDA(String forumUrl) {
	        this.forumUrl = forumUrl;
	    }

	    public String getForumUrl() {
	        return forumUrl;
	    }
}
