package de.jeff_media.JukeBoxPlus.version;

import org.bukkit.Location;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;

import java.util.function.Predicate;

public class Version1_19Utils {

    private static Predicate<Entity> ALLAY_ENTITY_PREDICATE = new Predicate<Entity>() {
        @Override
        public boolean test(Entity entity) {
            return entity instanceof Allay;
        }
    };

    public static void makeAllaysDance(Location loc, double radius) {
        for(Entity entity : loc.getWorld().getNearbyEntities(loc, radius, radius, radius, ALLAY_ENTITY_PREDICATE)) {
            if(entity instanceof Allay) {
                ((Allay) entity).startDancing();
            }
        }
    }

    public static void makeAllaysStopDance(Location loc, double radius) {
        for(Entity entity : loc.getWorld().getNearbyEntities(loc, radius*4, radius*4, radius*4, ALLAY_ENTITY_PREDICATE)) {
            if(entity instanceof Allay) {
                ((Allay) entity).stopDancing();
            }
        }
    }

}
