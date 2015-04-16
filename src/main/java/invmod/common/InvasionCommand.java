package invmod.common;

import invmod.Invasion;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;


public class InvasionCommand extends CommandBase {

    public void processCommand(ICommandSender sender, String[] args) {
        String username = sender.getCommandSenderName();
        if ((args.length > 0) && (args.length <= 7)) {
            if (args[0].equals("help")) {
                sender.addChatMessage(new ChatComponentText("--- Showing Invasion help page 1 of 1 ---").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                sender.addChatMessage(new ChatComponentText("/begin x to start a wave"));
                sender.addChatMessage(new ChatComponentText("/end to end the invasion"));
                sender.addChatMessage(new ChatComponentText("/range x to set the spawn range"));
            } else if (args[0].equals("begin")) {
                if (args.length == 2) {
                    int startWave = Integer.parseInt(args[1]);
                    if (Invasion.getFocusNexus() != null) {
                        Invasion.getFocusNexus().debugStartInvaion(startWave);
                    }
                }
            } else if (args[0].equals("end")) {
                if (Invasion.getActiveNexus() != null) {
                    Invasion.getActiveNexus().emergencyStop();
                    Invasion.broadcastToAll(username + " ended invasion");
                } else {
                    sender.addChatMessage(new ChatComponentText(username + ": No invasion to end"));
                }
            } else if (args[0].equals("range")) {
                if (args.length == 2) {
                    int radius = Integer.parseInt(args[1]);
                    if (Invasion.getFocusNexus() != null) {
                        if ((radius >= 32) && (radius <= 128)) {
                            if (Invasion.getFocusNexus().setSpawnRadius(radius)) {
                                sender.addChatMessage(new ChatComponentText("Set nexus range to " + radius));
                            } else {
                                sender.addChatMessage(new ChatComponentText(username + ": Can't change range while nexus is active"));
                            }
                        } else {
                            sender.addChatMessage(new ChatComponentText(username + ": Range must be between 32 and 128"));
                        }
                    } else {
                        sender.addChatMessage(new ChatComponentText(username + ": Right-click the nexus first to set target for command"));
                    }
                }
            } else if (args[0].equals("spawnertest")) {
                int startWave = 1;
                int endWave = 11;

                if (args.length >= 4)
                    return;
                if (args.length >= 3)
                    endWave = Integer.parseInt(args[2]);
                if (args.length >= 2) {
                    startWave = Integer.parseInt(args[1]);
                }
                Tester tester = new Tester();
                tester.doWaveSpawnerTest(startWave, endWave);
            } else if (args[0].equals("pointcontainertest")) {
                Tester tester = new Tester();
                tester.doSpawnPointSelectionTest();
            } else if (args[0].equals("wavebuildertest")) {
                float difficulty = 1.0F;
                float tierLevel = 1.0F;
                int lengthSeconds = 160;

                if (args.length >= 5)
                    return;
                if (args.length >= 4)
                    lengthSeconds = Integer.parseInt(args[3]);
                if (args.length >= 3)
                    tierLevel = Float.parseFloat(args[2]);
                if (args.length >= 2) {
                    difficulty = Float.parseFloat(args[1]);
                }
                Tester tester = new Tester();
                tester.doWaveBuilderTest(difficulty, tierLevel, lengthSeconds);
            } else if (args[0].equals("nexusstatus")) {
                if (Invasion.getFocusNexus() != null)
                    Invasion.getFocusNexus().debugStatus();
            } else if (args[0].equals("bolt")) {
                if (Invasion.getFocusNexus() != null) {
                    int x = Invasion.getFocusNexus().getXCoord();
                    int y = Invasion.getFocusNexus().getYCoord();
                    int z = Invasion.getFocusNexus().getZCoord();
                    int time = 40;
                    if (args.length >= 6)
                        return;
                    if (args.length >= 5)
                        time = Integer.parseInt(args[4]);
                    if (args.length >= 4)
                        z += Integer.parseInt(args[3]);
                    if (args.length >= 3)
                        y += Integer.parseInt(args[2]);
                    if (args.length >= 2) {
                        x += Integer.parseInt(args[1]);
                    }
                    Invasion.getFocusNexus().createBolt(x, y, z, time);
                }
            } else if (args[0].equals("status")) {
                sender.addChatMessage(new ChatComponentText("nexus status:" + Invasion.getFocusNexus().isActive()));

            } else {
                sender.addChatMessage(new ChatComponentText("Command not recognised, use /invasion help for a list of all the available commands").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            }

        }
    }

    public String getCommandName() {
        return "invasion";
    }

    public String getCommandUsage(ICommandSender icommandsender) {
        return "";
    }
}