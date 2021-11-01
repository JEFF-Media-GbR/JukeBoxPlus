package de.jeff_media.JukeBoxPlus;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class TaskController {

    final Main main;


    TaskController(Main main) {
        this.main = main;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, this::checkJukeboxes, 0L, main.getConfig().getLong("tick-delay",5));

    }

    private void checkJukeboxes() {

        // TODO Skip this if no player is online
        // TODO Skip jukeboxes in worlds where no player is online or where no player is nearby

        long time = new Date().getTime();

        Iterator<Map.Entry<Block, JukeboxData>> iterator = main.jukeboxes.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Block, JukeboxData> entry = iterator.next();

            Block block = entry.getKey();
            JukeboxData jd = entry.getValue();

            // TODO: Make configurable
            if(block.getChunk().isLoaded()) {
                if(!(block.getState() instanceof Jukebox)) {
                    main.debug("Destroying JukeboxData and dropping discs, Block "+block.getLocation().toString()+" is no longer a Jukebox");
                    jd.destroy(block);
                    Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                                if (jd.file != null && jd.file.exists()) {
                                    jd.file.delete();
                                }
                            });
                    iterator.remove();
                    continue;
                }
            }


            Jukebox jb = (Jukebox) block.getState();



            if(jd.endTime > 0 ) {
                //main.debug("endTime>0");
                if (time >= jd.endTime) {
                    //main.debug("time >=endTime");


                    if ((!jd.loop
                            && !jd.shuffle)) {
                        jd.stopJukebox(jb,true);
                        continue;
                    } else {
                        jd.stopJukebox(jb,false);
                    }

                    main.debug("TaskController#checkLoopedJukeboxes(): Restarting Jukebox");
                    if (jd.shuffle) jd.randomRecord();
                    jd.setEndTime(main.songUtils.getDuration(jd.record));
                    //startLoop(entry.getKey());
                    jd.startJukebox(jb, jd.record,null);

                    main.utils.updateInventoryViews("TaskController##52");

                } else {
                    main.debug("TaskController#checkLoopedJukeboxes(): " +
                            "Time remaining: " + (jd.endTime - time) / 1000);

                }
            }
        }
    }

    void startLoop(Block block) {

        /*JukeboxData jd = main.jukeboxes.get(block);
        Jukebox jb = (Jukebox) block.getState();

        //int duration = main.songUtils.getDuration(JukeboxUtils.getJukebox(block).getPlaying());
        int duration = main.songUtils.getDuration(jd.record);

        if (duration <= 0) {
            main.debug("TaskController#startLoop(Block): Returning, duration <= 0");
            return;
        }

        jd.startJukebox(jb, jd.record);
        jd.setEndTime(duration);*/

    }
}
