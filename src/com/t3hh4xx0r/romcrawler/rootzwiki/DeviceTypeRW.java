package com.t3hh4xx0r.romcrawler.rootzwiki;

public enum DeviceTypeRW {
	   TORO("http://rootzwiki.com/forum/362-cdma-galaxy-nexus-developer-forum"), 
	   VIGOR("http://rootzwiki.com/forum/267-rezound-developer-forum/"),
	   ACE("http://rootzwiki.com/forum/163-desire-hd-developer-forum"),
	   ERIS("http://rootzwiki.com/forum/34-droid-eris-developer-forum"),
	   INC("http://rootzwiki.com/forum/22-droid-incredible-developer-forum"),
	   VIVOW("http://rootzwiki.com/forum/60-droid-incredible-2-developer-forum"),
	   SHOOTER("http://rootzwiki.com/forum/113-evo-3d-developer-forum"),
	   SUPERSONIC("http://rootzwiki.com/forum/36-evo-4g-developer-forum"),
	   SPEEDY("http://rootzwiki.com/forum/202-evo-shift-4g-developer-forum"),
	   VISION("http://rootzwiki.com/forum/109-g2-vision-developer-forum"),
	   DOUBLESHOT("http://rootzwiki.com/forum/142-mytouch-4g-slide-developer-forum"),
	   PASSION("http://rootzwiki.com/forum/75-nexus-one-developer-forum"),
	   PYRAMID("http://rootzwiki.com/forum/88-sensation-4g-development-forum"),
	   CDMA_TARGA("http://rootzwiki.com/forum/82-droid-bionic-developer-forum"),
	   OLYMPUS("http://rootzwiki.com/forum/44-atrix-developer-forum"),
	   CDMA_SPYDER("http://rootzwiki.com/forum/338-droid-razr-developer-forum"),
	   MECHA("http://rootzwiki.com/forum/12-thunderbolt-developer-forum"),
	   MAGURO("http://rootzwiki.com/forum/230-gsm-galaxy-nexus-developer-forum"),
	   SHOLES("http://://rootzwiki.com/forum/28-droid-1-developer-forum/"),
	   CDMA_DROID2("http://rootzwiki.com/forum/15-droid-2r2d2-developer-forum"),
	   CDMA_DROID2WE("http://rootzwiki.com/forum/76-droid-2-global-developer-forum"),
	   CRESPO("http://rootzwiki.com/forum/32-nexus-s-developer-forum/"),
	   CRESPO4G("http://rootzwiki.com/forum/140-nexus-s-4g-developer-forum/"),
	   CDMA_SHADOW("http://rootzwiki.com/forum/16-droid-x-developer-forum"),
       SPHD700("http://rootzwiki.com/forum/78-epic-4g-developer-forum/"),
	   SPHD710("http://rootzwiki.com/forum/237-sprint-epic-4g-touch-developer-forum/"),
       BLAZE("http://rootzwiki.com/forum/352-kindle-fire-developer-forum/"),
    WINGRAY("http://rootzwiki.com/forum/26-xoom-developer-forum/"),
    TENDERLOIN("http://rootzwiki.com/forum/232-hp-touchpad-android-developer-forum/"),
    ENCORE("http://rootzwiki.com/forum/223-bn-nook-color-developer-forum/"),
    P970("http://rootzwiki.com/forum/245-lg-thrill-optimus-3d-developer-forum/"),
    P999("http://rootzwiki.com/forum/51-g2x-developer-forum/"),
    GTP7510("http://rootzwiki.com/forum/63-galaxy-tab-101-wifi-developer-forum/"),
    SCHI800("http://rootzwiki.com/forum/46-galaxy-tab-7-inch-developer-forum/"),
    CDMA_SOLANA("http://rootzwiki.com/forum/121-droid-3-developer-forum/"),
    SUNFIRE("http://rootzwiki.com/forum/222-photon-4g-developer-forum/"),
    VENUS2("http://rootzwiki.com/forum/24-droid-pro-developer-forum/"),
    DAYTONA("http://rootzwiki.com/forum/20-droid-x2-developer-forum/"),
    LEXIKON("http://rootzwiki.com/forum/335-merge-developer-forum/"),
    GALAXYS2("http://rootzwiki.com/forum/96-galaxy-s-ii-developer-forum/"),
    SGHT89("http://rootzwiki.com/forum/354-att-galaxy-s-ii-skyrocket-developer-forum/"),
    SCHI510("http://rootzwiki.com/forum/59-droid-charge-developer-forum/"),
    SGHT839("http://rootzwiki.com/forum/100-sidekick-4g-developer-forum/"),
    SGHT989("http://rootzwiki.com/forum/266-t-mobile-galaxy-s-ii-developer-forum/"),
    SGHI777("http://rootzwiki.com/forum/265-att-galaxy-s-ii-developer-forum/"),
    SGHT959V("http://rootzwiki.com/forum/98-galaxy-s-4g-developer-forum/"),
    INFUSE4G("http://rootzwiki.com/forum/111-infuse-4g-developer-forum/");


	    private final String forumUrl;

	    private DeviceTypeRW(String forumUrl) {
	        this.forumUrl = forumUrl;
	    }

	    public String getForumUrl() {
	        return forumUrl;
	    }
}
