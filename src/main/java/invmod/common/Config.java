package invmod.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config
{
  protected Properties properties;

  public void loadConfig(File configFile)
  {
    mod_Invasion.log("Loading config");
    this.properties = new Properties();
    try
    {
      if (!configFile.exists())
      {
        mod_Invasion.log("Config not found. Creating file 'invasion_config.txt' in minecraft directory");
        if (!configFile.createNewFile())
          mod_Invasion.log("Unable to create new config file.");
      }
      else
      {
        FileReader configRead = new FileReader(configFile);
        try
        {
          this.properties.load(configRead);
        }
        finally
        {
          configRead.close();
        }
      }

    }
    catch (FileNotFoundException e)
    {
      mod_Invasion.log(e.getMessage());
      mod_Invasion.log("Proceeding with default config");
    }
    catch (IOException e)
    {
      mod_Invasion.log(e.getMessage());
      mod_Invasion.log("Proceeding with default config");
    }
  }

  public void writeProperty(BufferedWriter writer, String key) throws IOException
  {
    writeProperty(writer, key, null);
  }

  public void writeProperty(BufferedWriter writer, String key, String comment) throws IOException
  {
    if (comment != null)
    {
      writer.write("# " + comment);
      writer.newLine();
    }

    writer.write(key + "=" + this.properties.getProperty(key));
    writer.newLine();
  }

  public void setProperty(String key, String value)
  {
    this.properties.setProperty(key, value);
  }

  public String getProperty(String key, String defaultValue)
  {
    return this.properties.getProperty(key, defaultValue);
  }

  public int getPropertyValueInt(String keyName, int defaultValue)
  {
    String property = this.properties.getProperty(keyName, "null");
    if (!property.equals("null"))
    {
      return Integer.parseInt(property);
    }

    this.properties.setProperty(keyName, Integer.toString(defaultValue));
    return defaultValue;
  }

  public float getPropertyValueFloat(String keyName, float defaultValue)
  {
    String property = this.properties.getProperty(keyName, "null");
    if (!property.equals("null"))
    {
      return Float.parseFloat(property);
    }

    this.properties.setProperty(keyName, Float.toString(defaultValue));
    return defaultValue;
  }

  public boolean getPropertyValueBoolean(String keyName, boolean defaultValue)
  {
    String property = this.properties.getProperty(keyName, "null");
    if (!property.equals("null"))
    {
      return Boolean.parseBoolean(property);
    }

    this.properties.setProperty(keyName, Boolean.toString(defaultValue));
    return defaultValue;
  }

  public String getPropertyValueString(String keyName, String defaultValue)
  {
    String property = this.properties.getProperty(keyName, "null");
    if (!property.equals("null"))
    {
      return property;
    }

    this.properties.setProperty(keyName, defaultValue);
    return defaultValue;
  }
}