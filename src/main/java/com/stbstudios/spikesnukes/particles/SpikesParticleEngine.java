package com.stbstudios.spikesnukes.particles;

import java.util.ArrayList;
import java.util.List;

public class SpikesParticleEngine {
    public static final List<SimpleParticle> PARTICLES = new ArrayList<>();

    public static void spawnParticle(double x, double y, double z, float size) {
        PARTICLES.add(new SimpleParticle(x, y, z, size));
    }
}
