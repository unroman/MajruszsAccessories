package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnEntitySwimSpeedMultiplierGet;
import com.mlib.data.Serializable;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import com.mlib.time.TimeHelper;
import net.minecraft.server.level.ServerLevel;

public class SwimmingSpeedBonus extends BonusComponent< AccessoryItem > {
	RangedFloat multiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 10.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new SwimmingSpeedBonus( handler, bonus );
	}

	protected SwimmingSpeedBonus( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.multiplier.set( bonus, Range.of( 0.0f, 10.0f ) );

		OnEntitySwimSpeedMultiplierGet.listen( this::increaseSwimSpeed )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.entity ) );

		this.addTooltip( "majruszsaccessories.bonuses.swim_bonus", TooltipHelper.asPercent( this.multiplier ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "swim_speed", this.multiplier::define );
	}

	private void increaseSwimSpeed( OnEntitySwimSpeedMultiplierGet data ) {
		data.multiplier *= 1.0f + CustomConditions.getLastHolder().apply( this.multiplier );
		if( data.entity.isInWater() && data.getLevel() instanceof ServerLevel && TimeHelper.haveTicksPassed( 5 ) ) {
			this.spawnEffects( data );
		}
	}

	private void spawnEffects( OnEntitySwimSpeedMultiplierGet data ) {
		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 1 )
			.emit( data.getServerLevel(), AnyPos.from( data.entity.position() ).add( 0.0f, 0.5f * data.entity.getBbHeight(), 0.0f ).vec3() );
	}
}
