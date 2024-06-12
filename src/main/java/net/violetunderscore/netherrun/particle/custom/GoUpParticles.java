package net.violetunderscore.netherrun.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.violetunderscore.netherrun.block.ModBlocks;

import java.util.Random;


public class GoUpParticles extends TextureSheetParticle {
    protected GoUpParticles(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.friction = 0.8F;
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.quadSize *= 0.85F;
        Random randomlifetime = new Random();
        this.lifetime = randomlifetime.nextInt(40) + 10;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;

        this.gravity = -2;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
        if (!this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).is(ModBlocks.GO_UP.get())) {
            this.gravity = 2;
        }
        else {
            this.gravity = -2;
            if (this.level.getBlockState(BlockPos.containing(this.x, this.y + 1, this.z)).is(ModBlocks.GO_UP.get())) {
                this.age = 1;
            }
        }
    }

    private void fadeOut()
    {
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new GoUpParticles(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
