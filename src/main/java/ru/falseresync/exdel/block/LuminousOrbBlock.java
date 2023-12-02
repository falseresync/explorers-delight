package ru.falseresync.exdel.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LuminousOrbBlock extends Block {
    public static final VoxelShape SHAPE = VoxelShapes.cuboid(7 / 16F, 7 / 16F, 7 / 16F, 10 / 16F, 10 / 16F, 10 / 16F);

    public LuminousOrbBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextFloat() > 0.85) {
            world.addParticle(
                    ParticleTypes.SNOWFLAKE,
                    pos.getX() + 0.5 - random.nextFloat() / 4, pos.getY() + 0.625 + random.nextFloat() / 4, pos.getZ() + 0.5 - random.nextFloat() / 4,
                    0, 0, 0);
        }
    }
}
