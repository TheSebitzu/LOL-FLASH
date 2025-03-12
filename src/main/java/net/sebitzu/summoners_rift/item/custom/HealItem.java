package net.sebitzu.summoners_rift.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class HealItem extends Item {
    public HealItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        int healRadius = 10; // blocks
        int healAmount = 3; // 1.5 hearts
        int tickCooldown = 200; // 200 ticks (10 sec)

        if (!world.isClient()) {
            Vec3d playerPos = user.getPos();
            List<PlayerEntity> nearbyPlayers = world.getEntitiesByClass(PlayerEntity.class,
                    new Box(playerPos.x - healRadius, playerPos.y - healRadius, playerPos.z - healRadius,
                            playerPos.x + healRadius, playerPos.y + healRadius, playerPos.z + healRadius),
                    Entity::isAlive
            );

            for (PlayerEntity player : nearbyPlayers) {
                float healthToSet = Math.min(player.getMaxHealth(), player.getHealth() + healAmount);
                player.setHealth(healthToSet);
            }

            ItemStack stack = user.getStackInHand(hand);
            stack.decrement(1);
            user.getItemCooldownManager().set(stack, (tickCooldown));
        }

        return ActionResult.SUCCESS;
    }
}
