package invmod.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;

public class SimplyID
{
  private static int nextSimplyID;
  private static Set<String> loadedIDs = new HashSet();
  private static String loadedWorld = null;
  private static File file = null;
  private static PrintWriter writer = null;

  public static String getNextSimplyID(Entity par1Entity) {
    loadSession(par1Entity.worldObj);

    nextSimplyID = 0;

    int i = nextSimplyID;
    while (true) {
      String id = EntityList.getEntityString(par1Entity) + nextSimplyID++;
      if (loadedIDs.add(id)) {
        writeIDToFile(id);
        return id;
      }
    }
  }

  public static void loadSession(World worldObj) {
    if ((loadedWorld == null) || (!worldObj.getSaveHandler().getWorldDirectoryName().equals(loadedWorld)))
      resetSimplyIDTo(worldObj);
  }

  public static void resetSimplyIDTo(World world)
  {
    if (writer != null) {
      writer.flush();
      writer.close();
    }
    loadedIDs.clear();

    loadedWorld = world.getSaveHandler().getWorldDirectoryName();
    String directory = "saves/" + loadedWorld + "/";
    file = new File(directory + "savedIDs.txt");
    try
    {
      writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true))); } catch (FileNotFoundException e) {
    } catch (IOException e) {
      e.printStackTrace();
    }

    populateSet();
  }

  public static void writeIDToFile(String id) {
    writer.println(id);
    writer.flush();
  }

  public static void populateSet() {
    FileReader pre = null;
    BufferedReader reader = null;
    try {
      pre = new FileReader(file);
      reader = new BufferedReader(pre);

      String line = null;
      try
      {
        while ((line = reader.readLine()) != null)
          if (line.startsWith("delete ")) {
            deleteID(line, Boolean.valueOf(false));
          }
          else
            addID(line);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      try
      {
        if (reader != null)
          reader.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    } catch (FileNotFoundException e) {
    }
    if (reader != null) {
      try {
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    refreshLoadedIDFile();
  }

  private static void refreshLoadedIDFile() {
    try {
      PrintWriter writer = new PrintWriter(file);

      for (String id : loadedIDs) {
        writer.println(id);
      }

      writer.flush();
      writer.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static Set<String> getLoadedIDs()
  {
    return loadedIDs;
  }

  public static void setLoadedIDs(Set<String> _loadedIDs) {
    loadedIDs = _loadedIDs;
  }

  public static void addID(String newID) {
    loadedIDs.add(newID);
  }

  public static void deleteID(String deletedID, Boolean flag)
  {
    if ((!flag.booleanValue()) && (deletedID.startsWith("delete "))) {
      deletedID = deletedID.split(" ")[1];
    }

    if (flag.booleanValue()) {
      writeIDToFile("delete " + deletedID);
    }

    loadedIDs.remove(deletedID);
  }

  public static void deleteID(World world, String string)
  {
    loadSession(world);

    deleteID(string, Boolean.valueOf(true));
  }
}
