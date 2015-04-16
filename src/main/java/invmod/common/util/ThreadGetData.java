package invmod.common.util;

import invmod.Invasion;

//do at start of client
public class ThreadGetData extends Thread {
    public ThreadGetData() {
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        Invasion.latestVersionNumber = VersionChecker.getLatestVersion();
        Invasion.recentNews = VersionChecker.getRecentNews();

    }
}