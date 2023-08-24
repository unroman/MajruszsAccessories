package com.majruszsaccessories.boosters;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.boosters.components.BoosterComponent;
import com.majruszsaccessories.boosters.components.LowerSpawnRate;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnLoot;
import com.mlib.contexts.base.Condition;
import com.mlib.math.Range;
import com.mlib.modhelper.AutoInstance;
import net.minecraft.world.entity.monster.Endermite;

import java.util.function.Supplier;

@AutoInstance
public class PeaceEmblem extends BoosterBase {
	public PeaceEmblem() {
		super( Registries.PEACE_EMBLEM );

		this.name( "PeaceEmblem" )
			.add( LowerSpawnRate.create( 0.1 ) )
			.add( DropChance.create() );
	}

	static class DropChance extends BoosterComponent {
		public static ISupplier create() {
			return DropChance::new;
		}

		protected DropChance( Supplier< BoosterItem > item, ConfigGroup group ) {
			super( item );

			DoubleConfig chance = new DoubleConfig( 0.05, Range.CHANCE );
			chance.name( "drop_chance" ).comment( "Chance for Peace Emblem to drop from Endermite." );

			OnLoot.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.chance( chance ) )
				.addCondition( OnLoot.hasLastDamagePlayer() )
				.addCondition( Condition.predicate( data->data.entity instanceof Endermite ) )
				.insertTo( group );
		}
	}
}
