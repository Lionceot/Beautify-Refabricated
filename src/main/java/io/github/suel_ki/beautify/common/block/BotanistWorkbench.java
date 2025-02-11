package io.github.suel_ki.beautify.common.block;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BotanistWorkbench extends HorizontalDirectionalBlock {
	//Map of hitboxes for direction the model can be facing
	private static final Map<Direction, VoxelShape> SHAPES_FOR_MODEL = ImmutableMap.of(
			Direction.NORTH, Shapes.or(box(2, 0, 0, 16, 12, 14.25),
					box(9.5, 12, 8.5, 13.5, 16, 12.5)),
			Direction.SOUTH, Shapes.or(box(0, 0, 1.75, 14, 12, 16),
					box(2.5, 12, 3.5, 6.5, 16, 7.5)),
			Direction.WEST, Shapes.or(box(0, 0, 0, 14.25, 12, 14),
					box(8.5, 12, 2.5, 12.5, 16, 6.5)),
			Direction.EAST, Shapes.or(box(1.75, 0, 2, 16, 12, 16),
					box(3.5, 12, 9.5, 7.5, 16, 13.5))
	);

	public static final MapCodec<BotanistWorkbench> CODEC = simpleCodec(BotanistWorkbench::new);

	public BotanistWorkbench(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<BotanistWorkbench> codec() {
		return CODEC;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPES_FOR_MODEL.get(state.getValue(FACING));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(FACING);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> component, TooltipFlag flag) {
		if (!Screen.hasShiftDown()) {
			component.add(Component.translatable("tooltip.beautify.shift").withStyle(ChatFormatting.YELLOW));
		}

		if (Screen.hasShiftDown()) {
			component.add(Component.translatable("tooltip.beautify.botanist_workbench.1")
					.withStyle(ChatFormatting.GRAY));
		}
		super.appendHoverText(stack, tooltipContext, component, flag);
	}
}
