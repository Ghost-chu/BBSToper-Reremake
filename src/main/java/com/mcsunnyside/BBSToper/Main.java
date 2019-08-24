package com.mcsunnyside.BBSToper;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Main extends JavaPlugin {

    private static int tid = 0;
    private static long changeidcooldown;
    private static int rwoneday;
    private static int pagesize;
    private static String repeat = "";
    private static String bdsuccess = "";
    private static String rwsuccess = "";
    private static String notsame = "";
    private static String oncooldown = "";
    private static String noaccount = "";
    private static String reward = "";
    private static String noreward = "";
    private static String reload = "";
    private static String nopermission = "";
    private static String invalid = "";
    private static String enable = "";
    private static String usage = "";
    private static String posterid = "";
    private static String postertime = "";
    private static String noposter = "";
    private static String posternum = "";
    private static String overtime = "";
    private static String waitamin = "";
    private static String samebinding = "";
    private static String ownsamebinding = "";
    private static String overpage = "";
    private static String pageinfo = "";
    private static List<String> cmds = new ArrayList<>();
    private static List<String> help = new ArrayList<>();
    private static List<String> ID = new ArrayList<>();
    private static List<String> Time = new ArrayList<>();
    private static Map<String, String> binding = new HashMap<>();
    private FileConfiguration poster = null;
    private File posterFile = null;
    private Set<String> pendingProcessRequest = new HashSet<>();
    private Queue<String> pendingCommands = new LinkedList<>();

    @Override
    public void onEnable() {
        //No CatServer BEGIN
        getLogger().log(new LogRecord(Level.CONFIG,"禁止CatServer或其他开发者移除本段代码，开发者保留要求删除修改分支、二进制文件的权利"));
        if (getServer().getName().toLowerCase().contains("catserver")) {
            // Send FATAL ERROR TO CatServer's users.
            getLogger().log(new LogRecord(Level.CONFIG,"禁止CatServer或其他开发者移除本段代码，开发者保留要求删除修改分支、二进制文件的权利"));
            getLogger().severe("禁用：BBSToper-Rerename 不支持CatServer服务端。");
            Bukkit.getPluginManager().disablePlugin(this);
            getLogger().log(new LogRecord(Level.CONFIG,"禁止CatServer或其他开发者移除本段代码，开发者保留要求删除修改分支、二进制文件的权利"));
            return;
        }
        getLogger().log(new LogRecord(Level.CONFIG,"禁止CatServer或其他开发者移除本段代码，开发者保留要求删除修改分支、二进制文件的权利"));
        //No CatServer END
        getLogger().info("BBSToper 重置 by Ghost_chu");
        getLogger().info("考虑一下给作者的服务器使用一张提升卡来支持作者吧!  https://www.mcbbs.net/thread-837933-1-1.html");
        this.saveDefaultConfig();
        this.saveDefaultPoster();
        this.Load();
        getLogger().info(enable);// log输出
        new BukkitRunnable(){
            @Override
            public void run() {
                String command =pendingCommands.poll();
                while (command != null){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
                    command = pendingCommands.poll();
                }
            }
        }.runTaskTimer(this, 0,10);
    }

    @Override
    public void onDisable() {// 只有玩家数据会在关服时保存
        this.savePoster();// 保存玩家数据
    }

    private void Reload() {// 重载时不会保存任何数据
        this.reloadConfig();// 重载配置
        this.reloadPoster();// 重载数据
        this.Load();// 加载配置和数据
    }

    private void Load() {// 加载配置和数据
        this.getConfig();
        this.getPoster();
        tid = this.getConfig().getInt("tid");
        changeidcooldown = this.getConfig().getLong("changeidcooldown");
        rwoneday = this.getConfig().getInt("rwoneday");
        pagesize = this.getConfig().getInt("pagesize");
        cmds = this.getConfig().getStringList("rewards");
        String prefix = Objects.requireNonNull(this.getConfig().getString("messages.prefix")).replaceAll("&", "§");// 前缀
        help = this.getConfig().getStringList("messages.help");// 帮助
        for (int i = 0; i < help.size(); i++) {
            help.set(i, prefix + help.get(i).replaceAll("&", "§"));
        }
        repeat = prefix + Objects.requireNonNull(this.getConfig().getString("messages.repeat")).replaceAll("&", "§");
        bdsuccess = prefix + Objects.requireNonNull(this.getConfig().getString("messages.bdsuccess")).replaceAll("&", "§");
        rwsuccess = prefix + Objects.requireNonNull(this.getConfig().getString("messages.rwsuccess")).replaceAll("&", "§");
        notsame = prefix + Objects.requireNonNull(this.getConfig().getString("messages.notsame")).replaceAll("&", "§");
        oncooldown = prefix + Objects.requireNonNull(this.getConfig().getString("messages.oncooldown")).replaceAll("&", "§");
        noaccount = prefix + Objects.requireNonNull(this.getConfig().getString("messages.noaccount")).replaceAll("&", "§");
        reward = prefix + Objects.requireNonNull(this.getConfig().getString("messages.reward")).replaceAll("&", "§");
        noreward = prefix + Objects.requireNonNull(this.getConfig().getString("messages.noreward")).replaceAll("&", "§");
        reload = prefix + Objects.requireNonNull(this.getConfig().getString("messages.reload")).replaceAll("&", "§");
        nopermission = prefix + Objects.requireNonNull(this.getConfig().getString("messages.nopermission")).replaceAll("&", "§");
        invalid = prefix + Objects.requireNonNull(this.getConfig().getString("messages.invalid")).replaceAll("&", "§");
        enable = Objects.requireNonNull(this.getConfig().getString("messages.enable")).replaceAll("&", "§");
        usage = prefix + Objects.requireNonNull(this.getConfig().getString("messages.usage")).replaceAll("&", "§");
        posterid = Objects.requireNonNull(this.getConfig().getString("messages.posterid")).replaceAll("&", "§");
        postertime = Objects.requireNonNull(this.getConfig().getString("messages.postertime")).replaceAll("&", "§");
        noposter = prefix + Objects.requireNonNull(this.getConfig().getString("messages.noposter")).replaceAll("&", "§");
        posternum = prefix + Objects.requireNonNull(this.getConfig().getString("messages.posternum")).replaceAll("&", "§");
        overtime = prefix + Objects.requireNonNull(this.getConfig().getString("messages.overtime")).replaceAll("&", "§");
        waitamin = prefix + Objects.requireNonNull(this.getConfig().getString("messages.waitamin")).replaceAll("&", "§");
        samebinding = prefix + Objects.requireNonNull(this.getConfig().getString("messages.samebinding")).replaceAll("&", "§");
        ownsamebinding = prefix + Objects.requireNonNull(this.getConfig().getString("messages.ownsamebinding")).replaceAll("&", "§");
        overpage = prefix + Objects.requireNonNull(this.getConfig().getString("messages.overpage")).replaceAll("&", "§");
        pageinfo = prefix + Objects.requireNonNull(this.getConfig().getString("messages.pageinfo")).replaceAll("&", "§");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (cmd.getName().equalsIgnoreCase("poster")) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("binding")) {// 绑定账号
                            sender.sendMessage(usage);// 提示用法
                        } else if (args[0].equalsIgnoreCase("list")) {
                            if (topList().size() != 0) {
                                outputToplist(sender, 0);
                            } else {
                                sender.sendMessage(noposter);
                            }
                        } else if (args[0].equalsIgnoreCase("reward")) {// 回报
                            if (getPoster().getString(sender.getName() + ".id") == null) {// 如果没有绑定账号
                                sender.sendMessage(noaccount);
                            } else {// 如果绑定了账号
                                if(pendingProcessRequest.contains(sender.getName())){
                                    getLogger().info(sender.getName()+" 重复请求，已取消");
                                    return;
                                }
                                pendingProcessRequest.add(sender.getName());
                                boolean b = false;
                                boolean isovertime = false;
                                List<String> list = new ArrayList<>();
                                boolean iswaitamin = false;
                                Getter();// 开始抓取网页
                                for (int i = 0; i < ID.size() && i < Time.size(); i++) {// 遍历网页抓取到的信息
                                    if (ID.get(i).equalsIgnoreCase(getPoster().getString(sender.getName() + ".id"))) {// 如果此次的ID包含玩家绑定的名字
                                        if (!list.contains(Time.get(i))) {// 如果list中不包含这次遍历到的时间
                                            list.add(Time.get(i));// 放进时间
                                        } else {// 如果List包含这次时间
                                            if (!getPoster().getStringList(sender.getName() + ".time")
                                                    .contains(Time.get(i))) {// 如果配置文件中没有包含这次时间
                                                iswaitamin = true;// 顶帖时间重复
                                            }
                                        }
                                        if (!getPoster().getStringList(sender.getName() + ".time")
                                                .contains(Time.get(i))) {// 如果此次循环到的time不被包含在储存的数据中
                                            String datenow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                            if (!datenow
                                                    .equals(getPoster().getString(sender.getName() + ".daybefore"))) {// 如果系统日期不等于数据文件中的日期
                                                getPoster().set(sender.getName() + ".rwaday", 0);// 次数归零
                                                getPoster().set(sender.getName() + ".daybefore", datenow);// 更新数据文件中的"日期"
                                            }
                                            if ((getPoster().getInt(sender.getName() + ".rwaday") < rwoneday)) {// 如果玩家当日次数小于设定值
                                                for (String s : cmds) {// 遍历奖励指令
                                                    pendingCommands.add(s.replaceAll("%PLAYER%", sender.getName()));// 遍历领奖指令
                                                }
                                                List<String> time = getPoster()
                                                        .getStringList(sender.getName() + ".time");// 此玩家的time List
                                                time.add(Time.get(i));// 放入此次领过奖的时间
                                                getPoster().set(sender.getName() + ".time", time);// 保存新的time List
                                                getPoster().set(sender.getName() + ".rwaday",
                                                        getPoster().getInt(sender.getName() + ".rwaday") + 1);// 次数+1
                                                savePoster();// 保存数据
                                                sender.sendMessage(reward.replaceAll("%TIME%", Time.get(i)));// 领奖提示
                                                b = true;// 放入领取成功
                                            } else {// 如果大于设定
                                                isovertime = true;// 放入超过次数
                                            }
                                        }
                                    }
                                } // 整个遍历完成
                                if (b) {// 如果领取成功
                                    sender.sendMessage(rwsuccess);
                                } else {// 如果领取不成功
                                    if (isovertime) {// 如果超过次数
                                        sender.sendMessage(overtime.replaceAll("%RWONEDAY%", String.valueOf(rwoneday)));// 提示超过次数
                                    } else {// 没有超过次数
                                        if (iswaitamin) {// 是否重复,间隔少于1min
                                            sender.sendMessage(waitamin);// 提示间隔问题
                                        } else {
                                            sender.sendMessage(noreward);// 没找到记录
                                        }
                                    }
                                }
                                pendingProcessRequest.remove(sender.getName());
                            }
                        } else if (args[0].equalsIgnoreCase("reload")) {// 重载
                            if (sender.hasPermission("bbstoper.admin")) {
                                Reload();
                                sender.sendMessage(reload);
                            } else {
                                sender.sendMessage(nopermission);// 没权限
                            }
                        } else {
                            sender.sendMessage(invalid);
                        }
                    }
                    if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("binding")) {// 绑定账号
                            String id = getPoster().getString(sender.getName() + ".id");
                            long date = System.currentTimeMillis() - getPoster().getLong(sender.getName() + ".date");
                            boolean iscd = date > changeidcooldown * 86400000;
                            if (id == null || iscd) {// 如果之前没有绑定过或解绑冷却已过
                                if (binding.get(sender.getName()) == null) {// 如果没有重复确认过绑定
                                    binding.put(sender.getName(), args[1]);
                                    sender.sendMessage(repeat);
                                } else if (binding.get(sender.getName()).equals(args[1])) {// 如果重复正确
                                    if (!isSame(args[1])) {// 如果没有相同
                                        getPoster().set(sender.getName() + ".id", args[1]);
                                        getPoster().set(sender.getName() + ".date", System.currentTimeMillis());// 记录时间
                                        getPoster().set(sender.getName() + ".daybefore", "");// 之前的日期
                                        getPoster().set(sender.getName() + ".rwaday", 0);// 当天次数为0
                                        savePoster();// 保存数据
                                        binding.put(sender.getName(), null);// 绑定成功,清空map
                                        sender.sendMessage(bdsuccess);
                                    } else {
                                        if (getPoster().getString(sender.getName() + ".id") != null
                                                && Objects.requireNonNull(getPoster().getString(sender.getName() + ".id")).contains(args[1])) {
                                            sender.sendMessage(ownsamebinding);// 你已经绑定过此ID
                                        } else {
                                            sender.sendMessage(samebinding);// 已有相同绑定
                                        }
                                        binding.put(sender.getName(), null);// 已有相同,清空map
                                    }
                                } else if (!binding.get(sender.getName()).equals(args[1])) {
                                    binding.put(sender.getName(), null);// 两次不一样，删除map重新输入
                                    sender.sendMessage(notsame);
                                }
                            } else {// 如果还在冷却中
                                sender.sendMessage(oncooldown);
                            }

                        } else if (args[0].equalsIgnoreCase("list")) {// 列出顶帖者
                            if (sender.hasPermission("bbstoper.admin")) {
                                if (topList().size() != 0) {
                                    boolean b = true;
                                    for (int i = 0; i < args[1].length(); i++) {// 判断参数是否为数字
                                        if (!Character.isDigit(args[1].charAt(i))) {
                                            b = false;
                                        }
                                    }
                                    if (b) {
                                        int page = Integer.parseInt(args[1]) - 1;// 零基的java
                                        if (page + 1 <= paging().size()) {
                                            outputToplist(sender, page);// 输出给玩家
                                        } else {
                                            sender.sendMessage(overpage);// 超出页数
                                        }
                                    } else {
                                        sender.sendMessage(invalid);// 不是数字
                                    }
                                } else {
                                    sender.sendMessage(noposter);
                                }
                            } else {
                                sender.sendMessage(nopermission);
                            }
                        } else {
                            sender.sendMessage(invalid);
                        }
                    }
                    if (args.length == 0) {
                        for (String s : help) {// 遍历帮助列表
                            sender.sendMessage(s);
                        }
                    }
                    if (args.length > 2) {
                        sender.sendMessage(invalid);
                    }
                }
            }
        }.runTaskAsynchronously(this);
        return true;
    }

    private boolean isSame(String id) {// 判断是否有重复
        boolean b = false;
        Map<String, Object> map = this.getPoster().getValues(true);// 获取所有键值的Map
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {// 遍历key
            if (key.contains(".id")) {// 如果key包含子健id
                list.add((String) map.get(key));// 把这个key获取的值放进list
            }
        }
        for (String s : list) {
            if (s.equalsIgnoreCase(id)) {// 如果此次循环到的ID包含传给方法的参数id
                b = true;
                break;
            }
        }
        return b;
    }

    private static synchronized List<ResultContainer> getRecords() {
        String url = "https://www.mcbbs.net/forum.php?mod=misc&action=viewthreadmod&tid=" + tid + "&infloat=yes&handlekey=viewthreadmod&inajax=1&ajaxtarget=fwin_content_viewthreadmod";
        return new ResultBuilder((Main) Bukkit.getPluginManager().getPlugin("BBSToper")).buildFromString(Objects.requireNonNull(HttpUtil.sendGet(url)));
    }

    private static synchronized void Getter() {// 抓取网页,此方法加锁
        ID.clear();
        Time.clear();
        List<ResultContainer> records = getRecords();
        for (ResultContainer container : records) {
            if(!container.isTopCard()){
                continue; //跳过非提升卡
            }
            ID.add(container.getUser().trim());
            Time.add(container.getDate().trim());
        }
    }

    private static void outputToplist(CommandSender p, Integer page) {
        String[] FinalList = paging().get(page).toArray(new String[0]);
        p.sendMessage(posternum + ":" + Time.size());
        p.sendMessage(FinalList);
        p.sendMessage(pageinfo.replaceAll("%PAGE%", String.valueOf(page + 1)).replaceAll("%TOTALPAGE%",
                String.valueOf(paging().size())));
    }

    private static List<List<String>> paging() {// 分页
        List<String> list = topList();
        int totalCount = list.size(); // 总条数
        int pageCount; // 总页数
        int m = totalCount % pagesize; // 余数
        if (m > 0) {
            pageCount = totalCount / pagesize + 1;
        } else {
            pageCount = totalCount / pagesize;
        }
        List<List<String>> totalList = new ArrayList<>();
        for (int i = 1; i <= pageCount; i++) {
            if (m == 0) {
                List<String> subList = list.subList((i - 1) * pagesize, pagesize * (i));
                totalList.add(subList);
            } else {
                if (i == pageCount) {
                    List<String> subList = list.subList((i - 1) * pagesize, totalCount);
                    totalList.add(subList);
                } else {
                    List<String> subList = list.subList((i - 1) * pagesize, pagesize * i);
                    totalList.add(subList);
                }
            }
        }
        return totalList;
    }

    private static List<String> topList() {// 列出顶帖列表
        Getter();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < ID.size() && i < Time.size(); i++) {
            list.add(posterid + ":" + ID.get(i) + " " + postertime + ":" + Time.get(i));
        }
        return list;
    }

    private void reloadPoster() {// 重载
        if (posterFile == null) {
            posterFile = new File(getDataFolder(), "poster.yml");
        }
        poster = YamlConfiguration.loadConfiguration(posterFile);
    }

    private FileConfiguration getPoster() {// 获取
        if (poster == null) {
            reloadPoster();
        }
        return poster;
    }

    private void savePoster() {// 保存
        if (poster == null || posterFile == null) {
            return;
        }
        try {
            this.getPoster().save(posterFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + posterFile, ex);
            getLogger().info("Save poster Error!");
        }
    }

    private void saveDefaultPoster() {// 默认覆盖
        if (posterFile == null) {
            posterFile = new File(getDataFolder(), "poster.yml");
        }
        if (!posterFile.exists()) {
            this.saveResource("poster.yml", false);
        }
    }
}
