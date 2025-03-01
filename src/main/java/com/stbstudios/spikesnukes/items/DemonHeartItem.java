package com.stbstudios.spikesnukes.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class DemonHeartItem extends Item {
    public DemonHeartItem() {
        super(new Item.Properties()
                .stacksTo(3)
                .food(new FoodProperties.Builder()
                        .saturationMod(10f)
                        .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200),0.5f)
                        .build()
                )
        );
    }
}
