package me.StevenLawson.TotalFreedomMod;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.DEVELOPERS;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.EXECS;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.SECURITYOFFICERS;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.OWNERS;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum TFM_PlayerRank
{
    DEVELOPER("a " + ChatColor.DARK_PURPLE + "Developer", ChatColor.DARK_PURPLE + "[Dev]"),
    IMPOSTOR("an " + ChatColor.GREY + ChatColor.UNDERLINE + "Impostor", ChatColor.GREY.toString() + ChatColor.UNDERLINE + "[IMP]"),
    NON_OP("a " + ChatColor.RESET + "Non-OP", ChatColor.RESET.toString()),
    OP("an " + ChatColor.LIGHT_PURPLE + "OP", ChatColor.LIGHT_PURPLE + "[OP]"),
    SUPER("an " + ChatColor.YELLOW + "Admin", ChatColor.YELLOW + "[Admin]"),
    TELNET("a " + ChatColor.GREEN + "Super Admin", ChatColor.GREEN + "[SA]"),
    SENIOR("a " + ChatColor.RED + "Senior Admin", ChatColor.RED + "[SrA]"),
    OWNER("the " + ChatColor.BLUE + "Owner", ChatColor.BLUE + "[Owner]"),
    CONSOLE("The " + ChatColor.DARK_PURPLE + "Console", ChatColor.DARK_PURPLE + "[Console]"),
    SECURITYOFFICER("The " + ChatColor.AQUA + "Security Officer", ChatColor.AQUA + "[Security]"),
    EXEC("an ") + ChatColor.DARK_RED + "Executive", ChatColor.DARK_RED + "[Executive]");
    private final String loginMessage;
    private final String prefix;

    private TFM_PlayerRank(String loginMessage, String prefix)
    {
        this.loginMessage = loginMessage;
        this.prefix = prefix;
    }

    public static String getLoginMessage(CommandSender sender)
    {
        // Handle console
        if (!(sender instanceof Player))
        {
            return fromSender(sender).getLoginMessage();
        }

        // Handle admins
        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);
        if (entry == null)
        {
            // Player is not an admin
            return fromSender(sender).getLoginMessage();
        }

        // Custom login message
        final String loginMessage = entry.getCustomLoginMessage();

        if (loginMessage == null || loginMessage.isEmpty())
        {
            return fromSender(sender).getLoginMessage();
        }

        return ChatColor.translateAlternateColorCodes('&', loginMessage);
    }

    public static TFM_PlayerRank fromSender(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return CONSOLE;
        }

        if (TFM_AdminList.isAdminImpostor((Player) sender))
        {
            return IMPOSTOR;
        }

        if (DEVELOPERS.contains(sender.getName()))
        {
            return DEVELOPER;
        }
        
        if (EXECS.contains(sender.getName()))
        {
            return EXEC;
        }

        final TFM_Admin entry = TFM_AdminList.getEntryByIp(TFM_Util.getIp((Player) sender));

        final TFM_PlayerRank rank;

        if (entry != null && entry.isActivated())
        {
            if (TFM_ConfigEntry.SERVER_OWNERS.getList().contains(sender.getName()))
            {
                return OWNER;
            }

            if (entry.isSeniorAdmin())
            {
                rank = SENIOR;
            }
            else if (entry.isTelnetAdmin())
            {
                rank = TELNET;
            }
            else
            {
                rank = SUPER;
            }
        }
        else
        {
            if (sender.isOp())
            {
                rank = OP;
            }
            else
            {
                rank = NON_OP;
            }

        }
        return rank;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getLoginMessage()
    {
        return loginMessage;
    }
}
