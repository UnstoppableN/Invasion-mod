package invmod.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import invmod.common.mod_Invasion;

public class VersionChecker {

	
	public static boolean checkForUpdates(EntityPlayerMP entityplayer)
	{
			try{
			if(mod_Invasion.getUpdateNotifications() && mod_Invasion.getLatestVersionNumber() != null && mod_Invasion.getRecentNews() != null)
			{
				if(!mod_Invasion.getLatestVersionNumber().equals("null"))
				{
					if(Version.get(mod_Invasion.getLatestVersionNumber()).comparedState(mod_Invasion.getVersionNumber()) == 1 )
					{
						mod_Invasion.sendMessageToPlayer(entityplayer,"Invasion mod outdated, consider updating to the latest version");
						mod_Invasion.sendMessageToPlayer(entityplayer,"Changes in v"+mod_Invasion.getLatestVersionNumber()+": " + mod_Invasion.getRecentNews());
						return true;
					}				
				}
			}
			} catch(Exception e) {}
		return false;
	}

	//Get and return latest version
	public static String getLatestVersion()
	{
		String[] text = merge(getHTML("https://dl.dropboxusercontent.com/u/96357007/invasion_mod/Invasion_mod.txt")).split(":");
		if(!text[0].contains("UTF-8") && !text[0].contains("HTML") && !text[0].contains("http")) return text[0];
		return "null";
	}

	//Get and return recent News
	public static String getRecentNews()
	{
		String[] text = merge(getHTML("https://dl.dropboxusercontent.com/u/96357007/invasion_mod/Invasion_mod.txt")).split(":");
		if(text.length > 1 && !text[1].contains("UTF-8") && !text[1].contains("HTML") && !text[1].contains("http")) return text[1];
		return "null";
	}
	
	public static List<String> getHTML(String urlToRead)
	{
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		List<String> result = new ArrayList<String>();

		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while((line = rd.readLine()) != null)
			{
				result.add(line.trim());
			}

			rd.close();
		} catch(Exception e) {
			result.clear();
			result.add("null");
			mod_Invasion.log("An error occured while connecting to URL '" + urlToRead + ".'");
		}

		return result;
	}
	
	public static String merge(List<String> text)
	{
		StringBuilder builder = new StringBuilder();

		for(String s : text)
		{
			builder.append(s);
		}

		return builder.toString();
	}
}
