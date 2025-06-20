package com.stbstudios.spikesnukes.mobeffects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RadioactiveEffect extends MobEffect {
    protected RadioactiveEffect() {
        super(MobEffectCategory.HARMFUL, 0x19812c);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        System.out.println(entity);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
