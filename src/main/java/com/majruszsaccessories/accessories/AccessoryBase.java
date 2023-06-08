package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.common.ItemBase;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.gamemodifiers.contexts.OnItemRender;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnTradeSetup;
import net.minecraftforge.registries.RegistryObject;

public class AccessoryBase extends ItemBase< AccessoryItem, AccessoryComponent, AccessoryComponent.ISupplier > {
	public AccessoryBase( RegistryObject< AccessoryItem > item ) {
		super( item, Registries.Groups.ACCESSORIES );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->data.holder.getItem().equals( this.item.get() ) ) )
			.insertTo( this.group );

		OnTradeSetup.listen( this::addTrades )
			.insertTo( this.group );

		OnItemRender.listen( this::addBoosterIcon );
	}

	private void addTrades( OnTradeSetup.Data data ) {
		this.components.stream()
			.filter( TradeOffer.class::isInstance )
			.map( TradeOffer.class::cast )
			.filter( offer->offer.getProfession() == data.profession )
			.forEach( offer->data.getTrades( offer.getTier() ).add( ( trader, random )->offer.toMerchantOffer() ) );
	}

	private void addBoosterIcon( OnItemRender.Data data ) {
		// TODO: 1.18.2 does not support IItemDecorator
	}
}
