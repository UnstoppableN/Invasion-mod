package invmod.common.util;

import invmod.common.mod_Invasion;

//do at start of client
public class ThreadGetData extends Thread
{
	public ThreadGetData()
	{
		setDaemon(true);
		start();
	}

	@Override
	public void run()
	{
		mod_Invasion.latestVersionNumber = VersionChecker.getLatestVersion();
		mod_Invasion.recentNews = VersionChecker.getRecentNews();

	}
}